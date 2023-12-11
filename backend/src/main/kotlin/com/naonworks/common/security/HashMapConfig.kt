package com.naonworks.common.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


data class UserInfo(val ip: String, val userAgent: String)
@Configuration
class HashMapConfig {
    @Bean
    fun sharedHashMap(): HashMap<String, UserInfo> {
        return HashMap()
    }
}