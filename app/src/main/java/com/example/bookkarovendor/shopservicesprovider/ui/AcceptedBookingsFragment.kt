package com.example.bookkarovendor.shopservicesprovider.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentAcceptedShopBinding
import com.example.bookkarovendor.deliveryservicesprovider.ui.FirestoreRepositoryDelivery


class AcceptedBookingsFragment : Fragment() {

    private lateinit var viewModel: ShopBookingsViewModel

    private lateinit var binding: FragmentAcceptedShopBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel =
            ViewModelProvider(this, ShopBookingsViewModelFactory(requireActivity().application)).get(
                ShopBookingsViewModel::class.java
            )
        binding =  DataBindingUtil.inflate(inflater, R.layout.fragment_accepted_shop, container, false)

        setNoBookings()
        binding.bookingsRecyclerShopAccepted.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getAcceptedBookings()
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer { bookings ->
                if (!bookings.isNullOrEmpty()) {
                    setBookingsExist()
                    val adapter = AcceptBookingsAdapterShop.AcceptedBookingsAdapterShop(
                        bookings,
                        requireContext(),
                        FirestoreRepositoryShop(requireActivity().application)
                    )
                    binding.bookingsRecyclerShopAccepted.adapter = adapter
                } else {
                    setNoBookings()
                }
            })

        return binding.root
    }

    private fun setNoBookings() {
        binding.bookingsRecyclerShopAccepted.visibility = View.GONE
        binding.noBookingsTextShopAccepted.visibility = View.VISIBLE
        binding.noBookingsImageShopAccepted.visibility = View.VISIBLE

    }

    private fun setBookingsExist() {
        binding.bookingsRecyclerShopAccepted.visibility = View.VISIBLE
        binding.noBookingsTextShopAccepted.visibility = View.GONE
        binding.noBookingsImageShopAccepted.visibility = View.GONE
    }

}
