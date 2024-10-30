package com.martin.bibleapp.fakes

import com.martin.bibleapp.domain.install.Installation

class FakeInstallation: Installation {
    override suspend fun isInstalled(moduleName: String): Boolean {
        return true
    }

    override suspend fun install(moduleName: String) {
        // NOP
    }
}