package com.example.bookkarovendor.deliveryservicesprovider.ui

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
import kotlinx.android.synthetic.main.list_item_accept_booking.view.booking_progress
import kotlinx.android.synthetic.main.list_item_accepted_booking.view.*
import kotlinx.android.synthetic.main.list_item_available_delivery.view.booking_accept_delivery_available
import kotlinx.android.synthetic.main.list_item_available_delivery.view.booking_date_delivery_available
import kotlinx.android.synthetic.main.list_item_available_delivery.view.booking_price_delivery_available
import kotlinx.android.synthetic.main.list_item_available_delivery.view.booking_service_name_delivery_available
import kotlinx.android.synthetic.main.list_item_delivery_accepted.view.*
import java.text.SimpleDateFormat
import java.util.*

class BookingDelivery(
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

 class AcceptBookingViewHolderDelivery(view: View) : RecyclerView.ViewHolder(view) {
    val serviceDateText: TextView = view.booking_date_delivery_available
    val serviceNameText: TextView = view.booking_service_name_delivery_available
    val servicePriceText: TextView = view.booking_price_delivery_available
    val acceptService: ImageView = view.booking_accept_delivery_available
    val acceptProgress: ProgressBar? = view.booking_progress
}

class AcceptBookingsAdapterDelivery(
    private val items: List<BookingDelivery>,
    private val context: Context,
    private val application: Application,
    private val viewModel: DeliveryBookingsViewModel
) : RecyclerView.Adapter<AcceptBookingViewHolderDelivery>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptBookingViewHolderDelivery {
        return AcceptBookingViewHolderDelivery(
            LayoutInflater.from(context).inflate(R.layout.list_item_available_delivery, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

    override fun onBindViewHolder(holder: AcceptBookingViewHolderDelivery, position: Int) {
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
                if(holder.acceptProgress!==null)
                holder.acceptProgress.visibility = View.VISIBLE
                viewModel.acceptBooking(booking.docID)
            }
        }

    }

    class AcceptedBookingViewHolderDelivery(view: View) : RecyclerView.ViewHolder(view) {
        val serviceDateText: TextView = view.booking_date_delivery_accepted
        val serviceNameText: TextView = view.booking_service_name_delivery_accepted
        val servicePriceText: TextView = view.booking_price_delivery_accepted
        val workStartedButton: MaterialButton = view.workStartedButton_delivery_accepted
        val workCompletedButton: MaterialButton = view.workCompletedButton_delivery_accepted
        val cancelWorkButton: MaterialButton = view.cancelWorkButton_delivery_accepted
    }

    class AcceptedBookingsAdapterDelivery(
        private val items: List<BookingDelivery>,
        private val context: Context,
        private val repository: FirestoreRepositoryDelivery
    ) : RecyclerView.Adapter<AcceptedBookingViewHolderDelivery>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AcceptedBookingViewHolderDelivery {
            return AcceptedBookingViewHolderDelivery(
                LayoutInflater.from(context)
                    .inflate(R.layout.list_item_delivery_accepted, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

        override fun onBindViewHolder(holder: AcceptedBookingViewHolderDelivery, position: Int) {
            val booking = items[position]
            val priceText = "${context.getString(R.string.rupee_symbol)}${booking.servicePrice}"
            holder.serviceDateText.text = formatter.format(booking.serviceDate)
            holder.serviceNameText.text = booking.serviceName
            holder.servicePriceText.text = priceText

            holder.workStartedButton.setOnClickListener {
                repository.updateBooking(booking.docID, BookingDelivery.STATUS_STARTED)
            }

            holder.workCompletedButton.setOnClickListener {
                repository.updateBooking(booking.docID, BookingDelivery.STATUS_COMPLETED)
            }

            holder.cancelWorkButton.setOnClickListener {
                repository.cancelBooking(booking.docID)
            }

        }
    }
}