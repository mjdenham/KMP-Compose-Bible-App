/**
 * Distribution License:
 * JSword is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License, version 2.1 as published by
 * the Free Software Foundation. This program is distributed in the hope
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * The License is available on the internet at:
 * http://www.gnu.org/copyleft/lgpl.html
 * or by writing to:
 * Free Software Foundation, Inc.
 * 59 Temple Place - Suite 330
 * Boston, MA 02111-1307, USA
 *
 * Copyright: 2005
 * The copyright to this program is held by it's authors.
 *
 * ID: $Id$
 */
package com.martin.kmpsword.sword

import com.martin.kmpsword.sword.inflate.MessageInflater
import okio.Buffer
import okio.FileHandle
import okio.use

/**
 * Various utilities used by different Sword classes.
 *
 * @see gnu.lgpl.License for license details.<br></br>
 * The copyright to this program is held by it's authors.
 *
 * @author Joe Walker [joe at eireneh dot com]
 */
object SwordUtil {
    /**
     * Read a RandomAccessFile
     *
     * @param raf
     * The file to read
     * @param offset
     * The start of the record to read
     * @param theSize
     * The number of bytes to read
     * @return the read data
     * @throws IOException
     * on error
     */
    internal fun readRAF(raf: FileHandle, offset: Int, theSize: Int): ByteArray {
//        raf.seek(offset)
//
//        val offset: Long = raf.getFilePointer()
//        var size = theSize
//
//        if (offset + size > raf.length()) {
//            DataPolice.report("Need to reduce size to avoid EOFException. offset=" + offset + " size=" + size + " but raf.length=" + raf.length()) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//            size = (raf.length() - offset).toInt()
//        }
//
//        if (size < 1) {
//            DataPolice.report("Nothing to read at offset = $offset returning empty because size=$size") //$NON-NLS-1$ //$NON-NLS-2$
//            return ByteArray(0)
//        }

//        val read = ByteArray(theSize)
//        raf.read(offset.toLong(), read, 0, theSize)

        val read = ByteArray(theSize)
        raf.read(offset.toLong(), read, 0, theSize)

        return read
    }

