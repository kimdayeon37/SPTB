package com.naonworks.module.whitelistip
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.toList
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*


@Tag(name = "IP")
@RestController
@RequestMapping("/api")

class IpController @Autowired constructor(private val ipService: IpService) {
    @Operation(summary = "Get IPs")
    @GetMapping("/getIps")
    suspend fun getIps(): Map<String, Any> {
        val ipsWithStatus = ipService.getIps().toList()
        val activeStatus = ipsWithStatus.firstOrNull()?.active ?: false

        return mapOf(
            "active" to activeStatus,
            "result" to ipsWithStatus.map { it.ip }
        )
    }
    @Operation(summary = "Add IP")
    @PostMapping("/addIp")
    suspend fun addIp(@RequestBody request: Map<String, String>): Map<String, Boolean> {
        val ip = request["ip"] ?: error("Missing 'ip' field in the request")
        val success = ipService.addIp(UserIpDto(ip, active = false))
        return mapOf("result" to success)
    }

    @Operation(summary = "Delete IP")
    @PostMapping("/deleteIp")
    suspend fun deleteIp(@RequestBody request: Map<String, String>): Map<String, Boolean> {
        val ip = request["ip"] ?: error("Missing 'ip' field in the request")
        val success = ipService.deleteIp(ip)
        return mapOf("result" to success)
    }

    @Operation(summary = "Set IP Active")
    @PostMapping("/ipActive")
    suspend fun ipActive(@RequestBody request: Map<String, Boolean>): Map<String, Boolean> {
        val active = request["active"] ?: error("Missing 'active' field in the request")
        val success = ipService.ipActive(active)
        return mapOf("result" to success)
    }

//    @GetMapping("/checkIp")
//    fun checkIp(): Map<String, Boolean> {
//        return ipService.checkIp(request)
//    }
}