package com.naonworks.module.whitelistip

import com.naonworks.common.config.jooq.JooqQuery
import com.naonworks.entity.ips.tables.WhitelistipTable.Whitelistip
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.toList
import org.jooq.DSLContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.web.util.matcher.IpAddressMatcher
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class IpService @Autowired constructor(
    private val ctx: DSLContext,
    ) {
    suspend fun getIps(): Flow<UserIpDto> {
        val table = Whitelistip

        val query = ctx.selectFrom(table)

        return JooqQuery.findAllFlow(query).mapNotNull { record ->
            UserIpDto(
                ip = record.get(table.ip),
                active = record.get(table.active)
            )
        }
    }
    suspend fun addIp(ipDto: UserIpDto): Boolean {
        val table = Whitelistip

        val query = ctx.insertInto(table)
            .set(table.ip, ipDto.ip)
            .set(table.active, ipDto.active)

        val result = JooqQuery.execute(query)
        return result.isNotEmpty() && result[0] == 1
    }
    suspend fun deleteIp(ip: String): Boolean {
        val table = Whitelistip

        val query = ctx.deleteFrom(table)
            .where(table.ip.eq(ip))

        val result = JooqQuery.execute(query)
        return result.isNotEmpty() && result[0] == 1
    }

    suspend fun ipActive(active: Boolean): Boolean {
        val table = Whitelistip

        val query = ctx.update(table)
            .set(table.active, active)

        val result = JooqQuery.execute(query)
        return result.isNotEmpty() && result[0] > 0
    }
    suspend fun getIpActiveResult(active: Boolean): Boolean {
        return ipActive(active)
    }

}