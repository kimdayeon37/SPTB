package com.naonworks.module.smtp

import org.slf4j.LoggerFactory
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.JavaMailSenderImpl
import org.springframework.stereotype.Service

@Service
class SmtpService {
    private val log = LoggerFactory.getLogger(this::class.java)
    private fun createMailSender(smtpConfig: SmtpConfig): JavaMailSender {
        val mailSender = JavaMailSenderImpl()
        mailSender.javaMailProperties["mail.smtp.auth"] = "true"
        mailSender.javaMailProperties["mail.smtp.ssl.enable"]="true"
        mailSender.javaMailProperties["mail.smtp.ssl.trust"]="smtp.naver.com"
        mailSender.host = smtpConfig.host
        mailSender.port = smtpConfig.port.toInt()
        mailSender.username = smtpConfig.username
        mailSender.password = smtpConfig.password

        return mailSender
    }

    fun sendMail(mailRequest: MailRequest) {
        log.info(mailRequest.config.username)
        val mailSender = createMailSender(mailRequest.config)
        val message = SimpleMailMessage()
        message.from = mailRequest.config.username
        message.setTo(mailRequest.msg.to)
        message.subject = mailRequest.msg.subject
        message.text = mailRequest.msg.text
        mailSender.send(message)
    }
}
