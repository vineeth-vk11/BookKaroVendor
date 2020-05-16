package com.example.bookkarovendor.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookkarovendor.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore

class FirestoreRepository(private val application: Application) {

    init {
        Log.d("FirestoreRepository", "FirestoreRepository created")
    }

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    fun getBookings(): CollectionReference {
        return firestoreDB.collection(application.getString(R.string.firestore_collection_order_data))
    }

    fun acceptBooking(bookingId: String) {
        firestoreDB.collection(application.getString(R.string.firestore_collection_order_data))
            .document(bookingId)
            .update(
                mapOf(
                    application.getString(R.string.firestore_collection_order_data_field_status) to Booking.STATUS_ACCEPTED,
                    application.getString(R.string.firestore_collection_order_data_field_accepted_shop_id) to uid
                )
            )
    }

}

class BookingsViewModel(private val application: Application) : ViewModel() {

    private val TAG = "BOOKINGS_VIEW_MODEL"
    private val firestoreRepository = FirestoreRepository(application)

    init {
        Log.d(TAG, "BookingsViewModel created")
    }

    private val uid = FirebaseAuth.getInstance().currentUser!!.uid

    private var pendingBookings: MutableLiveData<List<Booking>> = MutableLiveData()
    private var acceptedBookings: MutableLiveData<List<Booking>> = MutableLiveData()

    fun getPendingBookings(): LiveData<List<Booking>> {
        Log.d(TAG, "getPendingBookings() called")
        firestoreRepository.getBookings().whereEqualTo(
            application.getString(R.string.firestore_collection_order_data_field_status),
            Booking.STATUS_PENDING
        ).addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            Log.d(TAG, "getPendingBookings Snapshot listener called")

            if (firebaseFirestoreException != null) {
                Log.e(TAG, "Firestore listening failed.")
                pendingBookings.value = null
                return@addSnapshotListener
            }

            val pendingBookingsList: MutableList<Booking> = mutableListOf()
            for (doc in querySnapshot!!) {
                Log.d(TAG, "getPendingBookings Found a document: ${doc.data}")
                pendingBookingsList.add(
                    Booking(
                        doc.id,
                        doc.getDate(application.getString(R.string.firestore_collection_order_data_field_service_date))!!,
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_service_name))!!,
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_service_price))!!
                    )
                )
            }
            pendingBookings.value = pendingBookingsList
            Log.d(TAG, "Value of pendingBookings changed")
        }
        Log.d(TAG, "Value of pendingBookings returned")
        return pendingBookings
    }

    fun getAcceptedBookings(): LiveData<List<Booking>> {

        firestoreRepository.getBookings().whereEqualTo(
            application.getString(R.string.firestore_collection_order_data_field_accepted_shop_id),
            uid
        ).addSnapshotListener { querySnapshot, firebaseFirestoreException ->

            Log.d(TAG, "getAcceptedBookings Snapshot listener called")

            if (firebaseFirestoreException != null) {
                Log.e(TAG, "Firestore listening failed.")
                acceptedBookings.value = null
                return@addSnapshotListener
            }

            val acceptedBookingsList: MutableList<Booking> = mutableListOf()
            for (doc in querySnapshot!!) {
                acceptedBookingsList.add(
                    Booking(
                        doc.id,
                        doc.getDate(application.getString(R.string.firestore_collection_order_data_field_service_date))!!,
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_service_name))!!,
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_service_price))!!
                    )
                )
            }
            acceptedBookings.value = acceptedBookingsList
            Log.d(TAG, "Value of acceptedBookings changed")
        }
        Log.d(TAG, "Value of acceptedBookings returned")
        return acceptedBookings
    }

}

class BookingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookingsViewModel(application) as T
    }
}