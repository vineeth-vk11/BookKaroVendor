package com.example.bookkarovendor.shopservicesprovider.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ShopBookingsViewModel (private val application: Application) : ViewModel(){

}
class ShopBookingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ShopBookingsViewModel(application) as T
    }
}