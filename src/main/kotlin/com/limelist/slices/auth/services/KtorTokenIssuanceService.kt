package com.limelist.slices.auth.services

import com.limelist.slices.auth.AuthConfig
import com.limelist.slices.auth.dataAccess.interfaces.AuthRepository
import com.limelist.slices.auth.services.models.*
import com.limelist.slices.users.services.UserCreationInternalService
import com.limelist.slices.users.services.models.RegistrationData
import com.limelist.slices.shared.RequestError
import com.limelist.slices.shared.RequestResult
import com.limelist.slices.shared.RequestError.ErrorCode.*

import java.util.UUID.randomUUID

class KtorTokenIssuanceService(
    val users: UserCreationInternalService,
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
            return RequestResult.ErrorResult(
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
            return RequestResult.ErrorResult(
                RequestError(
                    InvalidLoginOrPassword,
                    "Invalid login or password"
                )
            )
        }

        return RequestResult.SuccessResult(
            tokens.create(
                AuthenticationTokensData(
                    fromRepository.hashedPassword,
                    fromRepository.userId
                )
            )
        )
    }

    override suspend fun refresh(
        refreshToken: RefreshTokenData
    ): RequestResult<AccessToken> {
        val tokensData = authRepository.findByRefreshToken(refreshToken)
            ?: return RequestResult.ErrorResult(
                RequestError(
                    InvalidRefreshToken,
                    "Invalid refresh token"
                )
            )

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

        val identificationData = IdentificationData(
            user.id,
            data.email,
            passwordHasher.hashPassword(data.password),
            refreshToken,
        )

        val addUserAuthDataResult = Result.runCatching {
            authRepository.add(identificationData)
        }

        if (addUserAuthDataResult.isFailure){
            return RequestResult.ErrorResult(
                RequestError(
                    AuthorizationServiceRegistrationError,
                    "Unknown registration error is authorization service"
                )
            )
        }

        return RequestResult.SuccessResult(
            tokens.create(
                AuthenticationTokensData(refreshToken, user.id)
            )
        )

    }

}