package com.example.bookkarovendor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAcceptedBinding

class AcceptedBookingsFragment : Fragment() {

    private lateinit var viewModel: BookingsViewModel

    private lateinit var binding: FragmentAcceptedBinding

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, BookingsViewModelFactory(requireActivity().application)).get(
                BookingsViewModel::class.java
            )
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_accepted, container, false)

        setNoBookings()
        binding.bookingsRecycler.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getAcceptedBookings()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { bookings ->
                if (!bookings.isNullOrEmpty()) {
                    setBookingsExist()
                    val adapter = AcceptedBookingsAdapter(
                        bookings,
                        requireContext(),
                        FirestoreRepository(requireActivity().application)
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
