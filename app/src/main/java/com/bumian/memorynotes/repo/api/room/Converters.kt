package com.bumian.memorynotes.repo.api.room

import androidx.room.TypeConverter
import com.bumian.memorynotes.repo.api.room.note.LatLng
import java.text.SimpleDateFormat
import java.util.*
import kotlin.streams.toList


class Converters {

    private val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    @TypeConverter
    fun fromDate(date: Date): String {
        return formatter.format(date)
    }

    @TypeConverter
    fun toDate(dateString: String): Date? {
        return formatter.parse(dateString)
    }

    @TypeConverter
    fun fromCharArray(arr: CharArray): String {
        val builder = StringBuilder()
        arr.forEach {
            builder.append(it)
        }

        return builder.toString()
    }

    @TypeConverter
    fun toCharArray(arrString: String): CharArray {
        val builder = StringBuilder(arrString)
        val charArray = CharArray(arrString.length)
        builder.chars().toList().forEachIndexed { index, i ->
            charArray[index] = i.toChar()
        }

        return charArray
    }

    @TypeConverter
    fun fromStringList(items: List<String>): String {
        val builder = StringBuilder()
        items.forEach { item ->
            builder.append(item)
        }

        return builder.toString()
    }

    @TypeConverter
    fun toStringList(listString: String): List<String> {
        val builder = StringBuilder(listString)
        val list = mutableListOf<String>()

        builder.toList().forEach { element ->
            list.add(element.toString())
        }

        return list
    }

    @TypeConverter
    fun fromLatLng(latLng: LatLng): String {
        val builder = StringBuilder().apply {
            append(latLng.lat)
            append(" ")
            append(latLng.lng)
        }
        return builder.toString()
    }

    @TypeConverter
    fun toLatLng(latLngString: String): LatLng {
        val builder = StringBuilder(latLngString)
        val params = builder.split(" ")

        return LatLng(lat = params[0].toDouble(), lng = params[1].toDouble())
    }
}