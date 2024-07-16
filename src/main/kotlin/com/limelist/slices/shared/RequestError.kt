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
            val InvalidLoginOrPassword = ErrorCode(0b100)
            val InvalidRefreshToken = ErrorCode(0b101)
            val ExpiredRefreshToken = ErrorCode(0b110)
            // При ошибке добавления данных пользователя в сервис авторизации
            val AuthorizationServiceRegistrationError = ErrorCode(0b1000)
            // Если при регистрации логин уже существует
            val LoginExistsError = ErrorCode(0b1001)
        }
    }
}

