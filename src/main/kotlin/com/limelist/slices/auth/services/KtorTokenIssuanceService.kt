package com.limelist.slices.auth.services

import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository
import com.limelist.slices.auth.services.models.*
import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.AuthorizationServiceRegistrationError
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.ExpiredRefreshToken
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.InvalidLoginOrPassword
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.InvalidRefreshToken
import com.limelist.slices.shared.RequestError.ErrorCode.Companion.LoginExistsError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.shared.getCurrentUnixUtc0TimeSeconds
import com.limelist.slices.users.services.internal.UsersCreationInternalService
import com.limelist.slices.users.services.models.RegistrationData


class KtorTokenIssuanceService(
    val users: UsersCreationInternalService,
    val authRepository: AuthRepository,
    val passwordHasher: PasswordHasher,
    val config: AuthConfig
) : TokenIssuanceService {

    private val tokens = TokensFactory(config)

    override suspend fun login(
        loginData: LoginData
    ): RequestResult<AuthenticationTokens> {
        val fromRepository = authRepository.findByLogin(
            loginData.login
        )

        if (fromRepository == null) {
            return RequestResult.FailureResult(
                RequestError(
                    InvalidLoginOrPassword,
                    "Invalid login or password"
                )
            )
        }

        val hasValidPassword = passwordHasher.verifyPassword(
            loginData.password,
            fromRepository.hashedPassword
        )

        if (!hasValidPassword) {
            return RequestResult.FailureResult(
                RequestError(
                    InvalidLoginOrPassword,
                    "Invalid login or password"
                )
            )
        }

        return RequestResult.SuccessResult(
            tokens.create(
                AuthenticationTokensData(
                    fromRepository.userId,
                    fromRepository.lastUpdate
                )
            )
        )
    }

    override suspend fun refresh(
        refreshToken: RefreshTokenData
    ): RequestResult<AccessToken> {
        val tokensData = authRepository.findByUserId(refreshToken.userId)

        if (tokensData?.lastUpdate != refreshToken.lastUpdate){
            return RequestResult.FailureResult(
                RequestError(
                    ExpiredRefreshToken,
                    "Expired refresh token"
                )
            )
        }

        return RequestResult.SuccessResult(
            tokens.createAccessToken(
                tokensData.userId
            )
        )
    }

    override suspend fun register(
        data: RegistrationData
    ): RequestResult<AuthenticationTokens> {
        if (authRepository.findByLogin(data.email) != null) {
            return RequestResult.FailureResult(
                RequestError(
                    LoginExistsError,
                    "A user with this username already exists"
                )
            )
        }

        val createUsersResult = users.createNewUser(data)

        if (createUsersResult is RequestResult.FailureResult)
            return createUsersResult

        val success = createUsersResult as RequestResult.SuccessResult

        val user = success.data;
        val lastUpdate = getCurrentUnixUtc0TimeSeconds()

        val identificationData = IdentificationData(
            user.id,
            data.email,
            passwordHasher.hashPassword(data.password),
            lastUpdate,
        )

        val addUserAuthDataResult = Result.runCatching {
            authRepository.add(identificationData)
        }

        if (addUserAuthDataResult.isFailure){
            return RequestResult.FailureResult(
                RequestError(
                    AuthorizationServiceRegistrationError,
                    "Unknown registration error is authorization service"
                )
            )
        }

        return RequestResult.SuccessResult(
            tokens.create(
                AuthenticationTokensData(user.id, lastUpdate)
            )
        )
    }

    override suspend fun validate(token: RefreshToken): RequestResult<RefreshTokenData> {
        val result = tokens.validate(token)

        if (result.isFailure){
            return RequestResult.FailureResult(
                RequestError(
                    InvalidRefreshToken,
                    "Invalid refresh token"
                )
            )
        }

        return  RequestResult.SuccessResult(
            result.getOrThrow()
        )

    }

}