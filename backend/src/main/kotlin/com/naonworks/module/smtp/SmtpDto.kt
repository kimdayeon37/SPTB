package com.naonworks.module.smtp

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

@JsonIgnoreProperties(ignoreUnknown = true)
data class SmtpMsg(
    @field:NotBlank
    var to: String,

    @field:NotBlank
    var subject: String,

    @field:NotBlank
    var text: String
)

data class SmtpConfig(
    @field:NotBlank
    var host: String,

    @field:NotNull
    var port: Number,

    @field:NotBlank
    var username: String,

    @field:NotBlank
    var password: String
)

data class MailRequest(

    var config: SmtpConfig,
    var msg: SmtpMsg
)