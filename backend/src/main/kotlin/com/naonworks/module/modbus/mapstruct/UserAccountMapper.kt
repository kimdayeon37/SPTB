package com.naonworks.module.modbus.mapstruct

import com.naonworks.entity.users.tables.pojos.AccountsPojo
import com.naonworks.entity.users.tables.records.AccountsRecord
import org.mapstruct.Mapper
import org.mapstruct.factory.Mappers

@Mapper
interface UserAccountMapper {
    companion object {
        var INSTANCE: UserAccountMapper = Mappers.getMapper(UserAccountMapper::class.java)
    }

    fun recordToPojo(record: AccountsRecord?): AccountsPojo?
}