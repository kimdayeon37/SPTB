package com.naonworks.module.user

import com.fasterxml.jackson.annotation.JsonFormat
import com.naonworks.common.security.*
import com.naonworks.entity.users.tables.pojos.AccountsPojo
import com.naonworks.module.modbus.ModbusController
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ServerWebExchange
import java.security.Principal

@Tag(name = "USER")
@RestController
@RequestMapping(path = ["/api"])
class UserController(
    private val accountService: AccountService,
    private val encoder: PasswordEncoder,
    private val hashMapService: HashMapService,
    private val jwtSupport: JwtSupport
) {
    private val log = LoggerFactory.getLogger(this::class.java)

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
    ): ResponseEntity<ModbusController.Jwt> {
        // todo: UserDetailsService 는 webflux 에서 사용 x
        val pojo = accountService.findUserByUsername(body.id)
        // ip랑 브라우저 정보 저장 해쉬맵
        if (pojo != null && encoder.matches(body.pw, pojo.password)) {
            val ipAddress = exchange.request.remoteAddress?.address?.hostAddress
            val userAgent = exchange.request.headers.getFirst(HttpHeaders.USER_AGENT)
            hashMapService.addInfo(body.id, ipAddress.toString(), userAgent.toString())
            log.info("[로그인 정보] ipAddress : $ipAddress userAgent : $userAgent")
            return ResponseEntity.ok(ModbusController.Jwt(jwtSupport.generate(pojo.username).value))
        }

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
    ): ResponseEntity<ModbusController.Jwt> {
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
        return ResponseEntity.ok(ModbusController.Jwt(jwtSupport.generate(body.id).value))
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

}