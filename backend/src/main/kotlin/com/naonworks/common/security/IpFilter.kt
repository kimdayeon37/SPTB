package com.naonworks.common.security

import com.naonworks.module.whitelistip.IpService
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono

@Component
class IpFilter(
    private val authenticationConverter: ServerAuthenticationConverter,
    private val ipService: IpService
) : WebFilter {

    private val securityContextRepository: ServerSecurityContextRepository = WebSessionServerSecurityContextRepository()

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        return extractIpFromRequest(exchange)
            .flatMap { ip ->
                isIpAllowed(ip)
                    .flatMap { allowed ->
                        if (allowed) {
                            authenticationConverter.convert(exchange)
                                .flatMap { authentication ->
                                    val securityContext = SecurityContextImpl(authentication)
                                    securityContextRepository.save(exchange, securityContext)
                                }
                        } else {
                            Mono.empty()
                        }
                    }
            }
            .then(chain.filter(exchange))
    }

    private fun extractIpFromRequest(exchange: ServerWebExchange): Mono<String> {
        return Mono.justOrEmpty(exchange.request.remoteAddress?.address?.hostAddress)
    }
    fun isIpAllowed(receivingIp: String): Mono<Boolean> {
        return mono {
            val isIpBlockActive = ipService.ipActive(true)
            println("Is IP Block Active: $isIpBlockActive")

            if (isIpBlockActive) {
                // If the IP block is active, check the whitelist
                val whitelistIps = ipService.getIps().map { it.ip }.toList()
                println("Whitelist IPs: $whitelistIps")

                val isAllowed = receivingIp in whitelistIps
                println("Is IP Allowed: $isAllowed")

                isAllowed
            } else {
                // If the IP block is not active, allow all IPs
                println("IP Block is not active. Allowing all IPs.")
                true
            }
        }.onErrorReturn(true) // Ensure that in case of any error, it returns true
    }

}