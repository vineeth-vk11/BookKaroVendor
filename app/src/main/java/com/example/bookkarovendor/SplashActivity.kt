package com.example.bookkarovendor

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookkarovendor.householdserviceprovider.HouseholdServicesActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            //TODO: Read values of type from SharedPreferences. Based on the type, set the value of classToNavigateTo. types 100 -> delivery, 101 -> household, 10 -> shop
            val classToNavigateTo = HouseholdServicesActivity::class.java
            val intent = Intent(this, classToNavigateTo)
            startActivity(intent)
            finish()
        }
    }
}
