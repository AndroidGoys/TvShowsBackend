package com.limelist.slices.auth.services

interface PasswordHasher {
    fun hashPassword(password: String): String
}