package com.martin.kmpsword.sword.inflate

import okio.Buffer
import okio.Closeable
import okio.EOFException
import okio.IOException
import okio.Inflater
import okio.InflaterSource
import okio.buffer
import okio.inflate

class MessageInflater : Closeable {
//  private val source = Buffer()
//  private val inflater = Inflater(true /* omit zlib header */)
//  private val inflaterSource = InflaterSource(source, inflater)

  @Throws(IOException::class)
  fun inflate(source: Buffer, uncompressedSize: Int): Buffer {
    val out = Buffer()
    val inflate = source.inflate().read(out, uncompressedSize.toLong())
    return out
  }

  @Throws(IOException::class)
  override fun close() {
//    inflater.end()
  }
}