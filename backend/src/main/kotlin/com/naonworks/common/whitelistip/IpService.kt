package com.naonworks.common.whitelistip

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class IpService @Autowired constructor(private val ipRepository: IpRepository) {

    fun getIps(): Map<String, Any> {
        val ips = ipRepository.findAll()
        val ipList = ips.map { it.ip }
        return mapOf("active" to true, "result" to ipList)
    }

    fun addIp(ip: String): Map<String, Boolean> {
        val existingIpEntity = ipRepository.findByIp(ip)
        return if (existingIpEntity == null) {
            ipRepository.save(IpEntity(ip = ip))
            mapOf("result" to true)
        } else {
            mapOf("result" to false)
        }
    }

    fun deleteIp(ip: String): Map<String, Boolean> {
        val existingIpEntity = ipRepository.findByIp(ip)
        return if (existingIpEntity != null) {
            ipRepository.deleteById(existingIpEntity.id!!)
            mapOf("result" to true)
        } else {
            mapOf("result" to false)
        }
    }

    fun ipActiveAll(active: Boolean): Map<String, Boolean> {
        val allIps = ipRepository.findAll()
        allIps.forEach { it.active = active }
        ipRepository.saveAll(allIps)
        return mapOf("result" to true)
    }
}
