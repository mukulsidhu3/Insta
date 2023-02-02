package com.example.insta.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.insta.databinding.FragmentMainContentBinding

class MainContentFragment : Fragment() {

    private lateinit var binding: FragmentMainContentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment

        binding = FragmentMainContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.mainToolbar.setNavigationOnClickListener {
            Toast.makeText(requireContext(), "NAV", Toast.LENGTH_SHORT).show()
        }
    }
}