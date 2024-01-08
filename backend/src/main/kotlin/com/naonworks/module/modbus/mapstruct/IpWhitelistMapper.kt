package com.naonworks.module.modbus.mapstruct

import com.naonworks.entity.ips.tables.pojos.WhitelistipPojo
import com.naonworks.entity.ips.tables.records.WhitelistipRecord
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers
@Mapper
interface IpWhitelistMapper {
    companion object {
        var INSTANCE: IpWhitelistMapper = Mappers.getMapper(IpWhitelistMapper::class.java)
    }

    fun recordToPojo(record: WhitelistipRecord?): WhitelistipPojo?
}