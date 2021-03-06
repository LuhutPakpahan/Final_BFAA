package robert.pakpahan.final_bfaa.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import robert.pakpahan.final_bfaa.R
import robert.pakpahan.final_bfaa.databinding.FragmentDetailProfileBinding
import robert.pakpahan.final_bfaa.respond.response.Status
import robert.pakpahan.final_bfaa.respond.response.UserDetail
import robert.pakpahan.final_bfaa.source.database.FavoriteModel
import robert.pakpahan.final_bfaa.source.utils.hide
import robert.pakpahan.final_bfaa.source.utils.show
import robert.pakpahan.final_bfaa.ui.adapter.ProfileViewPagerAdapter
import robert.pakpahan.final_bfaa.ui.viewmodel.DetailPofileViewModel

class DetailPofileFragment : Fragment(), Toolbar.OnMenuItemClickListener {

    private lateinit var detailPofileViewModel: DetailPofileViewModel
    private lateinit var binding: FragmentDetailProfileBinding

    private lateinit var profileViewPagerAdapter : ProfileViewPagerAdapter

    private val tabTitle = arrayOf("Following", "Follower")

    private var favoriteUserModel: FavoriteModel? = null
    private var userDetailModel: UserDetail? = null

    private var isFavorite: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_detail_profile, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        detailPofileViewModel = ViewModelProvider(this).get(DetailPofileViewModel::class.java)

        arguments?.let { bundle ->
            val username = DetailPofileFragmentArgs.fromBundle(bundle).username
            val data = DetailPofileFragmentArgs.fromBundle(bundle).userDetail

            profileViewPagerAdapter = ProfileViewPagerAdapter(fragment = this, username = username)

            if (data == null){
                observeDataDetailProfile(username = username)
            } else {
                val tempFavoriteModel = Gson().toJson(data)
                val favoriteModel = Gson().fromJson(tempFavoriteModel, FavoriteModel::class.java)
                favoriteUserModel = favoriteModel
                isFavorite = true
                setData(userDetail = data)
                showDetailContainer()
            }
        }

        binding.apply {
            tbFragmentDetailProfile.setNavigationOnClickListener {
                activity?.onBackPressed()
            }
            tbFragmentDetailProfile.setOnMenuItemClickListener(this@DetailPofileFragment)

            viewPagerDetailProfile.adapter = profileViewPagerAdapter

            fabFavoriteDetailProfile.setOnClickListener {
                when {
                    isFavorite -> {
                        removeFromUsersFavorite()
                    }
                    else -> {
                        addToUsersFavorite()
                    }
                }
            }

            TabLayoutMediator(tabLayoutDetailProfile, viewPagerDetailProfile,
                TabLayoutMediator.TabConfigurationStrategy {
                        tab, position -> tab.text = tabTitle[position]
                }).attach()

        }
    }

    private fun removeFromUsersFavorite() {
        when {
            favoriteUserModel != null -> {
                detailPofileViewModel.deleteFavoriteUsers(user_id = favoriteUserModel!!.id, context = requireContext())
                binding.fabFavoriteDetailProfile.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                favoriteUserModel = null
                isFavorite = false
            }
        }
    }

    private fun addToUsersFavorite() {
        when {
            userDetailModel != null -> {
                val userString = Gson().toJson(userDetailModel)
                favoriteUserModel = Gson().fromJson(userString, FavoriteModel::class.java)
                when {
                    favoriteUserModel != null -> {
                        binding.fabFavoriteDetailProfile.setImageResource(R.drawable.ic_baseline_favorite_24)
                        detailPofileViewModel.insertFavoriteUsers(user = favoriteUserModel!!, context = requireContext())
                        isFavorite = true
                    }
                }
            }
        }
    }

    private fun observeIsUserFavorite() {
        binding.apply {
            userDetailModel?.let {
                detailPofileViewModel.selectFavoriteUser(user_id = it.id, context = requireContext()).observe(
                    viewLifecycleOwner, Observer { status ->
                        isFavorite = when(status.status){
                            Status.Type.SUCCESS -> {
                                favoriteUserModel = status.data
                                fabFavoriteDetailProfile.setImageResource(R.drawable.ic_baseline_favorite_24)
                                true
                            }
                            Status.Type.FAILED -> {
                                fabFavoriteDetailProfile.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                                false
                            }
                        }
                    }
                )
            }
        }
    }

    private fun observeDataDetailProfile(username: String) {
        detailPofileViewModel.getDetailProfile(username = username).observe(
            viewLifecycleOwner, Observer { status ->
                when(status.status){
                    Status.Type.SUCCESS -> {
                        status.data.apply {
                            setData(this!!)
                            showDetailContainer()
                        }
                    }
                    Status.Type.FAILED -> {
                    }
                }
            }
        )
    }

    private fun showDetailContainer() {
        binding.apply {
            containerDetailProfile.show()
            fabFavoriteDetailProfile.show()
            viewPagerDetailProfile.show()
            tabLayoutDetailProfile.show()
            pbLoadDetailProfile.hide()
        }
    }

    private fun setData(userDetail: UserDetail) {
        binding.apply {
            tvNameDetailProfile.text = userDetail.name
            tvUsernameDetailProfile.text = userDetail.login
            tvLocationDetailProfile.text = userDetail.location
            tvFollowerCountDetailProfile.text = getString(R.string.c_follower, userDetail.followers)
            tvFollowingDetailProfile.text = getString(R.string.c_following, userDetail.following)
            tvBioDetailProfile.text = userDetail.bio
            Glide.with(requireContext())
                .load(userDetail.avatarUrl)
                .circleCrop()
                .into(ivUserDetailProfile)
        }

        userDetailModel = userDetail
        observeIsUserFavorite()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            requireView().findNavController())
    }
}