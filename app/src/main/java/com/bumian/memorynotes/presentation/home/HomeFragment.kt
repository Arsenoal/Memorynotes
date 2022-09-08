package com.bumian.memorynotes.presentation.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumian.memorynotes.R
import com.bumian.memorynotes.common.EventBus
import com.bumian.memorynotes.presentation.analytics.AnalyticsViewModel
import com.bumian.memorynotes.presentation.filter.FilterDialogFragment
import com.bumian.memorynotes.presentation.note.AddNoteFragment
import com.bumian.memorynotes.repo.analytics.FirebaseAnalytics
import com.bumian.memorynotes.repo.api.room.AppDataBase
import com.bumian.memorynotes.repo.api.room.note.Note
import com.bumian.memorynotes.repo.auth.RoomAuth
import com.bumian.memorynotes.repo.notes.RoomNotes
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class HomeFragment: Fragment() {

    private val analyticsViewModel by lazy {
        AnalyticsViewModel(FirebaseAnalytics())
    }

    val viewModel by lazy {
        HomeViewModel(
            RoomNotes(AppDataBase.db!!.noteDao()),
            RoomAuth(AppDataBase.db!!)
        )
    }

    private val adapter by lazy {
        NotesAdapter(
            onWatchOnMapClick = { note ->
                mapContainer.isVisible = true
                openMap(listOf(note))
            },
            onRemoveClick = { note ->
                AlertDialog
                    .Builder(requireContext())
                    .setTitle(R.string.delete_note_title)
                    .setMessage(R.string.delete_note_message)
                    .setPositiveButton(R.string.delete_note) { _, _ ->
                        viewModel.removeNote(note)
                    }
                    .setNegativeButton(R.string.cancel) { _, _ ->

                    }
                    .create()
                    .show()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        analyticsViewModel.sendEvent("Home page opened")
        setupView()
    }

    private fun setupView() {
        layout_no_notes_found.isVisible = false
        val allNotes = mutableListOf<Note>()

        viewModel.allNotesLiveData.observe(viewLifecycleOwner) { notes ->
            allNotes.clear()
            allNotes.addAll(notes)

            if(notes.isEmpty()) {
                layout_no_notes_found.isVisible = true
                layout_no_notes_found.setOnClickListener {
                    AddNoteFragment().show(childFragmentManager, null)
                }
                adapter.resetItems(listOf())
            } else {
                layout_no_notes_found.isVisible = false
                adapter.resetItems(notes)
            }
        }

        viewModel.filteredNotesLiveData.observe(viewLifecycleOwner) { notes ->
            adapter.resetItems(notes)
        }

        viewModel.userNameLiveData.observe(viewLifecycleOwner) { name ->
            toolbarTitle.text = resources.getString(R.string.hi, name)
        }

        viewModel.noteRemovedLiveData.observe(viewLifecycleOwner) {
            viewModel.getNotes()
        }

        viewModel.getNotes()
        viewModel.loadUserName()

        toolbar.inflateMenu(R.menu.home_menu)
        toolbar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.filter -> {
                    FilterDialogFragment().show(childFragmentManager, null)
                }
            }
            false
        }

        bottomNav.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.addNote -> {
                    AddNoteFragment().show(childFragmentManager, null)

                    true
                }
                R.id.watchOnMap -> {
                    mapContainer.isVisible = true
                    openMap(allNotes)

                    true
                }
                else -> false
            }
        }

        GlobalScope.launch {
            EventBus.collect("Add note dialog close").collect {
                viewModel.getNotes()
            }
        }

        items.adapter = adapter
        items.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun openMap(notes: List<Note>) {
        analyticsViewModel.sendEvent("Map opened")

        val mapFragment = SupportMapFragment.newInstance().apply {
            getMapAsync { mMap ->
                notes.forEach { note ->
                    mMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(note.location.lat, note.location.lng))
                            .title(note.title)
                    )
                }
            }
        }

        childFragmentManager
            .beginTransaction()
            .replace(R.id.googleMap, mapFragment)
            .commit()

        closeMap.setOnClickListener {
            mapContainer.isVisible = false
        }
    }

}