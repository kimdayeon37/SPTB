package com.naonworks.module.smtp

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@Tag(name = "SMTP")
@RestController
@RequestMapping("/api")
class SmtpController(
    private val smtpService: SmtpService
) {

    @Operation(summary = "send smtp")
    @PostMapping("/smtp")
    suspend fun sendMail(@RequestBody mailRequest: MailRequest) {
        val to = mailRequest.msg.to
        val subject = mailRequest.msg.subject
        val text = mailRequest.msg.text

        smtpService.sendMail(mailRequest)
    }
}