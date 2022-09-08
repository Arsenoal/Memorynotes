package com.bumian.memorynotes.repo.api.room.note

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Note(
    @PrimaryKey
    val id: CharArray = CharArray(0),
    val title: String,
    val message: String,
    val image: String,
    val location: LatLng,
    val date: Date
) {
    override fun toString(): String {
        return "$title-$message-$image-$location-$date"
    }
}

data class LatLng(
    val lat: Double,
    val lng: Double
)