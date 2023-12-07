package com.naonworks.module.job

import jakarta.annotation.PreDestroy
import kotlinx.coroutines.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.support.CronExpression
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import kotlin.math.abs
import kotlin.time.toKotlinDuration

@Configuration(proxyBeanMethods = false)
class CustomJobInit(
        private val serverTimeJob: ServerTimeJob,
) : InitializingBean {
    private val log = org.slf4j.LoggerFactory.getLogger(this::class.java)

    private val jobHolder = mutableMapOf<CustomJob, CronExpression>()
    private val triggerHolder = mutableMapOf<CustomJob, LocalDateTime>()
    private var startJob: Job? = null

    override fun afterPropertiesSet() {
        setup()

        runBlocking { start() }
    }

    @PreDestroy
    fun destroy() {
        runBlocking {
            stop()
            clear()
        }
    }

    fun setup() {
        log.info("setup")

        val secExpression = getCronExpression("* * * * * *")

        //=================================================
        jobHolder[serverTimeJob] = secExpression
    }

    suspend fun clear() {
        log.warn("clear")

        jobHolder.clear()
    }

    suspend fun stop() {
        log.warn("stop")

        startJob?.cancelAndJoin()
        triggerHolder.clear()
    }

    suspend fun start() {
        log.info("start job")

        val nowDt = LocalDateTime.now()

        jobHolder.forEach {
            it.value.next(nowDt)?.let { next ->
                triggerHolder[it.key] = next
            }
        }

        startJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(Duration.ofMillis(100).toKotlinDuration())

                val jobDt = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS)

                triggerHolder.forEach {
                    if (it.value.isEqual(jobDt) || it.value.isBefore(jobDt)) {
                        jobHolder[it.key]?.next(jobDt)?.let { nextDt ->
                            launch {
                                val nextTimestamp = localToLong(nextDt)
                                val jobTimestamp = localToLong(jobDt)

                                if (nextTimestamp != null && jobTimestamp != null) {
                                    val limitMs = abs(nextTimestamp - jobTimestamp) - 100
                                    it.key.run(limitMs)
                                }
                            }

                            triggerHolder[it.key] = nextDt
                        }
                    }
                }
            }
        }
    }

    fun getCronExpression(expression: String): CronExpression {
        if (CronExpression.isValidExpression(expression))
            return CronExpression.parse(expression)

        throw RuntimeException("expression not valid : $expression")
    }

    fun localToLong(value: LocalDateTime?): Long? = value?.toInstant(OffsetDateTime.now(ZoneId.systemDefault()).offset)?.toEpochMilli()
}