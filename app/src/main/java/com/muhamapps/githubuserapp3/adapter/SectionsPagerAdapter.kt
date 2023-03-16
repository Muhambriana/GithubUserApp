package com.muhamapps.githubuserapp3.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.muhamapps.githubuserapp3.fragment.FollowersFragment
import com.muhamapps.githubuserapp3.fragment.FollowingFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    var username: String? = null

    override fun createFragment(position: Int): Fragment {


        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = username?.let { FollowersFragment.newInstance(it) }
            1 -> fragment = username?.let { FollowingFragment.newInstance(it) }
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int {
        return 2
    }

}