    internal fun readAndInflateRAF(raf: FileHandle, offset: Int, theSize: Int, uncompressedSize: Int): ByteArray {
        println("readAndInflateRAF offset: $offset theSize: $theSize uncompressedSize: $uncompressedSize")
//        raf.seek(offset)
//
//        val offset: Long = raf.getFilePointer()
//        var size = theSize
//
//        if (offset + size > raf.length()) {
//            DataPolice.report("Need to reduce size to avoid EOFException. offset=" + offset + " size=" + size + " but raf.length=" + raf.length()) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//            size = (raf.length() - offset).toInt()
//        }
//
//        if (size < 1) {
//            DataPolice.report("Nothing to read at offset = $offset returning empty because size=$size") //$NON-NLS-1$ //$NON-NLS-2$
//            return ByteArray(0)
//        }

//        val read = ByteArray(theSize)
//        raf.read(offset.toLong(), read, 0, theSize)

        val sink = Buffer()
        val bytesRead = raf.read(offset.toLong(), sink, theSize.toLong())
        println("bytesRead: $bytesRead sink size: ${sink.size}")

        // idea taken from https://github.com/square/okio/issues/570
        // but that reports EOFException
        val inflated = MessageInflater().use {
            it.inflate(sink, uncompressedSize)
        }

        return inflated.readByteArray()
    }


//    /**
//     * Writes "data" to a RandomAccessFile at the "offset" position
//     *
//     * @param raf
//     * RandomAccessFile
//     * @param offset
//     * offset to write at
//     * @param data
//     * data to write
//     * @throws IOException
//     * on error
//     */
//    @Throws(java.io.IOException::class)
//    internal fun writeRAF(raf: java.io.RandomAccessFile, offset: Long, data: ByteArray?) {
//        raf.seek(offset)
//        writeNextRAF(raf, data)
//    }
//
//    @Throws(java.io.IOException::class)
//    internal fun writeNextRAF(raf: java.io.RandomAccessFile, data: ByteArray?) {
//        if (data == null) {
//            return
//        }
//        raf.write(data)
//    }
//
//    /**
//     * Read a RandomAccessFile until a particular byte is seen
//     *
//     * @param raf
//     * The file to read
//     * @param offset
//     * The start of the record to read
//     * @param stopByte
//     * The point at which to stop reading
//     * @return the read data
//     * @throws IOException
//     * on error
//     */
//    @Throws(java.io.IOException::class)
//    internal fun readUntilRAF(
//        raf: java.io.RandomAccessFile,
//        offset: Int,
//        stopByte: Byte
//    ): ByteArray {
//        raf.seek(offset.toLong())
//        return readUntilRAF(raf, stopByte)
//    }
//
//    /**
//     * Read a RandomAccessFile until a particular byte is seen
//     *
//     * @param raf
//     * The file to read
//     * @param stopByte
//     * The point at which to stop reading
//     * @return the read data
//     * @throws IOException
//     * on error
//     */
//    @Throws(java.io.IOException::class)
//    internal fun readUntilRAF(raf: java.io.RandomAccessFile, stopByte: Byte): ByteArray {
//        // The strategy used here is to read the file twice.
//        // Once to determine how much to read and then getting the actual data.
//        // It may be more efficient to incrementally build up a byte buffer.
//        // Note: that growing a static array by 1 byte at a time is O(n**2)
//        // This is negligible when the n is small, but prohibitive otherwise.
//        val offset: Long = raf.getFilePointer()
//        var size = 0
//
//        var nextByte = -1
//        do {
//            nextByte = raf.read()
//
//            size++
//        } while (nextByte != -1 && nextByte != stopByte.toInt())
//
//        // Note: we allow for nextByte == -1 to be included in size
//        // so that readRAF will report EOF errors
//        return readRAF(raf, offset, size)
//    }
//
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
    internal fun decodeLittleEndian32(data: ByteArray, offset: Int): Int {
        // Convert from a byte to an int, but prevent sign extension.
        // So -16 becomes 240
        val byte1 = data[0 + offset].toInt() and 0xFF
        val byte2 = (data[1 + offset].toInt() and 0xFF) shl 8
        val byte3 = (data[2 + offset].toInt() and 0xFF) shl 16
        val byte4 = (data[3 + offset].toInt() and 0xFF) shl 24

        return byte4 or byte3 or byte2 or byte1
    }

//    /**
//     * Encode little endian data from a byte array. This assumes that the number
//     * fits in a Java integer. That is, the range of an unsigned C integer is
//     * greater than a signed Java integer. For a practical limit, 2**31 is way
//     * bigger than any document that we can have. If this ever doesn't work, use
//     * a long for the number.
//     *
//     * @param val
//     * the number to encode into little endian
//     * @param data
//     * the byte[] from which to write 4 bytes
//     * @param offset
//     * the offset into the array
//     */
//    internal fun encodeLittleEndian32(`val`: Int, data: ByteArray, offset: Int) {
//        data[0 + offset] = (`val` and 0xFF).toByte()
//        data[1 + offset] = ((`val` shr 8) and 0xFF).toByte()
//        data[2 + offset] = ((`val` shr 16) and 0xFF).toByte()
//        data[3 + offset] = ((`val` shr 24) and 0xFF).toByte()
//    }
//
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

//    /**
//     * Encode a 16-bit little endian from an integer. It is assumed that the
//     * integer's lower 16 bits are the only that are set.
//     *
//     * @param data
//     * the byte[] from which to write 2 bytes
//     * @param offset
//     * the offset into the array
//     */
//    internal fun encodeLittleEndian16(`val`: Int, data: ByteArray, offset: Int) {
//        data[0 + offset] = (`val` and 0xFF).toByte()
//        data[1 + offset] = ((`val` shr 8) and 0xFF).toByte()
//    }
//
//    /**
//     * Find a byte of data in an array
//     *
//     * @param data
//     * The array to search
//     * @param sought
//     * The data to search for
//     * @return The index of the found position or -1 if not found
//     */
//    internal fun findByte(data: ByteArray, sought: Byte): Int {
//        return findByte(data, 0, sought)
//    }
//
//    /**
//     * Find a byte of data in an array
//     *
//     * @param data
//     * The array to search
//     * @param offset
//     * The position in the array to begin looking
//     * @param sought
//     * The data to search for
//     * @return The index of the found position or -1 if not found
//     */
//    internal fun findByte(data: ByteArray, offset: Int, sought: Byte): Int {
//        for (i in offset until data.size) {
//            if (data[i] == sought) {
//                return i
//            }
//        }
//
//        return -1
//    }
//
    /**
     * Transform a byte array into a string given the encoding. If the encoding
     * is bad then it just does it as a string.
     *
     * @param data
     * The byte array to be converted
     * @param charset
     * The encoding of the byte array
     * @return a string that is UTF-8 internally
     */
    fun decode(key: String, data: ByteArray, charset: String): String {
        return decode(key, data, 0, data.size, charset)
    }

