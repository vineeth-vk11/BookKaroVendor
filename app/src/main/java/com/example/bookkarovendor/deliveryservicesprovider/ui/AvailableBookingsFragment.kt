package com.example.bookkarovendor.deliveryservicesprovider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAvailableDeliveryBinding


class AvailableBookingsFragment : Fragment() {

    private lateinit var viewModel: DeliveryBookingsViewModel

    private lateinit var binding: FragmentAvailableDeliveryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, DeliveryBookingsViewModelFactory(requireActivity().application)).get(
                DeliveryBookingsViewModel::class.java
            )
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_available_delivery,
            container,
            false
        )

        binding.bookingsRecyclerDeliveryAvailable.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getPendingBookings()
            .observe(viewLifecycleOwner, Observer { bookings ->
                if (!bookings.isNullOrEmpty()) {
                    setBookingsExist()
                    val adapter = AcceptBookingsAdapterDelivery(
                        bookings,
                        requireContext(),
                        requireActivity().application,
                        viewModel
                    )
                    binding.bookingsRecyclerDeliveryAvailable.adapter = adapter
                } else {
                    setNoBookings()
                }
            })

        return binding.root
    }

    private fun setNoBookings() {
        binding.bookingsRecyclerDeliveryAvailable.visibility = View.GONE
        binding.noBookingsTextDeliveryAvailable.visibility = View.VISIBLE
        binding.noBookingsImageDeliveryAvailable.visibility = View.VISIBLE
    }

    private fun setBookingsExist() {
        binding.bookingsRecyclerDeliveryAvailable.visibility = View.VISIBLE
        binding.noBookingsTextDeliveryAvailable.visibility = View.GONE
        binding.noBookingsImageDeliveryAvailable.visibility = View.GONE
    }

}