package org.crosswire.jsword.book.sword.state

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import org.crosswire.jsword.book.BookMetaData
import org.crosswire.jsword.book.sword.BlockType
import kotlin.coroutines.EmptyCoroutineContext

object OpenFileStateManager {

    private val metaToState = mutableMapOf<BookMetaData, ZVerseBackendState>()

    private var releaseResourceJob: Job? = null

    // Allow 30 seconds for BackendState to be reused before it is released
    private const val TIME_TO_RELEASE_MILLIS: Long = 1000 * 10

    fun getZVerseBackendState(metadata: BookMetaData, blockType: BlockType): ZVerseBackendState {
        var state = metaToState[metadata]
        if (state == null) {
            state = ZVerseBackendState(metadata, blockType)
            metaToState[metadata] = state
        }
        state.lastAccess = timeMillis()

        return state
    }

    fun release(fileState: OpenFileState) {
        releaseResourceJob?.cancel()
        val releaseRequestTime = timeMillis()
        fileState.lastAccess = releaseRequestTime

        //TODO consider implementing JSword's more thorough approach
        releaseResourceJob = CoroutineScope(EmptyCoroutineContext).launch {
            delay(TIME_TO_RELEASE_MILLIS)
            if (releaseRequestTime == fileState.lastAccess) {
                metaToState.remove(fileState.bookMetaData)?.let { removedState ->
                    if (removedState == fileState && releaseRequestTime == removedState.lastAccess) {
                        fileState.releaseResources()
                    }
                }
            }
        }
    }

    private fun timeMillis() = Clock.System.now().toEpochMilliseconds()
}