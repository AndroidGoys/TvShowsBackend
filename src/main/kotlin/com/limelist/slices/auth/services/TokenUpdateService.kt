package com.limelist.slices.auth.services

import com.limelist.slices.auth.services.models.AccessTokenData
import com.limelist.slices.auth.services.models.AuthenticationTokens
import com.limelist.slices.auth.services.models.LoginData

interface TokenUpdateService {
    fun updateToken(userId: Int, oldLoginData: LoginData, newLoginData: LoginData) : AuthenticationTokens
    fun registerNewUser(userId: Int, loginData: LoginData) : AuthenticationTokens
}