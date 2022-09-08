package com.bumian.memorynotes.repo.analytics

import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase

class FirebaseAnalytics(
    private val analytics: FirebaseAnalytics = Firebase.analytics
): AnalyticsRepo {

    override fun sendEvent(name: String) {
        analytics.logEvent(name, null)
    }
}