package com.naonworks.module.modbus

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.ObjectMapper
import com.naonworks.common.config.jooq.JooqQuery
import com.naonworks.common.config.rest.RestError
import com.naonworks.common.config.rest.RestErrorException
import com.naonworks.common.security.AccountService
import com.naonworks.common.security.JwtSupport
import com.naonworks.common.security.OnlyId
import com.naonworks.common.security.UserIdPw
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

@Tag(name = "MODBUS")
@RestController
@RequestMapping(path = ["/api"])
class ModbusController(
    private val mqttService: MqttService,
    private val ctx: DSLContext,
    private val validator: SmartValidator,
    private val accountService: AccountService,
    private val encoder: PasswordEncoder,
//    private val users: UserDetailsService,
    private val jwtSupport: JwtSupport
) {
    private val log = LoggerFactory.getLogger(this::class.java)
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

    @Operation(summary = "auth test")
    @GetMapping(
        path = ["/test"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    suspend fun test(
        @AuthenticationPrincipal principal: Principal,
        exchange: ServerWebExchange,
    ): ResponseEntity<String> {
        return ResponseEntity.ok(principal.name + "인증 성공")
    }

    @Operation(summary = "log in")
    @PostMapping(
        path = ["/login"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun login(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: UserIdPw,
    ): ResponseEntity<Jwt> {
////        val user = users.findByUsername(body.id).awaitSingleOrNull()
//        val user = users.loadUserByUsername(body.id)
//        user?.let {
//            if (encoder.matches(body.pw, it.password)) {
//                return ResponseEntity.ok(Jwt(jwtSupport.generate(it.username).value))
//            }
//        }
//        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
//        // 토큰 발행
////        val tokenInfo = accountService.login(body.id, body.pw)
////        return ResponseEntity.ok(tokenInfo.toString())

        // todo: UserDetailsService 는 webflux 에서 사용 x
        val pojo = accountService.findUserByUsername(body.id)
        // ip랑 브라우저 정보 저장 해쉬맵
        if (pojo != null && encoder.matches(body.pw, pojo.password))
            return ResponseEntity.ok(Jwt(jwtSupport.generate(pojo.username).value))

        throw BadCredentialsException("Invalid Credentials")
    }


    @Operation(summary = "sign up")
    @PostMapping(
        path = ["/signup"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun signup(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: UserIdPw,
    ): ResponseEntity<String> {
        // 동일 id 체크 후 db에 등록

        val success = accountService.signup(body.id, body.pw)

        return if (success)
            ResponseEntity.ok("생성 성공")
        else
            ResponseEntity.badRequest().body("동일한 ID가 존재합니다.")
    }

    @Operation(summary = "delete user")
    @PostMapping(
        path = ["/deleteUser"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun deleteUser(
        exchange: ServerWebExchange,

        @JsonFormat
        @RequestBody
        body: OnlyId,
    ): ResponseEntity<String> {
        // 동일 id 체크 후 db에 등록

        val success = accountService.deleteUser(body.id)

        return if (success)
            ResponseEntity.ok("삭제 성공")
        else
            ResponseEntity.badRequest().body("해당 ID는 존재하지 않습니다.")
    }

    @Operation(summary = "regenerate token")
    @PostMapping(
        path = ["/regenToken"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun regenToken(
        exchange: ServerWebExchange,

//        test : Principal,
//        userDetails: UserDetails?
        @JsonFormat
        @RequestBody
        body: OnlyId,
    ): ResponseEntity<Jwt> {
////        val user = users.findByUsername(body.id).awaitSingleOrNull()
//        val user = users.loadUserByUsername(body.id)
//        user?.let {
//            return ResponseEntity.ok(Jwt(jwtSupport.generate(it.username).value))
//
//        }
//        throw ResponseStatusException(HttpStatus.UNAUTHORIZED)
//        // 토큰 발행
////        val tokenInfo = accountService.login(body.id, body.pw)
////        return ResponseEntity.ok(tokenInfo.toString())

        // todo: authorizeExchange 에서 인증된 사용자만 controller 올수 있음으로 body 받을 필요 없음
//        if (userDetails == null)
//            return ResponseEntity.badRequest().build()
//
//        return ResponseEntity.ok(Jwt(jwtSupport.generate(userDetails.username).value))
        return ResponseEntity.ok(Jwt(jwtSupport.generate(body.id).value))
    }

    @Operation(summary = "delete user")
    @GetMapping(
        path = ["/getAllUsers"],
        produces = [MediaType.APPLICATION_JSON_VALUE]
    )
    suspend fun getAllUsers(
        exchange: ServerWebExchange,
    ): ResponseEntity<Flow<AccountsPojo>> {
        // 동일 id 체크 후 db에 등록

        val res = accountService.getAllUsers()

        return ResponseEntity.ok(res);
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