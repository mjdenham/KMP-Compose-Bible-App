/**
 * Distribution License:
 * JSword is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License, version 2.1 or later
 * as published by the Free Software Foundation. This program is distributed
 * in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The License is available on the internet at:
 * http://www.gnu.org/copyleft/lgpl.html
 * or by writing to:
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA
 *
 * © CrossWire Bible Society, 2005 - 2016
 *
 */
package org.crosswire.jsword.book.sword

import okio.Buffer
import okio.FileHandle
import okio.Inflater
import okio.InflaterSource
import okio.Path
import okio.Path.Companion.toPath
import okio.buffer
import okio.use
import org.crosswire.jsword.book.BookMetaData

/**
 * Various utilities used by different Sword classes.
 *
 * @author Joe Walker
 */
object SwordUtil {

    /**
     * Read a RandomAccessFile
     *
     * @param file
     * The file to read
     * @param offset
     * The start of the record to read
     * @param theSize
     * The number of bytes to read
     * @return the read data
     * @throws IOException
     * on error
     */
    internal fun readFile(file: FileHandle, offset: Int, theSize: Int): ByteArray {
        var size = theSize

        // It is common to have an entry that points to nothing.
        // That is the equivalent of an empty string.
        if (size == 0) {
            return ByteArray(0)
        }

        if (size < 0) {
//            log.error("Nothing to read at offset = {} returning empty because negative size={}", Long.toString(offset), Integer.toString(size));
            return ByteArray(0)
        }

        val fileSize = file.size()
        if (offset >= fileSize) {
//            log.error("Attempt to read beyond end. offset={} size={} but raf.length={}", Long.toString(offset), Integer.toString(size), Long.toString(rafSize));
            return ByteArray(0)
        }

        if (offset + size > fileSize) {
//            log.error("Need to reduce size to avoid EOFException. offset={} size={} but raf.length={}", Long.toString(offset), Integer.toString(size), Long.toString(rafSize));
            size = (fileSize - offset).toInt()
        }

        val sink = ByteArray(theSize)
        file.read(offset.toLong(), sink, 0, size)

        return sink
    }

    internal fun readAndInflateFile(file: FileHandle, offset: Int, theSize: Int, uncompressedSize: Int): ByteArray {
        println("readAndInflateFile offset: $offset theSize: $theSize uncompressedSize: $uncompressedSize")
        var size = theSize

        // It is common to have an entry that points to nothing.
        // That is the equivalent of an empty string.
        if (size == 0) {
            return ByteArray(0)
        }

        if (size < 0) {
//            log.error("Nothing to read at offset = {} returning empty because negative size={}", Long.toString(offset), Integer.toString(size));
            return ByteArray(0)
        }

        val fileSize = file.size()
        if (offset >= fileSize) {
//            log.error("Attempt to read beyond end. offset={} size={} but raf.length={}", Long.toString(offset), Integer.toString(size), Long.toString(rafSize));
            return ByteArray(0)
        }

        if (offset + size > fileSize) {
//            log.error("Need to reduce size to avoid EOFException. offset={} size={} but raf.length={}", Long.toString(offset), Integer.toString(size), Long.toString(rafSize));
            size = (fileSize - offset).toInt()
        }

        Buffer().use { inflated ->
            val inflatingBuffer = InflaterSource(file.source(offset.toLong()).buffer(), Inflater())
            val written = inflated.write(inflatingBuffer, uncompressedSize.toLong())
            println("bytesInflated: $written")

            return inflated.readByteArray()
        }
    }

    /**
     * Decode little endian data from a byte array. This assumes that the high
     * order bit is not set as this is used solely for an offset in a file in
     * bytes. For a practical limit, 2**31 is way bigger than any document that
     * we can have.
     *
     * @param data
     * the byte[] from which to read 4 bytes
     * @param offset
     * the offset into the array
     * @return The decoded data
     */
    fun decodeLittleEndian32(data: ByteArray, offset: Int): Int {
        // Convert from a byte to an int, but prevent sign extension.
        // So -16 becomes 240
        val byte1 = data[0 + offset].toInt() and 0xFF
        val byte2 = (data[1 + offset].toInt() and 0xFF) shl 8
        val byte3 = (data[2 + offset].toInt() and 0xFF) shl 16
        val byte4 = (data[3 + offset].toInt() and 0xFF) shl 24

        return byte4 or byte3 or byte2 or byte1
    }

    /**
     * Encode little endian data from a byte array. This assumes that the number
     * fits in a Java integer. That is, the range of an unsigned C integer is
     * greater than a signed Java integer. For a practical limit, 2**31 is way
     * bigger than any document that we can have. If this ever doesn't work, use
     * a long for the number.
     *
     * @param val
     * the number to encode into little endian
     * @param data
     * the byte[] from which to write 4 bytes
     * @param offset
     * the offset into the array
     */
    internal fun encodeLittleEndian32(`val`: Int, data: ByteArray, offset: Int) {
        data[0 + offset] = (`val` and 0xFF).toByte()
        data[1 + offset] = ((`val` shr 8) and 0xFF).toByte()
        data[2 + offset] = ((`val` shr 16) and 0xFF).toByte()
        data[3 + offset] = ((`val` shr 24) and 0xFF).toByte()
    }

    /**
     * Decode little endian data from a byte array
     *
     * @param data
     * the byte[] from which to read 2 bytes
     * @param offset
     * the offset into the array
     * @return The decoded data
     */
    internal fun decodeLittleEndian16(data: ByteArray, offset: Int): Int {
        // Convert from a byte to an int, but prevent sign extension.
        // So -16 becomes 240
        val byte1 = data[0 + offset].toInt() and 0xFF
        val byte2 = (data[1 + offset].toInt() and 0xFF) shl 8

        return byte2 or byte1
    }

