package com.example.nfctestconnectedsealed

import android.nfc.tech.MifareUltralight
import android.nfc.tech.NfcA
import android.util.Log
import com.example.nfctestconnectedsealed.utils.toHex
import java.io.IOException
import java.nio.charset.Charset

class MifarUltralightInfo(tag : MifareUltralight) {
    val type: Int
    val timeout: Int
    val maxTransceiveLength: Int
    val atqa: String
    val sak: Int
    val data: String
    //val write : Unit

    init {
        type = tag.type
        timeout = tag.timeout
        maxTransceiveLength = tag.maxTransceiveLength
        // MifareUltralight tags are also NfcA tags
        val nfca = NfcA.get(tag.tag)
        atqa = nfca.atqa.toHex()
        sak = nfca.sak.toInt()
        // Read memory data
        data = readData(tag)
        //write = writeData(tag)
    }

    private fun readData(tag: MifareUltralight): String {
        try {
            var dataAccumulator = ""
            // Connect to the tag
            tag.connect()
            // Get the number of readable pages
            val readablePagesCount = when (type) {
                // Ultralight has 16 pages, all readable
                MifareUltralight.TYPE_ULTRALIGHT -> 16
                // Ultralight C has 48 pages, but the last 4 are unreadable
                MifareUltralight.TYPE_ULTRALIGHT_C -> 44
                else -> 0
            }
            // Read every fourth page, as 4 pages are read at a time by `readPages()`
            for (currentPage in 0 until readablePagesCount) {
                if (currentPage % 4 == 0) dataAccumulator += tag.readPages(currentPage).toHex()
                Log.d("tagaaaf","${tag.readPages(currentPage)}")
            }

            tag.close()

            return dataAccumulator

        } catch (e: IOException) {
            e.localizedMessage?.let { Log.e("MifareUltralightHelper", it) }
            return ""
        }
    }

}