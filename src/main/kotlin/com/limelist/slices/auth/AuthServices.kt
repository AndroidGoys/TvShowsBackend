package com.limelist.slices.auth

import com.limelist.slices.auth.services.TokenIssuanceService

data class AuthServices (
    val tokenIssuanceService: TokenIssuanceService
)