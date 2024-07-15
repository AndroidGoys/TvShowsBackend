package com.limelist.slices.shared

import kotlinx.serialization.Serializable

@Serializable
open class RequestError (
    val errorCode: ErrorCode,
    val errorMessage: String,
){
    @Serializable
    enum class ErrorCode {
        // При неверном логине / пароле
        InvalidLoginOrPassword,
        InvalidRefreshToken,
        // При ошибке добавления данных пользователя в сервис авторизации
        AuthorizationServiceRegistrationError,
        // Если при регистрации логин уже существует
        LoginExistsError
    }
}

