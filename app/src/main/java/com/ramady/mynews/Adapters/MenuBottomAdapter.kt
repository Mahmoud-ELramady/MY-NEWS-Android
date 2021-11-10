package com.ramady.mynews.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class MenuBottomAdapter(val fragment: ArrayList<Fragment>, fragmentM: FragmentManager)
    : FragmentStatePagerAdapter(fragmentM, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getCount(): Int {
        return fragment.size
    }

    override fun getItem(position: Int): Fragment {
        return fragment[position]
    }

}