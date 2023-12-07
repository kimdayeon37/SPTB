package com.naonworks.module.etc

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Sinks

@Tag(name = "SSE - MAIN")
@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/sse/main")
class SSEMainController {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    companion object {
        val serverTimeSinks = Sinks.many().replay().latest<Long>()
    }

    @Operation(summary = "server time")
    @GetMapping(path = ["/server-time"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    suspend fun serverTime(): Flow<Long> {
        return serverTimeSinks.asFlux().asFlow()
    }
}