package com.naonworks.module.modbus.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class MasterNetworkData(
    @field:NotBlank
    var protocol: String,

    @field:NotBlank
    @field:Size(max = 64)
    var ip: String,

    @field:NotBlank
//    @field:Min(0)
//    @field:Max(65535)
    var port: String,

    @field:NotNull
//    @field:Min(10)
//    @field:Max(100)
    var transactionDelay: String,

    @field:NotNull
//    @field:Min(1000)
//    @field:Max(3000)
    var timeout: String,

    @field:NotNull
//    @field:Min(1000)
//    @field:Max(3000)
    var msgCount: Int,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReadMessageData(
    @field:NotNull
    val time: Number,

    @field:NotBlank
    val name: String,

    @field:NotBlank
//    @field:Min(1)
//    @field:Max(3)
    val slaveId: String,

    @field:NotBlank
    val area: String,


    val byteSwap: Boolean?,


    val wordSwap: Boolean?,


//    @field:Min(0)
//    @field:Max(49999)
    val readAddress: String?,


//    @field:Min(1)
//    @field:Max(9999)
    val quantity: String?,


//    @field:Min(1)
//    @field:Max(1000)
    val scanTime: String?,
)

data class WriteMessageData(
    @field:NotNull
    val time: Number,

    @field:NotBlank
    val name: String,

    @field:NotBlank
    val slaveId: String,

    @field:NotBlank
    val type: String,

    val writeAddress: String?,

    val readAddress: String?,

    val readQuantity: String?,

    val values: List<Any>?,

    val invalidFunction: Boolean?,

    val invalidLength: Boolean?,

    val byteSwap: Boolean?,

    val wordSwap: Boolean?,

    val andMask: String?,

    val orMask: String?,

    val hexValue: String?
)

data class ReaderDataDto(
    val id: String,

    val networkData: MasterNetworkData,

    val msgData: List<ReadMessageData>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ReaderData(
    @Schema(description = "client ID")
    @field:NotNull
    var id: String,

    @Schema(description = "Network setting values")
    @field:NotNull
    @field:Valid
    @field:JsonFormat
    var networkData: MasterNetworkData,

    @Schema(description = "scenario message values")
    @field:NotNull
    @field:Valid
    @field:JsonFormat
    var msgData: List<ReadMessageData>
) {
    fun toDto() = ReaderDataDto(id, networkData, msgData)
}

data class WriterDataDto(
    val id: String,

    val networkData: MasterNetworkData,

    val msgData: List<WriteMessageData>
)

data class WriterData(
    @Schema(description = "client ID")
    @field:NotBlank
    val id: String,

    @Schema(description = "Network setting values")
    @field:NotNull
    val networkData: MasterNetworkData,

    @Schema(description = "scenario message values")
    @field:NotNull
    val msgData: List<WriteMessageData>
) {
    fun toDto() = WriterDataDto(id, networkData, msgData)
}