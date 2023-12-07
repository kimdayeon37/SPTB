package com.naonworks.module.modbus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class ClientIdDto(
    var id: String
)

data class ClientId(
    @field:NotBlank
    var id: String
) {
    fun toDto() = ClientIdDto(id)
}