    /**
     * Transform a portion of a byte array into a string given the encoding. If
     * the encoding is bad then it just does it as a string.
     *
     * @param data
     * The byte array to be converted
     * @param length
     * The number of bytes to use.
     * @param charset
     * The encoding of the byte array
     * @return a string that is UTF-8 internally
     */
    fun decode(key: String, data: ByteArray, length: Int, charset: String): String {
        return decode(key, data, 0, length, charset)
    }

    /**
     * Transform a portion of a byte array starting at an offset into a string
     * given the encoding. If the encoding is bad then it just does it as a
     * string.
     *
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
    fun decode(key: String, data: ByteArray, offset: Int, length: Int, charset: String): String {
        if ("WINDOWS-1252" == charset) //$NON-NLS-1$
        {
            clean1252(key, data, length)
        }
        val txt =
        try {
            data.decodeToString(offset, length)
        } catch (ex: Exception) { //TODO java.io.UnsupportedEncodingException) {
            // It is impossible! In case, use system default...
            //log.error("$key: Encoding: $charset not supported", ex) //$NON-NLS-1$ //$NON-NLS-2$
            //txt = String(data, offset, length)
            ex.printStackTrace()
            "Error"
        }

        return txt
    }

    /**
     * Remove rogue characters in the source. These are characters that are not
     * valid in cp1252 aka WINDOWS-1252 and in UTF-8 or are non-printing control
     * characters in the range of 0-32.
     */
    /**
     * Remove rogue characters in the source. These are characters that are not
     * valid in cp1252 aka WINDOWS-1252 and in UTF-8 or are non-printing control
     * characters in the range of 0-32.
     */
    fun clean1252(key: String, data: ByteArray, length: Int = data.size) {
        for (i in 0 until length) {
            // between 0-32 only allow whitespace
            // characters 0x81, 0x8D, 0x8F, 0x90 and 0x9D are undefined in
            // cp1252
            val c = data[i].toInt() and 0xFF
            if ((c >= 0x00 && c < 0x20 && c != 0x09 && c != 0x0A && c != 0x0D) || (c == 0x81 || c == 0x8D || c == 0x8F || c == 0x90 || c == 0x9D)) {
                data[i] = 0x20
//                DataPolice.report(key + " has bad character 0x" + c.toString(16) + " at position " + i + " in input.") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
            }
        }
    }
//
//    /**
//     * The log stream
//     */
//    private val log: Logger = Logger.getLogger(SwordUtil::class.java)
}
