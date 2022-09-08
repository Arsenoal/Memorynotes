package com.bumian.memorynotes.presentation.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumian.memorynotes.MainActivity
import com.bumian.memorynotes.R
import com.bumian.memorynotes.presentation.analytics.AnalyticsViewModel
import com.bumian.memorynotes.presentation.home.HomeFragment
import com.bumian.memorynotes.repo.analytics.FirebaseAnalytics
import com.bumian.memorynotes.repo.auth.RoomAuth
import com.bumian.memorynotes.repo.api.room.AppDataBase
import kotlinx.android.synthetic.main.fragment_auth.*

class AuthFragment: Fragment() {

    private val viewModel by lazy {
        AuthViewModel(
            RoomAuth(db = AppDataBase.instance(requireContext().applicationContext))
        )
    }

    private val analyticsViewModel by lazy {
        AnalyticsViewModel(FirebaseAnalytics())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        analyticsViewModel.sendEvent("Auth screen")

        viewModel.existingUsers.observe(viewLifecycleOwner) {
            analyticsViewModel.sendEvent("User entered with existing account")
            (requireActivity() as MainActivity).replaceFragment(HomeFragment())
        }
        viewModel.checkAccount()

        enterWithAccount.setOnClickListener {
            val userNameText = userName.text.toString()
            if(userNameText.isEmpty()) {
                userNameLayout.error = "user name should not be empty"
            } else {
                viewModel.userLoginSuccess.observe(viewLifecycleOwner) { flag ->
                    (requireActivity() as MainActivity).replaceFragment(HomeFragment())
                    analyticsViewModel.sendEvent("User created account")
                }
                viewModel.enterWith(userNameText)
            }
        }
    }
}