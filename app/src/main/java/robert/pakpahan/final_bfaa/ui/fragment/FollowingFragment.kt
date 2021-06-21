package robert.pakpahan.final_bfaa.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import robert.pakpahan.final_bfaa.R
import robert.pakpahan.final_bfaa.databinding.FragmentFollowingBinding
import robert.pakpahan.final_bfaa.respond.response.SearchResponse
import robert.pakpahan.final_bfaa.respond.response.Status
import robert.pakpahan.final_bfaa.source.utils.hide
import robert.pakpahan.final_bfaa.source.utils.show
import robert.pakpahan.final_bfaa.ui.adapter.UserSearchAdapter
import robert.pakpahan.final_bfaa.ui.viewmodel.FollowingViewModel



class FollowingFragment : Fragment(), UserSearchAdapter.Listener {

    private lateinit var followingViewModel: FollowingViewModel
    private lateinit var binding: FragmentFollowingBinding
    private lateinit var adapter: UserSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_following, container, false)

        return binding.root
    }

    private fun observerDataUserFollowing(username: String) {
        binding.apply {
            pbLoadFollowing.show()
            layoutEmptyDataFollowing.hide()
            layoutFailedFollowing.hide()
        }
        followingViewModel.getFollowingUser(username = username).observe(
            viewLifecycleOwner, Observer { status ->
                when(status.status){
                    Status.Type.SUCCESS -> {
                        status.data.apply {
                            if (this!!.isNotEmpty()){
                                binding.apply {
                                    pbLoadFollowing.hide()
                                }
                                adapter.setUserSearchData(this)
                            }else {
                                binding.apply {
                                    pbLoadFollowing.hide()
                                    layoutEmptyDataFollowing.show()
                                }
                            }
                        }
                    }
                    Status.Type.FAILED -> {
                        binding.apply {
                            pbLoadFollowing.hide()
                            layoutFailedFollowing.show()
                        }
                    }
                }
            }
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        adapter = UserSearchAdapter(requireContext(), this@FollowingFragment)

        binding.apply {
            rvUserFollowing.layoutManager = LinearLayoutManager(context)
            rvUserFollowing.adapter = adapter
        }

        followingViewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)

        arguments?.apply {
            getString("username")?.let {
                observerDataUserFollowing(username = it)
            }
        }
    }

    companion object {
        var TAG = FollowingFragment::class.java.name

        fun newInstance(username: String): FollowingFragment {
            val bundle = Bundle().apply {
                putString("username", username)
            }

            return FollowingFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onUserClickListenre(view: View, data: SearchResponse) {
        Log.d(TAG, "onUserClickListenre: $data")
    }
}