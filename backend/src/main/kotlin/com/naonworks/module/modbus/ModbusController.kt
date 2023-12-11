package com.naonworks.module.modbus

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.naonworks.common.config.jooq.JooqQuery
import com.naonworks.common.config.rest.RestError
import com.naonworks.common.config.rest.RestErrorException
import com.naonworks.common.security.*
import com.naonworks.entity.log.tables.ServerLogTable
import com.naonworks.entity.log.tables.pojos.ServerLogPojo
import com.naonworks.entity.users.tables.pojos.AccountsPojo
import com.naonworks.module.modbus.dto.*
import com.naonworks.module.modbus.mapstruct.ServerLogMapper
import com.naonworks.module.mqtt.MqttService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.jooq.DSLContext
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerSentEvent
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.SmartValidator
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.security.Principal
import java.util.*
import kotlin.random.Random

@Tag(name = "MODBUS, OPCUA")
@RestController
@RequestMapping(path = ["/api"])
class ModbusController(
    private val mqttService: MqttService,
    private val ctx: DSLContext,
    private val validator: SmartValidator,

    ) {

    private val mapper = ServerLogMapper.INSTANCE
    private val objectMapper = ObjectMapper()

    data class Jwt(val token: String)
    data class LogMessage(var id: String, var time: String, var type: String, var content: String) {
        // 기본 생성자
        constructor() : this("", "", "", "")
    }

    fun extractClientIdFromMessage(message: MqttMessage): String? {
        val messageJson = String(message.payload)
        val objectMapper = ObjectMapper()
        val jsonObject = objectMapper.readValue(messageJson, LogMessage::class.java)
        return jsonObject.id
    }


    @Operation(summary = "sse system subscribe")
    @GetMapping(path = ["/sse/system"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun systemSubscribe(
        @RequestParam("clientId") clientId: String?,  // 클라이언트가 보낸 clientId
        exchange: ServerWebExchange,
    ): Flow<ServerSentEvent<String>> =
        mqttService.subscribe("/log/system").filter { message ->
            val messageClientId = extractClientIdFromMessage(message)
            messageClientId == clientId
        }.map { message ->
            ServerSentEvent.builder<String>().data(String(message.payload)).build()
        }


    @Operation(summary = "sse trans subscribe")
    @GetMapping(path = ["/sse/trans"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun transSubscribe(
        @RequestParam("clientId") clientId: String?,  // 클라이언트가 보낸 clientId
        exchange: ServerWebExchange,
    ): Flow<ServerSentEvent<String>> =

        mqttService.subscribe("/log/trans").filter { message ->
            val messageClientId = extractClientIdFromMessage(message)
            messageClientId == clientId
        }.map { message ->
            ServerSentEvent.builder<String>().data(String(message.payload)).build()
        }


    @Operation(summary = "get client id")
    @GetMapping(path = ["/getClientId"], produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun clientId(
        exchange: ServerWebExchange,
    ): ResponseEntity<String> {
        val byteArr = ByteArray(16)

        Random.nextBytes(byteArr)

        val body = HexFormat.of().formatHex(byteArr).replace("\\", "")

        return ResponseEntity.ok(body)
    }

    @Operation(summary = "publish id")
    @PostMapping(
        path = ["/id"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postId(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: ClientId,
    ): ResponseEntity<String> {
        mqttService.publish("/id", objectMapper.writeValueAsBytes(body.toDto()))

        return ResponseEntity.ok("Express")
    }


    @Operation(summary = "publish exit")
    @PostMapping(
        path = ["/exit"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postExit(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: ClientId,
    ): ResponseEntity<String> {
        mqttService.publish("/modbus/exit", objectMapper.writeValueAsBytes(body.toDto()))

        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish reader")
    @PostMapping(
        path = ["/reader"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postReader(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: ReaderData,
    ): ResponseEntity<String> {
        val e = BeanPropertyBindingResult(body, "")
        validator.validate(body, e)

        if (!e.hasErrors())
            mqttService.publish("/modbus/master/read", objectMapper.writeValueAsBytes(body.toDto()))

        if (e.hasErrors())
            throw RestErrorException(RestError(e))

        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish writer")
    @PostMapping(
        path = ["/writer"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postWriter(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: WriterData,
    ): ResponseEntity<String> {
        val e = BeanPropertyBindingResult(body, "")
        validator.validate(body, e)

        if (!e.hasErrors())
            mqttService.publish("/modbus/master/write", objectMapper.writeValueAsBytes(body.toDto()))
        if (e.hasErrors())
            throw RestErrorException(RestError(e))
        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish MES start")
    @PostMapping(
        path = ["/MSE/start"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postMESStart(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: SlaveData,
    ): ResponseEntity<String> {

        val e = BeanPropertyBindingResult(body, "")
        validator.validate(body, e)

        if (!e.hasErrors())
            mqttService.publish("/modbus/slave", objectMapper.writeValueAsBytes(body.toDto()))
        if (e.hasErrors())
            throw RestErrorException(RestError(e))
        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish OPC-UA Server start")
    @PostMapping(
        path = ["/opcua/server"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postOUSStart(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: ServerData,
    ): ResponseEntity<String> {

        val e = BeanPropertyBindingResult(body, "")
        validator.validate(body, e)

        if (!e.hasErrors())
            mqttService.publish("/opcua/server", objectMapper.writeValueAsBytes(body.toDto()))
        if (e.hasErrors())
            throw RestErrorException(RestError(e))
        return ResponseEntity.ok("Express")
    }


    @Operation(summary = "publish OPC-UA Client start")
    @PostMapping(
        path = ["/opcua/client"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postOUCStart(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: ClientData,
    ): ResponseEntity<String> {

        val e = BeanPropertyBindingResult(body, "")
        validator.validate(body, e)

        if (!e.hasErrors())
            mqttService.publish("/opcua/client", objectMapper.writeValueAsBytes(body.toDto()))
        if (e.hasErrors())
            throw RestErrorException(RestError(e))
        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "publish OPC-UA Client read writer")
    @PostMapping(
        path = ["/opcua/readwriter"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun postReadWriter(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: ReadWriterData,
    ): ResponseEntity<String> {
        val e = BeanPropertyBindingResult(body, "")
        validator.validate(body, e)

        if (!e.hasErrors())
            mqttService.publish("/opcua/client/readwrite", objectMapper.writeValueAsBytes(body.toDto()))
        if (e.hasErrors())
            throw RestErrorException(RestError(e))
        return ResponseEntity.ok("Express")
    }

    @Operation(summary = "로그?")
    @GetMapping(path = ["/log"], produces = [MediaType.APPLICATION_JSON_VALUE])
    suspend fun getLog(
        exchange: ServerWebExchange,
    ): ResponseEntity<Flow<ServerLogPojo>> {
        val table = ServerLogTable.Server_Log

        val query = ctx.selectFrom(table)

        val list = JooqQuery.findAllFlow(query).mapNotNull { mapper.recordToPojo(it) }

        return ResponseEntity.ok(list)
    }

}