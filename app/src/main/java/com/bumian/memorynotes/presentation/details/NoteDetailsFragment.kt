package com.bumian.memorynotes.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumian.memorynotes.MainActivity
import com.bumian.memorynotes.R
import com.bumian.memorynotes.presentation.home.HomeFragment
import com.bumian.memorynotes.presentation.home.HomeViewModel
import com.bumian.memorynotes.repo.api.room.AppDataBase
import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.auth.RoomAuth
import com.bumian.memorynotes.repo.notes.RoomNotes
import com.bumptech.glide.Glide
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_note.*
import kotlinx.android.synthetic.main.layout_toolbar.*


private const val NOTE = "NOTE"

class NoteDetailsFragment: Fragment(), OnMapReadyCallback {

    val viewModel by lazy {
        HomeViewModel(
            requireActivity().application,
            RoomNotes(AppDataBase.db!!.noteDao()),
            RoomAuth(AppDataBase.db!!)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_note, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mapView.run {
            onCreate(savedInstanceState)
            getMapAsync(this@NoteDetailsFragment)
        }

        arguments?.getParcelable<Note>(NOTE)?.let { note ->
            toolbarTitle.text = note.title
            message.text = note.message

            Glide.with(requireContext())
                .load(note.image.toUri())
                .into(noteImage)

            removeContainer.setOnClickListener {
                AlertDialog
                    .Builder(requireContext())
                    .setTitle(R.string.delete_note_title)
                    .setMessage(R.string.delete_note_message)
                    .setPositiveButton(R.string.delete_note) { _, _ ->
                        viewModel.removeNote(note)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ -> }
                    .create()
                    .show()
            }
        }

        back.isVisible = true
        back.setOnClickListener {
            (requireActivity() as MainActivity).replaceFragmentClearBackstack(HomeFragment())
        }

        viewModel.noteRemovedLiveData.observe(viewLifecycleOwner) {
            (requireActivity() as MainActivity).replaceFragmentClearBackstack(HomeFragment())
        }


    }

    override fun onMapReady(googleMap: GoogleMap) {
        googleMap.apply {
            arguments?.getParcelable<Note>(NOTE)?.let { note ->
                moveCamera(CameraUpdateFactory.newLatLng(LatLng(note.location.lat, note.location.lng)))
                addMarker(
                    MarkerOptions()
                    .position(LatLng(note.location.lat, note.location.lng))
                    .title(note.title)
                )
            }
        }
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
    }


    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView?.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        fun newInstance(note: Note) = NoteDetailsFragment().apply {
            arguments = bundleOf(NOTE to note)
        }
    }
}