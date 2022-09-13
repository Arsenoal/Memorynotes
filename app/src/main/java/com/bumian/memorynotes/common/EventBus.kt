package com.bumian.memorynotes.common

import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

object EventBus {

    private val collector by lazy {
        MutableStateFlow("")
    }


    @DelicateCoroutinesApi
    fun pushEvent(name: String) {
        GlobalScope.launch {
            collector.emit(name)
        }
    }

    fun collect(name: String): Flow<String> {
        return collector.filter { it == name }
    }

}