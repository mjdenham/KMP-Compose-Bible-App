package org.crosswire.common.util

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.prepareGet
import io.ktor.client.utils.DEFAULT_HTTP_BUFFER_SIZE
import io.ktor.utils.io.ByteReadChannel
import io.ktor.utils.io.readRemaining
import kotlinx.io.readByteArray
import okio.FileSystem
import okio.Path
import okio.SYSTEM
import okio.buffer
import okio.use

class WebResource() {
    private val client = HttpClient()

    suspend fun download(url: String, filePath: Path): Boolean {
        println("ZZZZZ Downloading $url to $filePath")
        var ok = false
        var totalBytes = 0
        filePath.createParentDirectories()

        println("ZZZZZ Downloading 1")
        FileSystem.SYSTEM.sink(filePath).buffer().use { sink ->
            println("ZZZZZ Downloading 2")
            client.prepareGet(url).execute { httpResponse ->
                println("ZZZZZ Downloading 3")
                val channel: ByteReadChannel = httpResponse.body()
                while (!channel.isClosedForRead) {
                    println("ZZZZZ Downloading 4")
                    val packet = channel.readRemaining(DEFAULT_HTTP_BUFFER_SIZE.toLong())
                    while (!packet.exhausted()) {
                        val bytes = packet.readByteArray()
                        sink.write(bytes)
                        totalBytes += bytes.size
                        ok = true
                    }
                }
            }
        }
        println("ZZZZZ Downloaded ${totalBytes/1024} KB")

        return ok
    }
}