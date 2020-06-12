package com.example.bookkarovendor

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookkarovendor.deliveryservicesprovider.DeliveryServicesActivity
import com.example.bookkarovendor.helper.SharedPreference
import com.example.bookkarovendor.householdserviceprovider.HouseholdServicesActivity
import com.example.bookkarovendor.shopservicesprovider.ShopServicesActivity
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        var sharedPref:SharedPreference= SharedPreference(this)
        super.onCreate(savedInstanceState)
        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
           val code = sharedPref.getValueLong("type")?.toInt()
            //TODO: Read values of type from SharedPreferences. Based on the type, set the value of classToNavigateTo. types 100 -> delivery, 101 -> household, 10 -> shop
           if(code==100){
               var classToNavigateTo = DeliveryServicesActivity::class.java
               val intent = Intent(this,classToNavigateTo)
               startActivity(intent)
               finish()
           }
           else if(code==101){
               var classToNavigateTo = HouseholdServicesActivity::class.java
               val intent = Intent(this,classToNavigateTo)
               startActivity(intent)
               finish()
           }
           else if(code==10){
               var classToNavigateTo = ShopServicesActivity::class.java
               val intent = Intent(this,classToNavigateTo)
               startActivity(intent)
               finish()
           }

        }
    }
}
