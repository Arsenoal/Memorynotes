package com.bumian.memorynotes.presentation.filter

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumian.memorynotes.R
import com.bumian.memorynotes.presentation.analytics.AnalyticsViewModel
import com.bumian.memorynotes.presentation.home.HomeFragment
import com.bumian.memorynotes.repo.analytics.FirebaseAnalytics
import kotlinx.android.synthetic.main.fragment_filter.*
import java.util.*

class FilterDialogFragment: DialogFragment() {

    private val homeViewModel by lazy {
        (parentFragment as HomeFragment).viewModel
    }

    private val analyticsViewModel by lazy {
        AnalyticsViewModel(FirebaseAnalytics())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        analyticsViewModel.sendEvent("Filter opened")

        val params = mutableListOf<Date>()

        startDateValue.setOnClickListener {
            showDatePicker { startDate ->
                startDateValue.text = startDate.toString()
                params.add(startDate)
            }
        }
        endDateValue.setOnClickListener {
            showDatePicker { endDate ->
                endDateValue.text = endDate.toString()
                params.add(endDate)
            }
        }

        applyFilter.setOnClickListener {
            if(params.size == 2)  {
                homeViewModel.filterNotes(params[0], params[1])
            }

            dismiss()
        }
    }

    private fun showDatePicker(result: (Date) -> Unit) {
        val calendar = Calendar.getInstance()

        DatePickerDialog(
            requireContext(), { view, year, month, day ->
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                calendar.set(Calendar.DAY_OF_MONTH, day)
                result.invoke(calendar.time)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }
}