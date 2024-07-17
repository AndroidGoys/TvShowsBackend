package com.limelist.slices.shared

import com.limelist.slices.shared.serialization.ErrorCodeSerializer
import kotlinx.serialization.Serializable

@Serializable
open class RequestError (
    val errorCode: ErrorCode,
    val errorMessage: String,
){
    @Serializable(with = ErrorCodeSerializer::class)
    class ErrorCode(val flag: Int) {
        companion object {
            // При неверном логине / пароле
            val InvalidLoginOrPassword = ErrorCode(1)
            val InvalidRefreshToken = ErrorCode(2)
            val ExpiredRefreshToken = ErrorCode(3)
            // При ошибке добавления данных пользователя в сервис авторизации
            val AuthorizationServiceRegistrationError = ErrorCode(4)
            // Если при регистрации логин уже существует
            val LoginExistsError = ErrorCode(5)
            val NotFound = ErrorCode(6)
        }
    }
}

