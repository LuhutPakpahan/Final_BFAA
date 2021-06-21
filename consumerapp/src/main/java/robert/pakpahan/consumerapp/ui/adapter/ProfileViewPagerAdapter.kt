package robert.pakpahan.consumerapp.ui.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import robert.pakpahan.consumerapp.ui.fragment.FollowerFragment
import robert.pakpahan.consumerapp.ui.fragment.FollowingFragment

class ProfileViewPagerAdapter(fragment: Fragment, private var username: String) : FragmentStateAdapter(fragment) {

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