package com.bumian.memorynotes.presentation.note

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumian.memorynotes.R
import com.bumian.memorynotes.common.EventBus
import com.bumian.memorynotes.common.isDouble
import com.bumian.memorynotes.presentation.analytics.AnalyticsViewModel
import com.bumian.memorynotes.repo.analytics.FirebaseAnalytics
import com.bumian.memorynotes.repo.api.room.AppDataBase
import com.bumian.memorynotes.repo.api.room.note.LatLng
import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.notes.RoomNotes
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*


class AddNoteFragment: BottomSheetDialogFragment() {

    private var imageUri: Uri ?= null

    private val analyticsViewModel by lazy {
        AnalyticsViewModel(FirebaseAnalytics())
    }

    private val viewModel by lazy {
        AddNoteViewModel(RoomNotes(AppDataBase.db!!.noteDao()))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        analyticsViewModel.sendEvent("Add note screen opened")
        viewModel.noteAddedLiveData.observe(viewLifecycleOwner) {
            dismiss()
        }

        setupListeners()
    }

    private fun setupListeners() {
        addNote.setOnClickListener {
            var flag = true

            val titleText = title.text
            val message = message.text
            val latStr = lat.text
            val lngStr = lng.text

            if(titleText.isNullOrBlank()) {
                flag = false
                titleLayout.error = resources.getString(R.string.title_should_not_be_empty)
            }

            if(message.isNullOrBlank()) {
                flag = false
                messageLayout.error = resources.getString(R.string.message_should_not_be_empty)
            }

            if(latStr.isNullOrBlank() || !latStr.toString().isDouble()) {
                flag = false
                latLayout.error = resources.getString(R.string.lat_should_not_be_empty)
            }

            if(lngStr.isNullOrBlank() || !lngStr.toString().isDouble()) {
                flag = false
                lngLayout.error = resources.getString(R.string.lng_should_not_be_empty)
            }

            if(imageUri == null) {
                flag = false
                uploadLayout.background = ContextCompat.getDrawable(requireContext(), R.drawable.error_background)
            }

            if(!flag) {
                return@setOnClickListener
            }

            val note = Note(
                id = UUID.randomUUID().toString().toCharArray(),
                title = titleText.toString(),
                message = message.toString(),
                image = imageUri.toString(),
                location = LatLng(
                    latStr.toString().toDouble(),
                    lngStr.toString().toDouble()
                ),
                date = Date()
            )

            analyticsViewModel.sendEvent("Note added")
            viewModel.addNote(note)
        }

        title.addTextChangedListener { titleLayout.error = "" }
        message.addTextChangedListener { messageLayout.error = "" }
        lat.addTextChangedListener { latLayout.error = "" }
        lng.addTextChangedListener { lngLayout.error = "" }

        uploadLayout.setOnClickListener {
            uploadLayout.background = null
            openGallery()
        }
    }

    @Suppress("DEPRECATION")
    private fun openGallery() {
        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI).apply {
            type = "image/*"
            startActivityForResult(Intent.createChooser(this, "Select Picture"), 1)
        }
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    try {
                        imageUri = data.data
                        imageItem.layoutParams = FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT
                        )

                        lifecycleScope.launch {
                            delay(500)
                            withContext(Dispatchers.Main) {
                                Glide.with(imageItem.context)
                                    .load(data.data)
                                    .into(imageItem)
                            }
                        }

                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                } else {
                    Toast.makeText(activity, "Canceled", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.pushEvent("Add note dialog close")
    }
}