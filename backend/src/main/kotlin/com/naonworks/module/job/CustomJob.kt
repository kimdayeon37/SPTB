package com.naonworks.module.job

import kotlinx.coroutines.withTimeout

interface CustomJob {
    suspend fun run(limitMs: Long) {
        try {
            withTimeout(limitMs) {
                job()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun job()
}