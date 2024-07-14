package com.limelist.slices.auth.services

import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository
import com.limelist.slices.auth.services.models.*
import com.limelist.slices.internal.users.UsersInternalService
import com.limelist.slices.internal.users.models.RegistrationData
import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.shared.RequestError.ErrorCode.*

import java.util.UUID.randomUUID

class KtorTokenIssuanceService(
    val users: UsersInternalService,
    val authRepository: AuthRepository,
    val passwordHasher: PasswordHasher,
    val config: AuthConfig
) : TokenIssuanceService {

    private val tokens = TokensFactory(config)

    override suspend fun login(
        data: LoginData
    ): RequestResult<AuthenticationTokens> {

        val password = passwordHasher.hashPassword(data.password)
        val tokensData = authRepository.findTokenDataByIdentificationData(
            data.login,
            password
        )

        if (tokensData == null) {
            return RequestResult.ErrorResult(
                RequestError(
                    InvalidIdentification,
                    "Invalid login or password"
                )
            )
        }

        return  RequestResult.SuccessResult(
            tokens.create(tokensData)
        )
    }

    override suspend fun refresh(
        refreshToken: RefreshTokenData
    ): RequestResult<AccessToken> {
        val tokensData = authRepository.findTokenDataByRefreshToken(refreshToken)

        if (tokensData == null) {
            return RequestResult.ErrorResult(
                RequestError(
                    InvalidRefreshToken,
                    "Invalid refresh token"
                )
            )
        }

        return RequestResult.SuccessResult(
            tokens.createAccessToken(
                AccessTokenData(tokensData.userId)
            )
        )
    }

    override suspend fun register(
        data: RegistrationData
    ): RequestResult<AuthenticationTokens> {
        val createUsersResult = users.createNewUser(data)

        if (createUsersResult is RequestResult.ErrorResult)
            return createUsersResult

        val success = createUsersResult as RequestResult.SuccessResult

        val user = success.data;
        val refreshToken = randomUUID().toString()

        val addUserAuthDataResult = Result.runCatching {
            authRepository.add(
                user.id,
                user.email,
                data.password,
                refreshToken
            )
        }

        if (addUserAuthDataResult.isFailure){
            return RequestResult.ErrorResult(
                RequestError(
                    AuthorizationServiceRegistrationError,
                    "Unknown registration error is authorization service"
                )
            )
        }

        return  RequestResult.SuccessResult(
            tokens.create(
                AuthenticationTokensData(refreshToken, user.id)
            )
        )

    }

}