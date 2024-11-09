package org.crosswire.jsword.book.sword.state

import org.crosswire.jsword.versification.Testament

data class LastLoadedBlock(val testament: Testament, val blockNum: Int, val uncompressed: ByteArray)
