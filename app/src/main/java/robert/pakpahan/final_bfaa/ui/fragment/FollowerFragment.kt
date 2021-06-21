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
import robert.pakpahan.final_bfaa.databinding.FragmentFollowerBinding
import robert.pakpahan.final_bfaa.respond.response.SearchResponse
import robert.pakpahan.final_bfaa.respond.response.Status
import robert.pakpahan.final_bfaa.source.utils.hide
import robert.pakpahan.final_bfaa.source.utils.show
import robert.pakpahan.final_bfaa.ui.adapter.UserSearchAdapter
import robert.pakpahan.final_bfaa.ui.viewmodel.FollowerViewModel

class FollowerFragment : Fragment(), UserSearchAdapter.Listener {

    private lateinit var followerViewModel: FollowerViewModel
    private lateinit var binding: FragmentFollowerBinding
    private lateinit var adapter: UserSearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_follower, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()
    }

    private fun init() {
        adapter = UserSearchAdapter(requireContext(), this@FollowerFragment)

        binding.apply {
            rvUserFollower.layoutManager = LinearLayoutManager(context)
            rvUserFollower.adapter = adapter
        }

        followerViewModel = ViewModelProvider(this).get(FollowerViewModel::class.java)

        arguments?.apply {

            getString("username")?.let {
                observeDataUserFollower(username = it)
            }

        }
    }

    private fun observeDataUserFollower(username: String) {
        binding.apply {
            pbLoadFollower.show()
            layoutFailedFollower.hide()
            layoutEmptyDataFollower.hide()
        }
        followerViewModel.getFollowerUser(username = username).observe(
            viewLifecycleOwner, Observer { status ->
                when(status.status){
                    Status.Type.SUCCESS -> {
                        status.data.apply {
                            if (this!!.isNotEmpty()){
                                binding.pbLoadFollower.hide()
                                adapter.setUserSearchData(this)
                            }else {
                                binding.apply {
                                    pbLoadFollower.hide()
                                    layoutEmptyDataFollower.show()
                                }
                            }
                        }
                    }
                    Status.Type.FAILED -> {
                        binding.apply {
                            pbLoadFollower.hide()
                            layoutFailedFollower.show()
                        }
                    }
                }
            }
        )
    }

    companion object {
        var TAG = FollowerFragment::class.java.name

        fun newInstance(username: String): FollowerFragment {
            val bundle = Bundle().apply {
                putString("username", username)
            }

            return FollowerFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onUserClickListenre(view: View, data: SearchResponse) {
        Log.d(TAG, "onUserClickListenre: $data")
    }
}