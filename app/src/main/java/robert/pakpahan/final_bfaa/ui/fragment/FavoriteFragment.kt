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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import robert.pakpahan.final_bfaa.R
import robert.pakpahan.final_bfaa.databinding.FragmentFavoriteBinding
import robert.pakpahan.final_bfaa.respond.response.UserDetail
import robert.pakpahan.final_bfaa.source.database.FavoriteModel
import robert.pakpahan.final_bfaa.source.utils.hide
import robert.pakpahan.final_bfaa.source.utils.show
import robert.pakpahan.final_bfaa.ui.adapter.FavoriteUserAdapter
import robert.pakpahan.final_bfaa.ui.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment(), Toolbar.OnMenuItemClickListener,
    FavoriteUserAdapter.Listener {

    private lateinit var binding: FragmentFavoriteBinding

    private lateinit var favoriteViewModel: FavoriteViewModel

    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_favorite, container, false)

        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        adapter = FavoriteUserAdapter(requireContext(), this)

        binding.apply {
            tbFragmentFavorite.setNavigationOnClickListener {
                activity?.onBackPressed()
            }

            tbFragmentFavorite.setOnMenuItemClickListener(this@FavoriteFragment)

            rvUserFavorite.layoutManager = LinearLayoutManager(requireContext())
            rvUserFavorite.adapter = adapter

        }

        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        observeFavoriteUsersList()
    }

    private fun observeFavoriteUsersList() {
        binding.apply {
            favoriteViewModel.favoriteUsersList(context = requireContext()).observe(
                viewLifecycleOwner, Observer { favoriteUsers ->
                    favoriteUsers.let {
                        binding.apply {
                            pbLoadFavorite.hide()
                            if (it.isNotEmpty()){
                                adapter.setFavoritesData(it)
                            } else {
                                adapter.notifyDataSetChanged()
                                layoutEmptyDataFavorite.show()
                            }
                        }
                    }
                })
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            requireView().findNavController())
    }

    override val FavoriteFragmentDirections: Any
        get() = TODO("Not yet implemented")

    override fun onFavoriteClickListener(view: View, data: FavoriteModel) {
        val tempUserDetail = Gson().toJson(data)
        val userDetail = Gson().fromJson(tempUserDetail, UserDetail::class.java)
        val navigation = robert.pakpahan.final_bfaa.ui.fragment.FavoriteFragmentDirections .actionFavoriteFragmentToDetailProfileFragment(userDetail.login, userDetail)
        view.findNavController().navigate(navigation)
    }
}


