package com.naonworks.module.modbus.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class ArgumentData(
    @field:NotBlank
    var name: String,

    @field:NotBlank
    var dataType: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class usreData(
    @field:NotBlank
    var id: String,
    @field:NotBlank
    var pw: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OUSNetworkData(
    @field:NotBlank
    var ip: String,

    @field:NotNull
    var port: Number,

    @field:NotBlank
    var certFile: String,

    @field:NotBlank
    var keyFile: String,

    @field:NotNull
    var users: List<usreData>,

    @field:NotNull
    var nodeCount: Number,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OUSMemoryNodeData(
    @field:NotBlank
    var id: String,

    @field:NotBlank
    var label: String,
    @field:NotBlank
    var category: String,
    @field:NotBlank
    var type: String,
    @field:NotBlank
    var accessRight: String,
    var inputArguments: List<ArgumentData>?,
    var outputArguments: List<ArgumentData>?,
    var children: List<OUSMemoryNodeData>?,
    var route: String?
)

data class ServerDataDto(
    @Schema(description = "client ID")
    var id: String,
    @Schema(description = "OPC-UA Network setting values")
    var ousNetworkData:OUSNetworkData,
    @Schema(description = "OPC-UA Node Tree values")
    var ousMemoryTreeData:List<OUSMemoryNodeData>
)
@JsonIgnoreProperties(ignoreUnknown = true)
data class ServerData(
    @Schema(description = "client ID")
    @field:NotBlank
    var id: String,
    @Schema(description = "OPC-UA Network setting values")
    @field:NotNull
    var ousNetworkData:OUSNetworkData,
    @Schema(description = "OPC-UA Node Tree values")
    @field:NotNull
    var ousMemoryTreeData:List<OUSMemoryNodeData>
){
    fun toDto() = ServerDataDto(id, ousNetworkData, ousMemoryTreeData)
}