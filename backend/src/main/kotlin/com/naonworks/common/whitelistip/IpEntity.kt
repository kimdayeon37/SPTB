package com.naonworks.common.whitelistip

import jakarta.persistence.*

@Entity
@Table(name = "whitelistip")
data class IpEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "ip")
    var ip: String = "",

    @Column(name = "active")
    var active: Boolean = false
)