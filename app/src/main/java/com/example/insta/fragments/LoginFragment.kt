package com.example.insta.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.example.insta.R
import com.example.insta.databinding.FragmentLoginBinding
import com.example.insta.util.InstaApplication
import com.example.insta.util.ShowMessage.Companion.showMessage
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)

        auth = ((activity as FragmentActivity).application as InstaApplication).fAuth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginBtn.setOnClickListener {
            val email = binding.emailEdt.text.toString()
            val pswd = binding.passwordEdt.text.toString()

            //sign in
            if (
                email.isNotEmpty() &&
                pswd.isNotEmpty()
            ) {

                auth.signInWithEmailAndPassword(email, pswd)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            Toast.makeText(requireContext(), "Login success", Toast.LENGTH_SHORT)
                                .show()
                            findNavController().navigate(R.id.action_loginFragment_to_mainContentFragment)
                        } else {
                            if (it.exception.toString().contains("AuthEmail")) {
                                showMessage(requireContext(), "Please enter valid email")
                            } else if (it.exception.toString().contains("AuthInvalidCredential")) {
                                binding.passwordEdt.error = "Incorrect password"
                                showMessage(requireContext(), "Incorrect password")
                            } else if (it.exception.toString().contains("AuthInvalidUser")) {
                                showMessage(requireContext(), "User Doesn't exist")
                            } else {
                                showMessage(requireContext(), "Please check your credential")
                            }
                        }
                    }

            } else if (email.isEmpty() && pswd.isEmpty()) {
                binding.emailEdt.error = "Please enter your email"
                binding.passwordEdt.error = "Please enter your password"
            } else if (email.isEmpty()) {
                binding.emailEdt.error = "Please enter your email"
            } else {
                binding.passwordEdt.error = "Please enter your password"
            }
        }

        binding.registerTxt.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signUpFragment)
        }
    }
}