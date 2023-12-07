package com.naonworks.module.modbus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class SlaveNetworkData(
    @field:NotBlank
    var protocol: String,

    @field:NotBlank
    var port: String,

    @field:NotNull
    var maxSessionCount: String,

    @field:NotNull
    var slaveId: String,

    @field:NotNull
    var waitTimeout: String,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SlaveMessageData(
    @field:NotBlank
    var coils: String,

    @field:NotBlank
    var distreteInputs: String,

    @field:NotBlank
    var inputRegisters: String,

    @field:NotBlank
    var holdingRegisters: String,

    @field:NotNull
    var byteSwap: Boolean,

    @field:NotNull
    var wordSwap: Boolean,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SlaveDataDto(
    var id: String,

    var networkData: SlaveNetworkData,

    var msgData: SlaveMessageData
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SlaveData(
    @Schema(description = "client ID")
    @field:NotNull
    var id: String,

    @Schema(description = "slave network data")
    @field:NotNull
    var networkData: SlaveNetworkData,

    @Schema(description = "slave memory data")
    @field:NotNull
    var msgData: SlaveMessageData
) {
    fun toDto() = SlaveDataDto(id, networkData, msgData)
}