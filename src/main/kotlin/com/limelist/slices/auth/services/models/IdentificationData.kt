package com.limelist.slices.auth.services.models

import java.util.stream.IntStream.IntMapMultiConsumer

class IdentificationData(
    val userId: Int,
    val login: String,
    val hashedPassword: String,
    val refreshTokenId: String
)