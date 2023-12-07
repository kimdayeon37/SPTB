package com.naonworks.module.job

import com.naonworks.module.etc.SSEMainController
import org.springframework.stereotype.Component

@Component
class ServerTimeJob : CustomJob {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    override suspend fun job() {
        SSEMainController.serverTimeSinks.tryEmitNext(System.currentTimeMillis())
    }
}