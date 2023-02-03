package com.example.insta.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.insta.R
import com.example.insta.adapter.ViewPagerProfileAdapter
import com.example.insta.databinding.FragmentProfileBinding
import com.example.insta.util.InstaApplication
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var viewPagerProfileAdapter: ViewPagerProfileAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        auth = ((activity as FragmentActivity).application as InstaApplication).fAuth
        database = ((activity as FragmentActivity).application as InstaApplication).fDatabase
        viewPagerProfileAdapter = ViewPagerProfileAdapter(childFragmentManager, lifecycle)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tabLayoutAndViewPager()

        val dataList = mutableListOf<String>()
        val postList = mutableListOf<String>()

        val dataRef = database.reference.child(auth.uid!!)

        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                dataList.clear()
                for (s in snapshot.children) {
                    dataList.add(s.value.toString())
                }
                try {
                    binding.totalFollowersTxt.text = dataList[1]
                    binding.totalFollowingText.text = dataList[2]
                    binding.userNameTxt.text = dataList[3]
                    Glide.with(requireContext()).load(dataList[4]).into(binding.profilePicIv)
                } catch (e: java.lang.Exception) {
                    e.stackTrace
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })

        val postRef = database.reference.child(auth.uid!!).child("posts")

        postRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                postList.clear()
                for (s in snapshot.children) {
                    postList.add(s.value.toString())
                }
                binding.totalPostsTxt.text = (postList.size-1).toString()
            }

            override fun onCancelled(error: DatabaseError) {}
        })

    }

    private fun tabLayoutAndViewPager() {
        binding.tabLayoutForPosts.addTab(
            binding.tabLayoutForPosts.newTab().setIcon(R.drawable.round_photo_library_24)
        )

        binding.tabLayoutForPosts.addTab(
            binding.tabLayoutForPosts.newTab().setIcon(R.drawable.round_add_to_photos_24)
        )

        binding.viewPagerForPosts.adapter = viewPagerProfileAdapter

        binding.tabLayoutForPosts.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    binding.viewPagerForPosts.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.viewPagerForPosts.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tabLayoutForPosts.selectTab(binding.tabLayoutForPosts.getTabAt(position))
            }
        })
    }
}