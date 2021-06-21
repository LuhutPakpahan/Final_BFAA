package robert.pakpahan.consumerapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import robert.pakpahan.consumerapp.source.Repository.DetailProfileRepository

class FollowingFragmentViewModel(application: Application): AndroidViewModel(application){

    private val detailProfileRepository: DetailProfileRepository = DetailProfileRepository()

    fun getFollowingUser(username: String) = detailProfileRepository.getFollowing(username)

}