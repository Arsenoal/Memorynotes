package com.bumian.memorynotes.repo.analytics

interface AnalyticsRepo {
    fun sendEvent(name: String)
}