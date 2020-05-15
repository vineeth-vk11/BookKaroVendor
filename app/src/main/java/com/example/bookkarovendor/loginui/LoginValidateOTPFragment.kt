package com.example.bookkarovendor.loginui

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.bookkarovendor.MainActivity
import com.example.bookkarovendor.R
import com.example.bookkarovendor.databinding.FragmentLoginValidateOtpBinding
import com.google.android.gms.tasks.TaskExecutors
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.concurrent.TimeUnit

class LoginValidateOTPFragment : Fragment() {

    private lateinit var storedVerificationId: String
    private lateinit var phone: String

    private lateinit var binding: FragmentLoginValidateOtpBinding
    private lateinit var db: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_login_validate_otp,
            container,
            false
        )
        db = FirebaseFirestore.getInstance()


        phone = requireArguments().getString("phone_number").toString()
        val otpMessage = getString(R.string.otp_sent_to) + " +91 $phone"
        binding.validateOtpSubHeader.text = otpMessage

        sendAuthRequest()

        binding.validateButton.setOnClickListener {
            val credential = PhoneAuthProvider.getCredential(
                storedVerificationId,
                binding.loginOtpEdit.text.toString()
            )
            signInWithPhoneAuthCredential(credential)
        }

        return binding.root
    }

    private fun sendAuthRequest() {
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                Snackbar.make(
                    binding.validateOtpCoordinator,
                    R.string.verification_failed,
                    Snackbar.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                storedVerificationId = verificationId
            }
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91$phone",
            60,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks
        )

        setWaitingForOTP()

        object : CountDownTimer(60000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.codeNotReceivedWaitText.text = getWaitTime(millisUntilFinished)
            }

            override fun onFinish() {
                setRequestAgain()
            }
        }.start()

    }

    private fun getWaitTime(milli: Long): String {
        val seconds = TimeUnit.MILLISECONDS.toSeconds(milli)
        return if (seconds <= 9)
            "Wait for 0:0$seconds"
        else
            "Wait for 0:$seconds"
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        setLoading()
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                startActivity(Intent(activity, MainActivity::class.java))
                activity!!.finish()
            } else {
                setNotLoading()
                Snackbar.make(
                    binding.validateOtpCoordinator,
                    R.string.verification_failed,
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setLoading() {
        binding.validateOtpProgress.visibility = View.VISIBLE
        binding.validateOtpConstrain.visibility = View.INVISIBLE
        binding.validateButton.isEnabled = false
    }

    private fun setNotLoading() {
        binding.validateOtpProgress.visibility = View.GONE
        binding.validateOtpConstrain.visibility = View.VISIBLE
        binding.validateButton.isEnabled = true
    }

    private fun setWaitingForOTP() {
        binding.codeNotReceivedWaitText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorTextSubHeader
            )
        )
        binding.codeNotReceivedWaitText.setOnClickListener {}
    }

    private fun setRequestAgain() {
        binding.codeNotReceivedWaitText.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.colorTextHeader
            )
        )
        binding.codeNotReceivedWaitText.text = getString(R.string.resend_otp)
        binding.codeNotReceivedWaitText.setOnClickListener {
            sendAuthRequest()
        }
    }

}