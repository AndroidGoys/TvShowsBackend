package com.limelist.slices.auth.services

import java.security.SecureRandom
import java.security.spec.KeySpec
import java.util.*
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


class PBKDF2Hasher : PasswordHasher {
    private val random: SecureRandom = SecureRandom()
    private val iterations = 2 shl 10
    private val hashLength = 128

    @OptIn(ExperimentalEncodingApi::class)
    override fun hashPassword(password: String): String {
        val salt = ByteArray(hashLength / 8)
        random.nextBytes(salt)

        val hash = pbkdf2(password, salt, iterations)
        val token = ByteArray(salt.size + hash.size)

        salt.copyInto(token, 0, 0, salt.size)
        hash.copyInto(token, salt.size, 0, hash.size)

        return Base64.encode(token)
    }

    @OptIn(ExperimentalEncodingApi::class)
    override fun verifyPassword(password: String, token: String): Boolean {
        val hash: ByteArray = Base64.decode(token)
        val salt: ByteArray = hash.copyOf(hashLength / 8)
        val check = pbkdf2(password, salt, iterations)

        for (i in 0 until check.size){
            if (hash[i + salt.size] != check[i]){
                return false
            }
        }
        return true;
    }


    private fun pbkdf2(password: String, salt: ByteArray, iterations: Int): ByteArray {
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), salt, iterations, hashLength)

        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1")
        return factory.generateSecret(spec).encoded
    }
}