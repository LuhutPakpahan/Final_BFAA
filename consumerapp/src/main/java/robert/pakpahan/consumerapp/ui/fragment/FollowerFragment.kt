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
import robert.pakpahan.consumerapp.databinding.FragmentFollowerBinding
import robert.pakpahan.consumerapp.respond.utils.hide
import robert.pakpahan.consumerapp.respond.utils.show
import robert.pakpahan.consumerapp.source.database.SearchResponse
import robert.pakpahan.consumerapp.source.database.Status
import robert.pakpahan.consumerapp.ui.adapter.UserSearchAdapter
import robert.pakpahan.consumerapp.ui.viewmodel.FollowerFragmentViewModel

class FollowerFragment : Fragment(), UserSearchAdapter.Listener {

    private lateinit var followerFragmentViewModel: FollowerFragmentViewModel
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
        followerFragmentViewModel = ViewModelProvider(this).get(FollowerFragmentViewModel::class.java)

        arguments?.apply {

            getString("username")?.let {
                observeDataUserFollower(username = it)
            }
        }

        adapter = UserSearchAdapter(context = requireContext(), listener = this@FollowerFragment)

        binding.apply {
            rvUserFollower.layoutManager = LinearLayoutManager(context)
            rvUserFollower.adapter = adapter
        }
    }

    private fun observeDataUserFollower(username: String) {
        binding.apply {
            pbLoadFollower.show()
            layoutFailedFollower.hide()
            layoutEmptyDataFollower.hide()
        }
        followerFragmentViewModel.getFollowerUser(username = username).observe(
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
        fun newInstance(username: String): FollowerFragment {
            val bundle = Bundle().apply {
                putString("username", username)
            }

            return FollowerFragment()
                .apply {
                    arguments = bundle
                }
        }

        var TAG = FollowerFragment::class.java.simpleName
    }

    override fun onUserClickListenre(view: View, data: SearchResponse) {
        Log.d(TAG, "onUserClickListenre: $data")
    }
}