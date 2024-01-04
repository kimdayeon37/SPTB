package com.naonworks.common.whitelistip

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface IpRepository : JpaRepository<IpEntity, Long> {
    fun findByIp(ip: String): IpEntity?
}