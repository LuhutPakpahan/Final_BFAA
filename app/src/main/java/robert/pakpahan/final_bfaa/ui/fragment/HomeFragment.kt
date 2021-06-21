package robert.pakpahan.final_bfaa.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.recyclerview.widget.LinearLayoutManager
import robert.pakpahan.final_bfaa.R
import robert.pakpahan.final_bfaa.databinding.FragmentHomeBinding
import robert.pakpahan.final_bfaa.respond.response.SearchResponse
import robert.pakpahan.final_bfaa.respond.response.Status
import robert.pakpahan.final_bfaa.source.utils.hide
import robert.pakpahan.final_bfaa.source.utils.hideKeyboard
import robert.pakpahan.final_bfaa.source.utils.show
import robert.pakpahan.final_bfaa.ui.adapter.UserSearchAdapter
import robert.pakpahan.final_bfaa.ui.viewmodel.HomeViewModel

class HomeFragment : Fragment(), Toolbar.OnMenuItemClickListener, UserSearchAdapter.Listener {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter : UserSearchAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_home, container, false)

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        observeDataUserSearch(binding.edtSearchHome.text.toString())
    }

    private fun searchUser() {
        binding.apply {
            if (!edtSearchHome.text.isBlank()){
                rvUserListHome.hide()
                layoutBeginSearchHome.hide()
                layoutEmptyDataHome.hide()
                layoutFailed.hide()
                pbLoadHome.show()
                observeDataUserSearch(edtSearchHome.text.toString())
            }
        }
    }

    private fun observeDataUserSearch(query: String) {
        homeViewModel.getUserBySearch(query = query).observe(
            viewLifecycleOwner, Observer {  status ->
                when (status.status) {
                    Status.Type.SUCCESS -> {
                        binding.apply {
                            layoutBeginSearchHome.hide()
                            pbLoadHome.hide()
                        }
                        status.data?.apply {
                            if (this.totalCount > 0) {
                                adapter.setUserSearchData(this.items)
                                binding.apply {
                                    layoutBeginSearchHome.hide()
                                    rvUserListHome.show()
                                }
                            }else {
                                binding.apply {
                                    layoutEmptyDataHome.show()
                                }
                            }
                        }
                    }
                    Status.Type.FAILED -> {
                        binding.apply {
                            layoutBeginSearchHome.hide()
                            pbLoadHome.hide()
                            layoutFailed.show()
                        }
                    }
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        init()

        super.onViewCreated(view, savedInstanceState)
    }

    private fun init() {

        adapter = UserSearchAdapter(requireContext(), this@HomeFragment)

        binding.apply {
            tbFragmentHome.setOnMenuItemClickListener(this@HomeFragment)

            ivSearchHome.setOnClickListener {
                hideKeyboard()
                searchUser()
            }

            edtSearchHome.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchUser()
                }
                false
            }

            rvUserListHome.layoutManager = LinearLayoutManager(context)
            rvUserListHome.adapter = adapter
        }

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return NavigationUI.onNavDestinationSelected(item!!,
            requireView().findNavController())
    }

    override fun onUserClickListenre(view: View, data: SearchResponse) {
        val navigation = HomeFragmentDirections.actionHomeFragmentToDetailProfileFragment(data.login, null)
        view.findNavController().navigate(navigation)
    }
}