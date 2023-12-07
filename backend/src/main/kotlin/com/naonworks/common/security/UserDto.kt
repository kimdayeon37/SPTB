package com.naonworks.common.security

import jakarta.validation.constraints.NotBlank

data class UserIdPwDto(
    var id: String,

    var pw: String,

    )

data class UserIdPw(
    @field:NotBlank
    var id: String,

    @field:NotBlank
    var pw: String,

    ) {
    fun toDto() = UserIdPwDto(id, pw)
}

data class OnlyId(
    @field:NotBlank
    var id: String
)