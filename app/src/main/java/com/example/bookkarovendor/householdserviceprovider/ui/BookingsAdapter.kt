package com.example.bookkarovendor.householdserviceprovider.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookkarovendor.R
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.list_item_accept_booking.view.*
import kotlinx.android.synthetic.main.list_item_accept_booking.view.booking_date
import kotlinx.android.synthetic.main.list_item_accept_booking.view.booking_price
import kotlinx.android.synthetic.main.list_item_accept_booking.view.booking_service_name
import kotlinx.android.synthetic.main.list_item_accepted_booking.view.*
import java.text.SimpleDateFormat
import java.util.*
 open class BookingHouseHold(
    val docID: String,
    val acceptedShopNumber: String?,
    val serviceDate: Date?,
    val serviceName: String?,
    val servicePrice: Long?,
    val shopAddress: String?,
    val shopIconUrl: String?,
    val shopName: String?,
    val status: Long?,
    val userId: String?,
    val type:Long?,
    val category:Long?
) {
    companion object {
        const val STATUS_PENDING = 100L
        const val STATUS_ACCEPTED = 101L
        const val STATUS_STARTED = 102L
        const val STATUS_COMPLETED = 103L
        const val STATUS_CANCELED = 104L
    }
}

  class AcceptBookingViewHolderHouseHold(view: View) : RecyclerView.ViewHolder(view) {
    val serviceDateText: TextView = view.booking_date
    val serviceNameText: TextView = view.booking_service_name
    val servicePriceText: TextView = view.booking_price
    val acceptService: ImageView = view.booking_accept
    val acceptProgress: ProgressBar = view.booking_progress
}

  class AcceptBookingsAdapterHouseHold(
      private val items: List<BookingHouseHold>,
      private val context: Context,
      private val application: Application,
      private val viewModel: BookingsViewModel
) : RecyclerView.Adapter<AcceptBookingViewHolderHouseHold>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptBookingViewHolderHouseHold {
        return AcceptBookingViewHolderHouseHold(
            LayoutInflater.from(context).inflate(R.layout.list_item_accept_booking, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

    override fun onBindViewHolder(holder: AcceptBookingViewHolderHouseHold, position: Int) {
        val booking = items[position]
        val priceText = "${application.getString(R.string.rupee_symbol)}${booking.servicePrice}"
        if (booking.serviceDate !== null && holder.serviceNameText.text !== null) {
            Log.d("HouseHold","Checking Nullity")
            holder.serviceDateText.text = formatter.format(booking.serviceDate)
            holder.serviceNameText.text = booking.serviceName
            holder.servicePriceText.text = priceText
            //click
            holder.acceptService.setOnClickListener {
                Log.d("HouseHold","adapter onClick Listener Triggered")
                holder.acceptService.visibility = View.GONE
                holder.acceptProgress.visibility = View.VISIBLE
                viewModel.acceptBooking(booking.docID)
            }
        }

    }

     class AcceptedBookingViewHolderHouseHold(view: View) : RecyclerView.ViewHolder(view) {
        val serviceDateText: TextView = view.booking_date
        val serviceNameText: TextView = view.booking_service_name
        val servicePriceText: TextView = view.booking_price
        val workStartedButton: MaterialButton = view.workStartedButton
        val workCompletedButton: MaterialButton = view.workCompletedButton
        val cancelWorkButton: MaterialButton = view.cancelWorkButton
    }

     class AcceptedBookingsAdapterHouseHold(
        private val items: List<BookingHouseHold>,
        private val context: Context,
        private val repository: FirestoreRepositoryHouseHold
    ) : RecyclerView.Adapter<AcceptedBookingViewHolderHouseHold>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AcceptedBookingViewHolderHouseHold {
            return AcceptedBookingViewHolderHouseHold(
                LayoutInflater.from(context)
                    .inflate(R.layout.list_item_accepted_booking, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

        override fun onBindViewHolder(holder: AcceptedBookingViewHolderHouseHold, position: Int) {
            val booking = items[position]
            val priceText = "${context.getString(R.string.rupee_symbol)}${booking.servicePrice}"
            holder.serviceDateText.text = formatter.format(booking.serviceDate)
            holder.serviceNameText.text = booking.serviceName
            holder.servicePriceText.text = priceText

            holder.workStartedButton.setOnClickListener {
                repository.updateBooking(booking.docID, BookingHouseHold.STATUS_STARTED)
            }

            holder.workCompletedButton.setOnClickListener {
                repository.updateBooking(booking.docID, BookingHouseHold.STATUS_COMPLETED)
            }

            holder.cancelWorkButton.setOnClickListener {
                repository.cancelBooking(booking.docID)
            }

        }
    }
}