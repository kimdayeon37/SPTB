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
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    override fun convert(exchange: ServerWebExchange?): Mono<Authentication> = mono {
        exchange ?: return@mono null

        var bearerToken = exchange.request.headers.getFirst(HttpHeaders.AUTHORIZATION)
            ?: exchange.request.queryParams.getFirst("authorization")
        if (bearerToken == null) return@mono null
        if (!bearerToken.startsWith("Bearer ")) {

            bearerToken = exchange.request.queryParams.getFirst("authorization") ?: return@mono null
        }
        val ipAddress = exchange.request.remoteAddress?.address?.hostAddress
        val userAgent = exchange.request.headers.getFirst(HttpHeaders.USER_AGENT)

        log.info("Client IP Address: $ipAddress")
        log.info("User-Agent: $userAgent")

        // ip랑 useragent 가져와서 토큰에 저장
        val token = bearerToken.substring(7)

        BearerToken(token, ipAddress, userAgent)
    }
}

@Component
class JwtAuthenticationManager(
    private val jwtSupport: JwtSupport,
    private val users: ReactiveUserDetailsService,
    private val hashMapService: HashMapService,
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication?): Mono<Authentication> = mono {
        authentication ?: return@mono null

        if (authentication !is BearerToken)
            return@mono null

        validate(authentication)
        // ip 랑 useragent 로그인 시점 만들어진것과 비교
        val username = jwtSupport.getId(authentication)
        val userInfo = hashMapService.getInfo(username)
        userInfo?.let {
            if (userInfo.ip != authentication.ipAddress || userInfo.userAgent != authentication.userAgent) return@mono null
        } ?: run { return@mono null }
        users.findByUsername(username).awaitSingleOrNull()?.let {
            UsernamePasswordAuthenticationToken(it, it.password, it.authorities)
        }
    }

    // 토큰 검증
    private suspend fun validate(token: BearerToken) {
        if (jwtSupport.isExpired(token))
            throw object : AuthenticationException("Token is not valid") {}
    }
}