package com.example.bookkarovendor.householdserviceprovider

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.ActivityHouseholdServicesBinding

class HouseholdServicesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHouseholdServicesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_household_services
        )

        val navController = findNavController(R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
    }
}
