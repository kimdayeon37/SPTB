package com.naonworks.common.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    @Bean
    fun passwordEncoder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(
            http: ServerHttpSecurity,
            converter: JwtServerAuthenticationConverter,
            authManager: JwtAuthenticationManager
    ): SecurityWebFilterChain? {
        val filter = AuthenticationWebFilter(authManager)
        filter.setServerAuthenticationConverter(converter)
        http
                // 규칙을 설정합니다. 예를 들어, 인증, 권한, 경로별 규칙 등을 정의할 수 있습니다.
                .authorizeExchange { exchanges ->
                    exchanges
                            .pathMatchers(HttpMethod.POST,"/api/login")
                            .permitAll() // 모든 사용자에게 허용
                            .pathMatchers(HttpMethod.POST,"/api/signup")
                            .permitAll() // 모든 사용자에게 허용
                            .pathMatchers(HttpMethod.POST,"/api/deleteUser")
                            .permitAll() // 모든 사용자에게 허용
                            .pathMatchers(HttpMethod.GET,"/api/getAllUsers")
                            .permitAll()
                            .pathMatchers(HttpMethod.GET,"/api/test")
                            .authenticated()
                            .pathMatchers(HttpMethod.POST,"/api/regenToken")
                            .authenticated()
                            // .pathMatchers("/api/**").authenticated() // USER
                            // 권한이 있는 사용자만 허용
                            .pathMatchers("/api/sse/**")
                            .authenticated()
                            .anyExchange()
                            .permitAll()
                }
                .addFilterAt(filter, SecurityWebFiltersOrder.AUTHENTICATION)
                .csrf { it.disable() }
                .formLogin { formLogin ->
                    formLogin.disable()
//                     formLogin.loginPage("/Login")
                }
                .httpBasic { it.disable() } // HTTP 기본 인증 사용 X

        return http.build()
    }
}