    /**
     * Encode a 16-bit little endian from an integer. It is assumed that the
     * integer's lower 16 bits are the only that are set.
     *
     * @param data
     * the byte[] from which to write 2 bytes
     * @param offset
     * the offset into the array
     */
    internal fun encodeLittleEndian16(`val`: Int, data: ByteArray, offset: Int) {
        data[0 + offset] = (`val` and 0xFF).toByte()
        data[1 + offset] = ((`val` shr 8) and 0xFF).toByte()
    }

    /**
     * Find a byte of data in an array
     *
     * @param data
     * The array to search
     * @param sought
     * The data to search for
     * @return The index of the found position or -1 if not found
     */
    internal fun findByte(data: ByteArray, sought: Byte): Int {
        return findByte(data, 0, sought)
    }

    /**
     * Find a byte of data in an array
     *
     * @param data
     * The array to search
     * @param offset
     * The position in the array to begin looking
     * @param sought
     * The data to search for
     * @return The index of the found position or -1 if not found
     */
    internal fun findByte(data: ByteArray, offset: Int, sought: Byte): Int {
        for (i in offset until data.size) {
            if (data[i] == sought) {
                return i
            }
        }

        return -1
    }

    /**
     * Transform a byte array into a string given the encoding. If the encoding
     * is bad then it just does it as a string.
     * Note: this may modify data. Don't use it to examine data.
     *
     * @param key the key
     * @param data
     * The byte array to be converted
     * @param charset
     * The encoding of the byte array
     * @return a string that is UTF-8 internally
     */
    fun decode(key: String?, data: ByteArray, charset: String): String {
        return decode(key, data, 0, data.size, charset)
    }

    /**
     * Transform a portion of a byte array into a string given the encoding. If
     * the encoding is bad then it just does it as a string.
     * Note: this may modify data. Don't use it to examine data.
     *
     * @param key the key
     * @param data
     * The byte array to be converted
     * @param length
     * The number of bytes to use.
     * @param charset
     * The encoding of the byte array
     * @return a string that is UTF-8 internally
     */
    fun decode(key: String?, data: ByteArray, length: Int, charset: String): String {
        return decode(key, data, 0, length, charset)
    }

    /**
     * Transform a portion of a byte array starting at an offset into a string
     * given the encoding. If the encoding is bad then it just does it as a
     * string. Note: this may modify data. Don't use it to examine data.
     *
     * @param key the key
     * @param data
     * The byte array to be converted
     * @param offset
     * The starting position in the byte array
     * @param length
     * The number of bytes to use.
     * @param charset
     * The encoding of the byte array
     * @return a string that is UTF-8 internally
     */
    fun decode(key: String?, data: ByteArray, offset: Int, length: Int, charset: String): String {
        if ("WINDOWS-1252" == charset) {
            clean1252(key, data, offset, length)
        }
        var txt = ""
        try {
            if (offset + length <= data.size) {
                txt = data.decodeToString(offset, length) //String(data, offset, length, charset(charset))
            }
        } catch (ex: Exception) {
            // It is impossible! In case, use system default...
//            log.error("{}: Encoding {} not supported.", key, charset, ex);
//            txt = String(data, offset, length)
            ex.printStackTrace()
        }

        return txt
    }

    /**
     * Remove rogue characters in the source. These are characters that are not
     * valid in cp1252 aka WINDOWS-1252 and in UTF-8 or are non-printing control
     * characters in the range of 0-32.
     */
    private fun clean1252(key: String?, data: ByteArray, offset: Int, length: Int) {
        var end = offset + length
        // make sure it doesn't go off the end
        if (end > data.size) {
            end = data.size
        }
        for (i in offset until end) {
            // between 0-32 only allow whitespace: \t, \n, \r, ' '
            // characters 0x81, 0x8D, 0x8F, 0x90 and 0x9D are undefined in
            // cp1252
            val c = data[i].toInt() and 0xFF
            if ((c >= 0x00 && c < 0x20 && c != 0x09 && c != 0x0A && c != 0x0D) || (c == 0x81 || c == 0x8D || c == 0x8F || c == 0x90 || c == 0x9D)) {
                data[i] = 0x20
                //                log.error("{} has bad character 0x{} at position {} in input.", key, Integer.toString(c, 16), Integer.toString(i));
            }
        }
    }


    /**
     * Returns where the book should be located
     * @param bookMetaData meta information about the book
     * @return the URI locating the resource
     * @throws BookException thrown if an issue is encountered, e.g. missing data files.
     */
    fun getExpandedDataPath(bookMetaData: BookMetaData): Path {
//        val loc: java.net.URI = NetUtil.lengthenURI(
//            bookMetaData.getLibrary(),
//            bookMetaData.getProperty(SwordBookMetaData.KEY_DATA_PATH)
//        )
//            ?: // FIXME(DMS): missing parameter
//            throw BookException(JSOtherMsg.lookupText("Missing data files for old and new testaments in {0}."))
        bookMetaData.getProperty(SwordBookMetaData.KEY_DATA_PATH)?.let { modulePath ->
            return bookMetaData.library.toPath().resolve(modulePath)
        } ?: throw Exception("Missing data files for old and new testaments in {0}.")
    }
    /**
     * The log stream
     */
    //    private static final Logger log = LoggerFactory.getLogger(SwordUtil.class);
}
