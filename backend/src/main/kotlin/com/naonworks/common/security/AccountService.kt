package com.naonworks.common.security

import com.naonworks.common.config.jooq.JooqQuery
import com.naonworks.entity.users.tables.AccountsTable
import com.naonworks.entity.users.tables.pojos.AccountsPojo
import com.naonworks.module.modbus.mapstruct.UserAccountMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.reactor.mono
import org.jooq.DSLContext

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import java.util.stream.Collectors


@Service
class AccountService(
    private val ctx: DSLContext,
    private val passwordEncoder: PasswordEncoder = BCryptPasswordEncoder()
)
//    :UserDetailsService
    : ReactiveUserDetailsService {
    private val mapper = UserAccountMapper.INSTANCE


    // 해당 id가 존재한다면 true 없다면 false
    suspend fun findUser(id: String): Boolean {
        val table = AccountsTable.Accounts

        val query = ctx.select(table.Username)
            .from(table)
            .where(table.Username.eq(id))
        val result = JooqQuery.findOne(query)
        return result != null
    }

    suspend fun signup(id: String, pw: String): Boolean {
        if (findUser(id)) return false
        val table = AccountsTable.Accounts // 여기에 테이블 이름을 정확하게 대입해야 합니다.
        val query = ctx.insertInto(table)
            .set(table.Username, id)
            .set(table.Password, passwordEncoder.encode(pw))
            .set(table.Role, "USER")

        val result = JooqQuery.execute(query)
        return result.isNotEmpty() && result[0] == 1
    }

    suspend fun deleteUser(id: String): Boolean {
        val table = AccountsTable.Accounts

        val query = ctx.deleteFrom(table)
            .where(table.Username.eq(id))

        val result = JooqQuery.execute(query)
        return result.isNotEmpty() && result[0] == 1
    }

    suspend fun getAllUsers(): Flow<AccountsPojo> {
        val table = AccountsTable.Accounts

        val query = ctx.selectFrom(table)

        return JooqQuery.findAllFlow(query).mapNotNull { mapper.recordToPojo(it) }
    }

    suspend fun findUserByUsername(username: String): AccountsPojo? {
        val table = AccountsTable.Accounts

        val query = ctx.selectFrom(table)
            .where(table.Username.eq(username))
            .limit(1)

        return JooqQuery.findOne(query)?.let { mapper.recordToPojo(it) }
    }

    private fun getAuthorities(roles: List<String>): List<GrantedAuthority> {
        return roles.stream()
            .map { role: String -> SimpleGrantedAuthority("ROLE_$role") }
            .collect(Collectors.toList())
    }


    override fun findByUsername(username: String?): Mono<UserDetails> = mono {
        username ?: throw UsernameNotFoundException("Username is empty or null")

        val user =
            findUserByUsername(username) ?: throw UsernameNotFoundException("User not found with username: $username")
        val authorities = listOf(SimpleGrantedAuthority(user.role))

        User.builder()
            .username(user.username)
            .password(user.password)
            .authorities(authorities)
            .build()
    }
}