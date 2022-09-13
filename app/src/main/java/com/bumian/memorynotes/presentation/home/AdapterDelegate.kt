package com.bumian.memorynotes.presentation.home

interface AdapterDelegate<T> {
    fun resetItems(items: List<T>)
}