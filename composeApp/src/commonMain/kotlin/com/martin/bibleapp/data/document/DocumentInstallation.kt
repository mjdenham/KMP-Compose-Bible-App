package com.martin.bibleapp.data.document

import com.martin.bibleapp.domain.install.Installation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.SYSTEM
import org.crosswire.common.util.IoUtil
import org.crosswire.common.util.WebResource
import org.crosswire.jsword.book.sword.SwordBookPath
import org.crosswire.jsword.book.sword.SwordConstants

class DocumentInstallation: Installation {
    private val webResource = WebResource()
    private val ioUtil = IoUtil()
    private var isInstalled = mutableMapOf<String, Boolean>()

    override suspend fun isInstalled(moduleName: String): Boolean = withContext(Dispatchers.IO) {
        if (isInstalled[moduleName] != true) {
            isInstalled[moduleName] = FileSystem.SYSTEM.exists(SwordBookPath.swordBookPath.resolve("mods.d/${moduleName.lowercase()}.conf"))
        }
        isInstalled[moduleName] ?: false
    }

    /*
    Android:
19:31:45.973  I  Downloaded 7973 KB
19:31:45.978  I  Unzipped: mods.d/bsb.conf to /data/user/0/com.martin.bibleapp/cache/sword_book/mods.d/bsb.conf; 3533 bytes written
19:31:45.980  I  Unzipped: modules/texts/ztext/bsb/nt.bzs to /data/user/0/com.martin.bibleapp/cache/sword_book/modules/texts/ztext/bsb/nt.bzs; 336 bytes written
19:31:45.981  I  Unzipped: modules/texts/ztext/bsb/nt.bzv to /data/user/0/com.martin.bibleapp/cache/sword_book/modules/texts/ztext/bsb/nt.bzv; 82460 bytes written
19:31:45.990  I  Unzipped: modules/texts/ztext/bsb/nt.bzz to /data/user/0/com.martin.bibleapp/cache/sword_book/modules/texts/ztext/bsb/nt.bzz; 1934455 bytes written
19:31:45.991  I  Unzipped: modules/texts/ztext/bsb/ot.bzs to /data/user/0/com.martin.bibleapp/cache/sword_book/modules/texts/ztext/bsb/ot.bzs; 480 bytes written
19:31:45.993  I  Unzipped: modules/texts/ztext/bsb/ot.bzv to /data/user/0/com.martin.bibleapp/cache/sword_book/modules/texts/ztext/bsb/ot.bzv; 241150 bytes written
19:31:46.019  I  Unzipped: modules/texts/ztext/bsb/ot.bzz to /data/user/0/com.martin.bibleapp/cache/sword_book/modules/texts/ztext/bsb/ot.bzz; 6103215 bytes written
19:31:46.020  I  App setup complete
IOS:
Downloaded 7973 KB
Unzipped: mods.d/bsb.conf to /Users/martin/Library/Developer/CoreSimulator/Devices/9489B8F3-BA87-4C77-B30A-A16F45FA4EAF/data/Containers/Data/Application/9FD4E222-E033-47D1-B71A-5DD54FFF4E32/tmp/sword_book/mods.d/bsb.conf; 3533 bytes written
Unzipped: modules/texts/ztext/bsb/nt.bzs to /Users/martin/Library/Developer/CoreSimulator/Devices/9489B8F3-BA87-4C77-B30A-A16F45FA4EAF/data/Containers/Data/Application/9FD4E222-E033-47D1-B71A-5DD54FFF4E32/tmp/sword_book/modules/texts/ztext/bsb/nt.bzs; 336 bytes written
Unzipped: modules/texts/ztext/bsb/nt.bzv to /Users/martin/Library/Developer/CoreSimulator/Devices/9489B8F3-BA87-4C77-B30A-A16F45FA4EAF/data/Containers/Data/Application/9FD4E222-E033-47D1-B71A-5DD54FFF4E32/tmp/sword_book/modules/texts/ztext/bsb/nt.bzv; 82460 bytes written
Unzipped: modules/texts/ztext/bsb/nt.bzz to /Users/martin/Library/Developer/CoreSimulator/Devices/9489B8F3-BA87-4C77-B30A-A16F45FA4EAF/data/Containers/Data/Application/9FD4E222-E033-47D1-B71A-5DD54FFF4E32/tmp/sword_book/modules/texts/ztext/bsb/nt.bzz; 1934455 bytes written
Unzipped: modules/texts/ztext/bsb/ot.bzs to /Users/martin/Library/Developer/CoreSimulator/Devices/9489B8F3-BA87-4C77-B30A-A16F45FA4EAF/data/Containers/Data/Application/9FD4E222-E033-47D1-B71A-5DD54FFF4E32/tmp/sword_book/modules/texts/ztext/bsb/ot.bzs; 480 bytes written
Unzipped: modules/texts/ztext/bsb/ot.bzv to /Users/martin/Library/Developer/CoreSimulator/Devices/9489B8F3-BA87-4C77-B30A-A16F45FA4EAF/data/Containers/Data/Application/9FD4E222-E033-47D1-B71A-5DD54FFF4E32/tmp/sword_book/modules/texts/ztext/bsb/ot.bzv; 241150 bytes written
Unzipped: modules/texts/ztext/bsb/ot.bzz to /Users/martin/Library/Developer/CoreSimulator/Devices/9489B8F3-BA87-4C77-B30A-A16F45FA4EAF/data/Containers/Data/Application/9FD4E222-E033-47D1-B71A-5DD54FFF4E32/tmp/sword_book/modules/texts/ztext/bsb/ot.bzz; 6103215 bytes written
App setup complete
     */
    override suspend fun install(moduleName: String) = withContext(Dispatchers.IO) {
        val url = getDownloadUrl(moduleName)
        val zipFile = getZipFile(moduleName)
        if (webResource.download(url, zipFile)) {
            ioUtil.unpackZip(
                zipFile,
                SwordBookPath.swordBookPath,
                true,
                SwordConstants.DIR_CONF,
                SwordConstants.DIR_DATA
            )
        }
    }

    private fun getDownloadUrl(moduleName: String) = downloadUrl.replace(moduleNameKey, moduleName)
    private fun getZipFile(moduleName: String) = tempDownloadPath.resolve("$moduleName.zip")

    companion object {
        const val moduleNameKey = "{NAME}"
        const val downloadUrl = "https://www.crosswire.org/ftpmirror/pub/sword/packages/rawzip/$moduleNameKey.zip"
        val tempDownloadPath = FileSystem.SYSTEM_TEMPORARY_DIRECTORY
    }
}