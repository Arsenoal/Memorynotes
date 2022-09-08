package com.bumian.memorynotes.presentation.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.cardview.widget.CardView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumian.memorynotes.R
import com.bumian.memorynotes.repo.api.room.note.Note

class NotesAdapter(
    val notes: MutableList<Note> = mutableListOf(),
    val onWatchOnMapClick: (Note) -> Unit,
    val onRemoveClick: (Note) -> Unit
): RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    lateinit var inflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        inflater = LayoutInflater.from(recyclerView.context)
    }

    inner class NotesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        val container = itemView.findViewById<CardView>(R.id.itemContainer)
        val noteImage = itemView.findViewById<ImageView>(R.id.noteImage)
        val noteMessage = itemView.findViewById<TextView>(R.id.noteMessage)
        val noteTitle = itemView.findViewById<TextView>(R.id.noteTitle)
        val watchOnMap = itemView.findViewById<LinearLayout>(R.id.watchOnMap)
        val remove = itemView.findViewById<AppCompatImageButton>(R.id.remove)

        fun bind(note: Note) {
            Glide.with(noteImage.context)
                .load(note.image.toUri())
                .into(noteImage)

            noteMessage.text = note.message
            noteTitle.text = note.title
            watchOnMap.setOnClickListener {
                onWatchOnMapClick.invoke(note)
            }
            remove.setOnClickListener {
                onRemoveClick.invoke(note)
            }

            container.setOnClickListener {

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NotesViewHolder(inflater.inflate(R.layout.item_layout, parent, false))

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.bind(notes[position])
    }

    override fun getItemCount() = notes.size

    fun resetItems(items: List<Note>) {
        notes.clear()
        notes.addAll(items)
        notifyDataSetChanged()
    }

}