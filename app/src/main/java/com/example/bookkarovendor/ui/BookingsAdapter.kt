package com.example.bookkarovendor.ui

import android.app.Application
import android.content.Context
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

data class Booking(
    val docID: String,
    val acceptedShopNumber: String?,
    val serviceDate: Date,
    val serviceName: String,
    val servicePrice: Long,
    val shopAddress: String?,
    val shopIconUrl: String?,
    val shopName: String?,
    val status: Long,
    val userId: String
) {
    companion object {
        const val STATUS_PENDING = 100L
        const val STATUS_ACCEPTED = 101L
        const val STATUS_STARTED = 102L
        const val STATUS_COMPLETED = 103L
        const val STATUS_CANCELED = 104L
    }
}

class AcceptBookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val serviceDateText: TextView = view.booking_date
    val serviceNameText: TextView = view.booking_service_name
    val servicePriceText: TextView = view.booking_price
    val acceptService: ImageView = view.booking_accept
    val acceptProgress: ProgressBar = view.booking_progress
}

class AcceptBookingsAdapter(
    private val items: List<Booking>,
    private val context: Context,
    private val application: Application,
    private val viewModel: BookingsViewModel
) : RecyclerView.Adapter<AcceptBookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptBookingViewHolder {
        return AcceptBookingViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_accept_booking, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

    override fun onBindViewHolder(holder: AcceptBookingViewHolder, position: Int) {
        val booking = items[position]
        val priceText = "${application.getString(R.string.rupee_symbol)}${booking.servicePrice}"
        holder.serviceDateText.text = formatter.format(booking.serviceDate)
        holder.serviceNameText.text = booking.serviceName
        holder.servicePriceText.text = priceText
        holder.acceptService.setOnClickListener {
            holder.acceptService.visibility = View.GONE
            holder.acceptProgress.visibility = View.VISIBLE
            viewModel.acceptBooking(booking.docID)
        }
    }
}

class AcceptedBookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val serviceDateText: TextView = view.booking_date
    val serviceNameText: TextView = view.booking_service_name
    val servicePriceText: TextView = view.booking_price
    val workStartedButton: MaterialButton = view.workStartedButton
    val workCompletedButton: MaterialButton = view.workCompletedButton
    val cancelWorkButton: MaterialButton = view.cancelWorkButton
}

class AcceptedBookingsAdapter(
    private val items: List<Booking>,
    private val context: Context,
    private val repository: FirestoreRepository
) : RecyclerView.Adapter<AcceptedBookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedBookingViewHolder {
        return AcceptedBookingViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_item_accepted_booking, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

    override fun onBindViewHolder(holder: AcceptedBookingViewHolder, position: Int) {
        val booking = items[position]
        val priceText = "${context.getString(R.string.rupee_symbol)}${booking.servicePrice}"
        holder.serviceDateText.text = formatter.format(booking.serviceDate)
        holder.serviceNameText.text = booking.serviceName
        holder.servicePriceText.text = priceText

        holder.workStartedButton.setOnClickListener {
            repository.updateBooking(booking.docID, Booking.STATUS_STARTED)
        }

        holder.workCompletedButton.setOnClickListener {
            repository.updateBooking(booking.docID, Booking.STATUS_COMPLETED)
        }

        holder.cancelWorkButton.setOnClickListener {
            repository.cancelBooking(booking.docID)
        }

    }
}