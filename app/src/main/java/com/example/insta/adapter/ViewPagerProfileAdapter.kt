package com.example.insta.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.insta.fragments.CreateFragment
import com.example.insta.fragments.PostsFragment

class ViewPagerProfileAdapter(
    manager: FragmentManager, lifecycle: Lifecycle
) : FragmentStateAdapter(manager, lifecycle) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            PostsFragment()
        } else {
            CreateFragment()
        }
    }
}