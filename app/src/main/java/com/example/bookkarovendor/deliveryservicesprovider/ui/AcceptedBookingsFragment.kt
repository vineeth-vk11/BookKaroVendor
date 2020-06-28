package com.example.bookkarovendor.deliveryservicesprovider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentDeliveryAcceptedBinding


class AcceptedBookingsFragment : Fragment() {

    private lateinit var viewModel: DeliveryBookingsViewModel

    private lateinit var binding: FragmentDeliveryAcceptedBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, DeliveryBookingsViewModelFactory(requireActivity().application)).get(
                DeliveryBookingsViewModel::class.java
            )
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_delivery_accepted, container, false)

        setNoBookings()
        binding.bookingsRecyclerDeliveryAccepted.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getAcceptedBookings()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { bookings ->
                if (!bookings.isNullOrEmpty()) {
                    setBookingsExist()
                    val adapter = AcceptBookingsAdapterDelivery.AcceptedBookingsAdapterDelivery(
                        bookings,
                        requireContext(),
                        FirestoreRepositoryDelivery(requireActivity().application)
                    )
                    binding.bookingsRecyclerDeliveryAccepted.adapter = adapter
                } else {
                    setNoBookings()
                }
            })

        return binding.root
    }

    private fun setNoBookings() {
        binding.bookingsRecyclerDeliveryAccepted.visibility = View.GONE
        binding.noBookingsTextDeliveryAccepted.visibility = View.VISIBLE
        binding.noBookingsImageDeliveryAccepted.visibility = View.VISIBLE

    }

    private fun setBookingsExist() {
        binding.bookingsRecyclerDeliveryAccepted.visibility = View.VISIBLE
        binding.noBookingsTextDeliveryAccepted.visibility = View.GONE
        binding.noBookingsImageDeliveryAccepted.visibility = View.GONE
    }

}
