package com.limelist.slices.auth.services

interface PasswordHasher {
    fun hashPassword(password: String): String
    fun verifyPassword(password: String, hash: String): Boolean
}