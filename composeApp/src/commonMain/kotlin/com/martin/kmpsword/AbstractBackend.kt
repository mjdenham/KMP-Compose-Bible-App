package com.martin.kmpsword

import okio.Path
import okio.Path.Companion.toPath

open class AbstractBackend {

    fun getExpandedDataPath(): Path /* URI */ {
//        val loc: java.net.URI = NetUtil.lengthenURI(
//            bmd.getLibrary(),
//            bmd.getProperty(ConfigEntryType.DATA_PATH) as String
//        )
//            ?: throw BookException(Msg.MISSING_FILE)
//
//        return loc
        return "/Users/martin/StudioProjects/kmp-sword/testFiles/BSB/modules/texts/ztext/bsb".toPath()
    }

}
