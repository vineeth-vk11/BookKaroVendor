package com.example.bookkarovendor.householdserviceprovider.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAvailableBookingsBinding

class AvailableBookingsFragment : Fragment() {

    private lateinit var viewModel: BookingsViewModel

    private lateinit var binding: FragmentAvailableBookingsBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, BookingsViewModelFactory(requireActivity().application)).get(
                BookingsViewModel::class.java
            )
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_available_bookings,
            container,
            false
        )

        binding.bookingsRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getPendingBookings()
            .observe(viewLifecycleOwner, Observer { bookings ->
                if (!bookings.isNullOrEmpty()) {
                    setBookingsExist()
                    val adapter = AcceptBookingsAdapter(
                        bookings,
                        requireContext(),
                        requireActivity().application,
                        viewModel
                    )
                    binding.bookingsRecycler.adapter = adapter
                } else {
                    setNoBookings()
                }
            })

        return binding.root
    }

    private fun setNoBookings() {
        binding.bookingsRecycler.visibility = View.GONE
        binding.noBookingsText.visibility = View.VISIBLE
        binding.noBookingsImage.visibility = View.VISIBLE
    }

    private fun setBookingsExist() {
        binding.bookingsRecycler.visibility = View.VISIBLE
        binding.noBookingsText.visibility = View.GONE
        binding.noBookingsImage.visibility = View.GONE
    }

}
