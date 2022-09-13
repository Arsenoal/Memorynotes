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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Note) return false

        if (!id.contentEquals(other.id)) return false
        if (title != other.title) return false
        if (message != other.message) return false
        if (image != other.image) return false
        if (location != other.location) return false
        if (date != other.date) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.contentHashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + message.hashCode()
        result = 31 * result + image.hashCode()
        result = 31 * result + location.hashCode()
        result = 31 * result + date.hashCode()
        return result
    }
}

data class LatLng(
    val lat: Double,
    val lng: Double
)