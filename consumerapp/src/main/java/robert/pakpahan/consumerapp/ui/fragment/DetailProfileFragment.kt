package robert.pakpahan.consumerapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import retrofit2.http.Tag
import robert.pakpahan.consumerapp.R
import robert.pakpahan.consumerapp.databinding.FragmentDetailProfileBinding
import robert.pakpahan.consumerapp.respond.utils.hide
import robert.pakpahan.consumerapp.respond.utils.show
import robert.pakpahan.consumerapp.data.model.FavoriteModel
import robert.pakpahan.consumerapp.ui.adapter.ProfileViewPagerAdapter
import robert.pakpahan.consumerapp.ui.viewmodel.DetailProfileFragmentViewModel

class DetailProfileFragment : Fragment() {

    private lateinit var detailProfileFragmentViewModel: DetailProfileFragmentViewModel
    private lateinit var binding: FragmentDetailProfileBinding

    private lateinit var profileViewPagerAdapter : ProfileViewPagerAdapter

    private val tabTitle = arrayOf("Following", "Follower")

    private var favoriteUserModel: FavoriteModel? = null

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
        detailProfileFragmentViewModel = ViewModelProvider(this).get(DetailProfileFragmentViewModel::class.java)

        arguments?.let { bundle ->
            val username = DetailProfileFragmentArgs.fromBundle(
                bundle
            ).username
            val data = DetailProfileFragmentArgs.fromBundle(
                bundle
            ).userDetail

            profileViewPagerAdapter = ProfileViewPagerAdapter(fragment = this, username = username!!)

            val tempFavoriteModel = Gson().toJson(data)
            val favoriteModel = Gson().fromJson(tempFavoriteModel, FavoriteModel::class.java)
            favoriteUserModel = favoriteModel
            setData(favoriteModel = data!!)
            showDetailContainer()
            isUsersFavorite()
        }

        binding.apply {
            tbFragmentDetailProfile.setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            viewPagerDetailProfile.adapter = profileViewPagerAdapter

            fabFavoriteDetailProfile.setOnClickListener {
                when {
                    favoriteUserModel != null -> {
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
                detailProfileFragmentViewModel.deleteFavoriteUsers(user_id = favoriteUserModel!!.id, context = requireContext())
                favoriteUserModel = null
                isUsersFavorite()
            }
        }
    }

    private fun addToUsersFavorite() {
        when {
            favoriteUserModel != null -> {
                detailProfileFragmentViewModel.insertFavoriteUsers(user = favoriteUserModel!!, context = requireContext())
                isUsersFavorite()
            }
        }
    }

    private fun isUsersFavorite() {
        binding.apply {
            when {
                favoriteUserModel != null -> {
                    fabFavoriteDetailProfile.setImageResource(R.drawable.ic_baseline_favorite_24)
                }
                else -> {
                    fabFavoriteDetailProfile.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
        }
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

    private fun setData(favoriteModel: FavoriteModel) {
        binding.apply {
            tvNameDetailProfile.text = favoriteModel.name
            tvUsernameDetailProfile.text = favoriteModel.login
            tvLocationDetailProfile.text = favoriteModel.location
            tvFollowerCountDetailProfile.text = getString(R.string.c_follower, favoriteModel.followers)
            tvFollowingDetailProfile.text = getString(R.string.c_following, favoriteModel.following)
            tvBioDetailProfile.text = favoriteModel.bio
            Glide.with(requireContext())
                .load(favoriteModel.avatarUrl)
                .circleCrop()
                .into(ivUserDetailProfile)
        }

    }

    companion object {
        var TAG = DetailProfileFragment::class.java.simpleName
    }

}