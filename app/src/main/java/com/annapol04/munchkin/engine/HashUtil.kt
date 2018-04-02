package com.annapol04.munchkin.engine

import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object HashUtil {
    fun applyMD5(input: ByteArray): ByteArray {
        try {
            return MessageDigest.getInstance("MD5").digest(input)
        } catch (e: NoSuchAlgorithmException) {
            throw RuntimeException(e)
        }
    }
}
