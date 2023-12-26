package com.huseyincan.eventdriven.model.data.converter

import android.graphics.Bitmap
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock

@RunWith(JUnit4::class)
class BitmapBlobTest {
    private val bitmapBlob = BitmapBlob()

    @Test
    fun testFromBitmap() {
        // Create a Bitmap object
        val bitmap = mock(Bitmap::class.java)

        val byteArray = bitmapBlob.fromBitmap(bitmap)

        // Check that the ByteArray is not null
        assertNotNull(byteArray)

    }
}