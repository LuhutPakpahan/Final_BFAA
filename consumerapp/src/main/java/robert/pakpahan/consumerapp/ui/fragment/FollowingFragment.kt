package robert.pakpahan.consumerapp.ui.fragment

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
import robert.pakpahan.consumerapp.R
import robert.pakpahan.consumerapp.databinding.FragmentFollowingBinding
import robert.pakpahan.consumerapp.respond.utils.hide
import robert.pakpahan.consumerapp.respond.utils.show
import robert.pakpahan.consumerapp.source.database.SearchResponse
import robert.pakpahan.consumerapp.source.database.Status
import robert.pakpahan.consumerapp.ui.adapter.UserSearchAdapter
import robert.pakpahan.consumerapp.ui.viewmodel.FollowingFragmentViewModel

class FollowingFragment : Fragment(), UserSearchAdapter.Listener {

    private lateinit var followingFragmentViewModel: FollowingFragmentViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        followingFragmentViewModel = ViewModelProvider(this).get(FollowingFragmentViewModel::class.java)

        adapter = UserSearchAdapter(requireContext(), this@FollowingFragment)

        binding.apply {
            rvUserFollowing.layoutManager = LinearLayoutManager(context)
            rvUserFollowing.adapter = adapter
        }

        arguments?.apply {
            getString("username")?.let {
                observerDataUserFollowing(username = it)
            }
        }
    }

    private fun observerDataUserFollowing(username: String) {
        binding.apply {
            pbLoadFollowing.show()
            layoutEmptyDataFollowing.hide()
            layoutFailedFollowing.hide()
        }
        followingFragmentViewModel.getFollowingUser(username = username).observe(
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

    companion object {
        var TAG = FollowingFragment::class.java.simpleName

        fun newInstance(username: String): FollowingFragment {
            val bundle = Bundle().apply {
                putString("username", username)
            }

            return FollowingFragment()
                .apply {
                    arguments = bundle
                }
        }
    }

    override fun onUserClickListenre(view: View, data: SearchResponse) {
        Log.d(TAG, "onUserClickListenre: $data")
    }
}