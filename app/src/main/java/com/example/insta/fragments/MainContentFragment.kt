package com.example.insta.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.insta.R
import com.example.insta.databinding.FragmentMainContentBinding
import com.example.insta.util.InstaApplication
import com.google.firebase.auth.FirebaseAuth

class MainContentFragment : Fragment(), MenuProvider {

    private lateinit var binding: FragmentMainContentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentMainContentBinding.inflate(inflater, container, false)

        //toolbar
        binding.mainToolbar.inflateMenu(R.menu.app_bar_menu)
        (activity as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
        (activity as AppCompatActivity).supportActionBar?.show()
        (activity as AppCompatActivity).supportActionBar?.title= ""
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.CREATED)

        auth = ((activity as FragmentActivity).application as InstaApplication).fAuth

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navHostFragment =
            childFragmentManager.findFragmentById(R.id.nestedFragmentConV) as NavHostFragment

        navController = navHostFragment.navController

        setupWithNavController(binding.bottomNavV, navController)
        binding.bottomNavV
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.app_bar_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.logout -> {
                auth.signOut()
                findNavController().navigate(R.id.action_mainContentFragment_to_loginFragment)
                Toast.makeText(requireContext(), "LogOut", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                false
            }
        }
    }
}