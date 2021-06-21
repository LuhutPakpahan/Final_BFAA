package robert.pakpahan.final_bfaa.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import robert.pakpahan.final_bfaa.ui.fragment.FollowerFragment
import robert.pakpahan.final_bfaa.ui.fragment.FollowingFragment

class ProfileViewPagerAdapter (fragment: Fragment, private var username: String) : FragmentStateAdapter(fragment) {

    private val tabCount = 2

    override fun getItemCount(): Int = tabCount

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> {
                FollowingFragment.newInstance(username = username)
            }
            1 -> {
                FollowerFragment.newInstance(username = username)
            }
            else -> FollowingFragment.newInstance(username = username)
        }
    }

}