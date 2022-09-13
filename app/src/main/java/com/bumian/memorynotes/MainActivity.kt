package com.bumian.memorynotes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.bumian.memorynotes.presentation.auth.AuthFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        replaceFragmentClearBackstack(AuthFragment())
    }

    fun replaceFragmentClearBackstack(fragment: Fragment) {
        supportFragmentManager.run {
            var i = 0
            while(i < backStackEntryCount) {
                popBackStack()
                i++
            }
            beginTransaction().replace(
                R.id.fragmentContainer,
                fragment
            ).addToBackStack(null).commit()
        }
    }

    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.fragmentContainer,
            fragment
        ).addToBackStack(null).commit()
    }
}