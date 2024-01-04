package com.naonworks.common.whitelistip

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")

class IpController @Autowired constructor(private val ipService: IpService) {

    @GetMapping("/getIps")
    fun getIps(): Map<String, Any> {
        return ipService.getIps()
    }

    @PostMapping("/addIp")
    fun addIp(@RequestBody request: Map<String, String>): Map<String, Boolean> {
        val ip = request["ip"] ?: ""
        return ipService.addIp(ip)
    }

    @DeleteMapping("/deleteIp")
    fun deleteIp(@RequestBody request: Map<String, String>): Map<String, Boolean> {
        val ip = request["ip"] ?: ""
        return ipService.deleteIp(ip)
    }


    @PostMapping("/ipActive")
    fun ipActiveAll(@RequestBody request: Map<String, Boolean>): Map<String, Boolean> {
        val active = request["active"] ?: false
        return ipService.ipActiveAll(active)
    }

}