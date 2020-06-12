package com.example.bookkarovendor.loginui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentLoginEnterPhoneBinding
import com.example.bookkarovendor.helper.SharedPreference
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class LoginEnterPhoneFragment : Fragment() {
    var sharedPref: SharedPreference = SharedPreference(requireContext())

    private lateinit var binding: FragmentLoginEnterPhoneBinding
    private lateinit var navController: NavController

    private lateinit var firebaseDb: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_login_enter_phone, container, false)
        navController = findNavController()
        firebaseDb = FirebaseFirestore.getInstance()

        binding.loginButton.setOnClickListener {
            val phone = binding.loginPhoneEdit.text.toString()
            validatePhoneAndRedirect(phone)
        }

        return binding.root
    }

    private fun validatePhoneAndRedirect(phone: String) {
        if (phone.matches(Regex("[1-9][0-9]{9}"))) {
            val phoneNumber = "+91$phone"
            firebaseDb.collection("ServiceProviders").document(phoneNumber).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val type = document.getLong("type")
                        val servicesProvided = document.getString("servicesProvided")
                        //TODO: Store these vendor details (type and servicesProvided) in SharedPreferences
                        if (type != null) {
                            sharedPref.save("type",type)
                        }
                        if (servicesProvided != null) {
                            sharedPref.save("servicesProvided",servicesProvided)
                        }
                        val action =
                            LoginEnterPhoneFragmentDirections.actionLoginEnterPhoneFragmentToLoginValidateOTPFragment(
                                phone
                            )
                        navController.navigate(action)
                    } else {
                        Snackbar.make(
                            binding.enterPhoneCoordinator,
                            "You're not registered as a vendor",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
        } else {
            Snackbar.make(
                binding.enterPhoneCoordinator,
                "Invalid phone number",
                Snackbar.LENGTH_SHORT
            ).show()
        }
    }

}