package com.example.bookkarovendor.ui

import android.app.Application
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.bookkarovendor.R
import kotlinx.android.synthetic.main.list_item_accept_booking.view.*
import java.text.SimpleDateFormat
import java.util.*

data class Booking(
    val docID: String,
    val serviceDate: Date,
    val serviceName: String,
    val servicePrice: Long
) {
    companion object {
        const val STATUS_PENDING = 100L
        const val STATUS_ACCEPTED = 101L
        const val STATUS_CANCELED = 102L
    }
}

class AcceptBookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val serviceDateText: TextView = view.booking_date
    val serviceNameText: TextView = view.booking_service_name
    val servicePriceText: TextView = view.booking_price
    val acceptService: ImageView = view.booking_accept
}

class AcceptBookingsAdapter(
    private val items: List<Booking>,
    private val context: Context,
    private val application: Application
) : RecyclerView.Adapter<AcceptBookingViewHolder>() {

    private val firestoreRepository = FirestoreRepository(application)

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
            firestoreRepository.acceptBooking(booking.docID)
            Log.d("AcceptBookingsAdapter", "Accepted booking")
        }
    }
}

class AcceptedBookingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val serviceDateText: TextView = view.booking_date
    val serviceNameText: TextView = view.booking_service_name
    val servicePriceText: TextView = view.booking_price
}

class AcceptedBookingsAdapter(private val items: List<Booking>, private val context: Context) :
    RecyclerView.Adapter<AcceptedBookingViewHolder>() {

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
    }
}