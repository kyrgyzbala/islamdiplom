package kg.programm.programmingapp.ui.main

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import kg.programm.programmingapp.ui.home.HomeFragment
import kg.programm.programmingapp.ui.lecture.LecturesFragment
import kg.programm.programmingapp.ui.profile.ProfileFragment

class MainPagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()
            1 -> LecturesFragment()
            else -> ProfileFragment()
        }
    }
}