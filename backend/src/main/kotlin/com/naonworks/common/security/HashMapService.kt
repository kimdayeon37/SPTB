package com.naonworks.common.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class HashMapService  @Autowired constructor(private val sharedHashMap: HashMap<String, UserInfo>) {
    fun addInfo(key: String, ip: String, userAgent: String) {
        val userInfo = UserInfo(ip, userAgent)
        sharedHashMap[key] = userInfo
    }

    fun getInfo(key: String): UserInfo? {
        return sharedHashMap[key]
    }
}