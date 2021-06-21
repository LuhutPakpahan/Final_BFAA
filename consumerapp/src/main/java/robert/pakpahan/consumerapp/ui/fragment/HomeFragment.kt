package robert.pakpahan.consumerapp.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
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
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_home.*
import robert.pakpahan.consumerapp.R
import robert.pakpahan.consumerapp.databinding.FragmentHomeBinding
import robert.pakpahan.consumerapp.respond.utils.hide
import robert.pakpahan.consumerapp.respond.utils.show
import robert.pakpahan.consumerapp.data.model.FavoriteModel
import robert.pakpahan.consumerapp.ui.adapter.FavoriteUserAdapter
import robert.pakpahan.consumerapp.ui.viewmodel.HomeFragmentViewModel

class HomeFragment : Fragment(), FavoriteUserAdapter.Listener, Toolbar.OnMenuItemClickListener {

    private lateinit var homeFragmentViewModel: HomeFragmentViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter : FavoriteUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {
        adapter = FavoriteUserAdapter(
            requireContext(),
            this@HomeFragment
        )

        binding.apply {
            tbFragmentHome.setOnMenuItemClickListener(this@HomeFragment)

            rvUserListHome.layoutManager = LinearLayoutManager(context)
            rvUserListHome.adapter = adapter
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        homeFragmentViewModel = ViewModelProvider(this).get(HomeFragmentViewModel::class.java)
        observeFavoriteUsersList()
    }

    private fun observeFavoriteUsersList() {
        binding.apply {
            pbLoadHome.show()
            layoutEmptyDataHome.hide()
            homeFragmentViewModel.favoriteUsersList(context = requireContext()).observe(
                viewLifecycleOwner, Observer { favoriteUsers ->
                    favoriteUsers.let {
                        binding.apply {
                            pbLoadHome.hide()
                            if (it.isNotEmpty()){
                                adapter.setUserSearchData(it)
                            } else {
                                adapter.notifyDataSetChanged()
                                layoutEmptyDataHome.show()
                            }
                        }
                    }
                })
        }
    }

    companion object {
        var TAG = HomeFragment::class.java.simpleName
    }

    override fun onUserClickListenre(view: View, data: FavoriteModel) {
        val navigation =
            HomeFragmentDirections.actionHomeFragmentToDetailProfileFragment(
                data.login,
                data
            )
        view.findNavController().navigate(navigation)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId){
            R.id.menuChangeLanguage -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                true
            }
            else -> false
        }
    }
}