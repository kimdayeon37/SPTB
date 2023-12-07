package com.naonworks.common.security

import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

// request 에서 헤더에서 토큰 추출
@Component
class JwtServerAuthenticationConverter : ServerAuthenticationConverter {
//    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> {
//        return Mono.justOrEmpty(exchange?.request?.headers?.getFirst(HttpHeaders.AUTHORIZATION))
//            .filter { it.startsWith("Bearer ") }.map { it.substring((7)) }.map { jwt -> BearerToken(jwt) }
//    }

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> = mono {
        exchange ?: return@mono null

        val bearerToken = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return@mono null

        if (!bearerToken.startsWith("Bearer "))
            return@mono null
        // ip랑 useragent 가져와서 토큰에 저장
        val token = bearerToken.substring(7)

        BearerToken(token)
    }
}

@Component
class JwtAuthenticationManager(
    private val jwtSupport: JwtSupport,
//    private val users: UserDetailsService,
    private val users: ReactiveUserDetailsService,
) : ReactiveAuthenticationManager {

    // 인증
//    override fun authenticate(authentication: Authentication?): Mono<Authentication> {
//        return Mono.justOrEmpty(authentication)
//            .filter { auth -> auth is BearerToken }
//            .cast(BearerToken::class.java)
//            .flatMap { jwt -> mono { validate(jwt) } }
//            .onErrorMap { error -> InvalidBearerToken(error.message) }
//    }

    override fun authenticate(authentication: Authentication?): Mono<Authentication> = mono {
        authentication ?: return@mono null

        if (authentication !is BearerToken)
            return@mono null

        validate(authentication)
        // ip 랑 ㅕuser agent 로그인 시점 만들어진것과 비교
        val username = jwtSupport.getId(authentication)

        users.findByUsername(username).awaitSingleOrNull()?.let {
            UsernamePasswordAuthenticationToken(it, it.password, it.authorities)
        }
    }

    // 토큰 검증
//    private suspend fun validate(token: BearerToken): Authentication {
//        val username = jwtSupport.getId(token)
////        val user = users.findByUsername(username).awaitSingleOrNull()
//        val user = users.loadUserByUsername(username)
//        if (jwtSupport.isValid(token, user)) {
//            return UsernamePasswordAuthenticationToken(user!!.username, user.password, user.authorities)
//        }
//
//        throw IllegalArgumentException("Token is not valid")
//    }

    private suspend fun validate(token: BearerToken) {
        if (jwtSupport.isExpired(token))
            throw object : AuthenticationException("Token is not valid") {}
    }
}

//class InvalidBearerToken(message: String?) : AuthenticationException(message)