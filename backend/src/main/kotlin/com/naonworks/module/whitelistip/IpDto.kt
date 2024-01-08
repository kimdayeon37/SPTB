package com.naonworks.module.whitelistip

import jakarta.validation.constraints.NotBlank

data class UserIpDto(
    var ip: String,

    var active: Boolean,

    )

data class UserIp(
    @field:NotBlank
    var ip: String,

    @field:NotBlank
    var active: Boolean
) {
    fun toDto() = UserIpDto(ip, active)
}
data class Onlyid(
    @field:NotBlank
    var id: String
)