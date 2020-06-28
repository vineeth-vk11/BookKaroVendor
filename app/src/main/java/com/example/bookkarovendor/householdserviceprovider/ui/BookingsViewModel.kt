package com.example.bookkarovendor.householdserviceprovider.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bookkarovendor.R
import com.example.bookkarovendor.helper.SharedPreferencesHelper
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

 class FirestoreRepositoryHouseHold(private val application: Application) {

    private val firestoreDB = FirebaseFirestore.getInstance()
    private val phone = FirebaseAuth.getInstance().currentUser!!.phoneNumber!!

    fun getBookings(): CollectionReference {
        return firestoreDB.collection(application.getString(R.string.firestore_collection_order_data))
    }

    fun getShopDetails(): DocumentReference {
        return firestoreDB.collection(application.getString(R.string.firestore_collection_vendor_data))
            .document(phone)
    }

    fun acceptBooking(
        bookingId: String,
        shopAddress: String,
        shopIconUrl: String,
        shopName: String
    ) {
        firestoreDB.collection(application.getString(R.string.firestore_collection_order_data))
            .document(bookingId)
            .update(
                mapOf(
                    application.getString(R.string.firestore_collection_order_data_field_status) to BookingHouseHold.STATUS_ACCEPTED,
                    application.getString(R.string.firestore_collection_order_data_field_accepted_shop_number)to phone,
                    application.getString(R.string.firestore_collection_order_data_field_shop_address) to shopAddress,
                    application.getString(R.string.firestore_collection_order_data_field_shop_icon_url) to shopIconUrl,
                    application.getString(R.string.firestore_collection_order_data_field_shop_name) to shopName
                )
            )
    }

    fun cancelBooking(bookingId: String) {
        firestoreDB.collection(application.getString(R.string.firestore_collection_order_data))
            .document(bookingId)
            .update(
                mapOf(
                    application.getString(R.string.firestore_collection_order_data_field_status) to BookingHouseHold.STATUS_PENDING,
                    application.getString(R.string.firestore_collection_order_data_field_accepted_shop_number) to "",
                    application.getString(R.string.firestore_collection_order_data_field_shop_address) to "",
                    application.getString(R.string.firestore_collection_order_data_field_shop_icon_url) to "",
                    application.getString(R.string.firestore_collection_order_data_field_shop_name) to ""
                )
            )
    }

    fun updateBooking(bookingId: String, status: Long) {
        firestoreDB.collection(application.getString(R.string.firestore_collection_order_data))
            .document(bookingId)
            .update(
                mapOf(
                    application.getString(R.string.firestore_collection_order_data_field_status) to status
                )
            )
    }

}

class BookingsViewModel(private val application: Application) : ViewModel() {

    private val TAG = "BOOKINGS_VIEW_MODEL"
    private val firestoreRepository = FirestoreRepositoryHouseHold(application)

    private val phone = FirebaseAuth.getInstance().currentUser!!.phoneNumber

    private var pendingBookings: MutableLiveData<List<BookingHouseHold>> = MutableLiveData()
    private var acceptedBookings: MutableLiveData<List<BookingHouseHold>> = MutableLiveData()

    fun acceptBooking(docId: String) {
        firestoreRepository.getShopDetails().get()
            .addOnSuccessListener { details ->
                firestoreRepository.getBookings()
                    .document(docId)
                    .update(
                        mapOf(
                            application.getString(R.string.firestore_collection_order_data_field_status) to BookingHouseHold.STATUS_ACCEPTED,
                            application.getString(R.string.firestore_collection_order_data_field_accepted_shop_number) to phone,
                            application.getString(R.string.firestore_collection_order_data_field_shop_address) to details.getString(
                                application.getString(R.string.firestore_collection_vendor_data_field_shop_address)
                            ),
                            application.getString(R.string.firestore_collection_order_data_field_shop_icon_url) to details.getString(
                                application.getString(R.string.firestore_collection_vendor_data_field_shop_icon_url)
                            ),
                            application.getString(R.string.firestore_collection_order_data_field_shop_name) to details.getString(
                                application.getString(R.string.firestore_collection_vendor_data_field_shop_name)
                            )

                        )
                    )
            }
    }

    fun getPendingBookings(): LiveData<List<BookingHouseHold>> {
        firestoreRepository.getBookings().whereEqualTo(
            application.getString(R.string.firestore_collection_order_data_field_status),
            BookingHouseHold.STATUS_PENDING
        ).whereEqualTo(application.getString(R.string.firestore_collection_order_data_field_type),
            SharedPreferencesHelper.HOUSEHOLD_SERVICE)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e(TAG, "Firestore listening failed.")
                pendingBookings.value = null
                return@addSnapshotListener
            }

            val pendingBookingsList: MutableList<BookingHouseHold> = mutableListOf()
            for (doc in querySnapshot!!) {
                pendingBookingsList.add(
                    BookingHouseHold(
                        doc.id,
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_accepted_shop_number)),
                        doc.getDate(application.getString(R.string.firestore_collection_order_data_field_service_date)),
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_service_name)),
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_service_price)),
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_shop_address)),
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_shop_icon_url)),
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_shop_name)),
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_status)),
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_user_id)),
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_type)),
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_category))
                    )

                )



            }
            pendingBookingsList.sortByDescending { it.serviceDate }
            pendingBookings.value = pendingBookingsList
        }
        return pendingBookings
    }

    fun getAcceptedBookings(): LiveData<List<BookingHouseHold>> {

        firestoreRepository.getBookings().whereEqualTo(
            application.getString(R.string.firestore_collection_order_data_field_accepted_shop_number),
            phone
        ).addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                Log.e(TAG, "Firestore listening failed.")
                acceptedBookings.value = null
                return@addSnapshotListener
            }

            val acceptedBookingsList: MutableList<BookingHouseHold> = mutableListOf()
            for (doc in querySnapshot!!) {
                acceptedBookingsList.add(
                    BookingHouseHold(
                        doc.id,
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_accepted_shop_number)),
                        doc.getDate(application.getString(R.string.firestore_collection_order_data_field_service_date))!!,
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_service_name))!!,
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_service_price))!!,
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_shop_address)),
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_shop_icon_url)),
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_shop_name)),
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_status))!!,
                        doc.getString(application.getString(R.string.firestore_collection_order_data_field_user_id))!!,
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_type)),
                        doc.getLong(application.getString(R.string.firestore_collection_order_data_field_category))
                    )
                )
            }
            acceptedBookingsList.sortByDescending { it.serviceDate }
            acceptedBookings.value = acceptedBookingsList
        }
        return acceptedBookings
    }

}

class BookingsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BookingsViewModel(application) as T
    }
}