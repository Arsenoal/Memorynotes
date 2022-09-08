package com.bumian.memorynotes.common

fun String.isDouble(): Boolean {
    return try {
        toDouble()
        true
    } catch (ex: Exception) {
        false
    }
}