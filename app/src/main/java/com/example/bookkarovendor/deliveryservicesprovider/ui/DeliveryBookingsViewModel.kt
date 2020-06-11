package com.example.bookkarovendor.deliveryservicesprovider.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookkarovendor.householdserviceprovider.ui.BookingsViewModel

class DeliveryBookingsViewModel (private val application: Application) : ViewModel(){

}
class DeliveryBookingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DeliveryBookingsViewModel(application) as T
    }
}
