package com.example.bookkarovendor.shopservicesprovider.ui

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
import kotlinx.android.synthetic.main.list_item_accepted_booking.view.*
import kotlinx.android.synthetic.main.list_item_available_shop.view.*
import kotlinx.android.synthetic.main.list_item_shop_accepted.view.*
import java.text.SimpleDateFormat
import java.util.*

 class BookingShop(
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

 class AcceptBookingViewHolderShop(view: View) : RecyclerView.ViewHolder(view) {
    val serviceDateText: TextView = view.booking_date_shop_available
    val serviceNameText: TextView = view.booking_service_name_shop_available
    val servicePriceText: TextView = view.booking_price_shop_available
    val acceptService: ImageView = view.booking_accept_shop_available
    val acceptProgress: ProgressBar? = view.booking_progress
}

 class AcceptBookingsAdapterShop(
    private val items: List<BookingShop>,
    private val context: Context,
    private val application: Application,
    private val viewModel: ShopBookingsViewModel
) : RecyclerView.Adapter<AcceptBookingViewHolderShop>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptBookingViewHolderShop {
        return AcceptBookingViewHolderShop(
            LayoutInflater.from(context).inflate(R.layout.list_item_available_shop, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

    override fun onBindViewHolder(holder: AcceptBookingViewHolderShop, position: Int) {
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

    class AcceptedBookingViewHolderShop(view: View) : RecyclerView.ViewHolder(view) {
        val serviceDateText: TextView = view.booking_date_shop_accepted
        val serviceNameText: TextView = view.booking_service_name_shop_accepted
        val servicePriceText: TextView = view.booking_price_shop_accepted
        val workStartedButton: MaterialButton = view.workStartedButton_shop_accepted
        val workCompletedButton: MaterialButton = view.workCompletedButton_shop_accepted
        val cancelWorkButton: MaterialButton = view.cancelWorkButton_shop_accepted
    }

    class AcceptedBookingsAdapterShop(
        private val items: List<BookingShop>,
        private val context: Context,
        private val repository: FirestoreRepositoryShop
    ) : RecyclerView.Adapter<AcceptedBookingViewHolderShop>() {

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): AcceptedBookingViewHolderShop {
            return AcceptedBookingViewHolderShop(
                LayoutInflater.from(context)
                    .inflate(R.layout.list_item_shop_accepted, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return items.size
        }

        private val formatter = SimpleDateFormat("MMMM dd hh:mm a", Locale.getDefault())

        override fun onBindViewHolder(holder: AcceptedBookingViewHolderShop, position: Int) {
            val booking = items[position]
            val priceText = "${context.getString(R.string.rupee_symbol)}${booking.servicePrice}"
            holder.serviceDateText.text = formatter.format(booking.serviceDate)
            holder.serviceNameText.text = booking.serviceName
            holder.servicePriceText.text = priceText

            holder.workStartedButton.setOnClickListener {
                repository.updateBooking(booking.docID, BookingShop.STATUS_STARTED)
            }

            holder.workCompletedButton.setOnClickListener {
                repository.updateBooking(booking.docID, BookingShop.STATUS_COMPLETED)
            }

            holder.cancelWorkButton.setOnClickListener {
                repository.cancelBooking(booking.docID)
            }

        }
    }
}