package com.naonworks.module.modbus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull


@JsonIgnoreProperties(ignoreUnknown = true)
data class OUCNetworkData(
        @field:NotNull
        var endpointurl: String,

        @field:NotBlank
        var securitymode: String,

        @field:NotBlank
        var securitypolicy: String,

        @field:NotBlank
        var useridentify: String,

        @field:NotBlank
        var username: String,
        @field:NotBlank
        var password: String,

        @field:NotBlank
        var certFile: String,

        @field:NotBlank
        var keyFile: String,

        @field:NotBlank
        var applicationuri: String,

        @field:NotNull
        var msgCount: Int,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OUCMemoryData(
        var type: String,

        @field:NotBlank
        var nodeId: String,

        @field:NotNull
        var interval: Number,
        @field:NotNull
        var queueSize: Number,
        @field:NotBlank
        var discardOldest: String,
        )

data class ClientDataDto(
        @Schema(description = "client ID")
        var id: String,
        @Schema(description = "OPC-UA Client Network setting values")
        var oucNetworkData:OUCNetworkData,
        @Schema(description = "OPC-UA MSG values")
        var msgData:List<OUCMemoryData>
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ClientData(
        @Schema(description = "client ID")
        var id: String,
        @Schema(description = "OPC-UA Client Network setting values")
        var oucNetworkData:OUCNetworkData,
        @Schema(description = "OPC-UA MSG values")
        var msgData:List<OUCMemoryData>

){
    fun toDto() = ClientDataDto(id, oucNetworkData, msgData)
}
@JsonIgnoreProperties(ignoreUnknown = true)
data class MethodArgumentData(
        @field:NotBlank
        var dataType: String,

        @field:NotBlank
        var size: String?,
)
data class ReadWriteMessageData(
        @field:NotBlank
        val name: String,

        @field:NotBlank
        val type: String,

        @field:NotNull
        val nodeId: String,

        val invalidmsgtype: Boolean?,

        val invalidmsglength: Boolean?,

        val invalidmsgchunk: Boolean?,

        var inputArguments: List<MethodArgumentData>?,

        val rawBuffer: Number?
)

data class ReadWriterDataDto(
        val id: String,

        val networkData: OUCNetworkData,

        val msgData: List<ReadWriteMessageData>
)
data class ReadWriterData(
        @Schema(description = "client ID")
        @field:NotBlank
        val id: String,

        @Schema(description = "Network setting values")
        @field:NotNull
        val networkData: OUCNetworkData,

        @Schema(description = "scenario message values")
        @field:NotNull
        val msgData: List<ReadWriteMessageData>
) {
        fun toDto() = ReadWriterDataDto(id, networkData, msgData)
}