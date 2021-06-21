package robert.pakpahan.consumerapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import robert.pakpahan.consumerapp.source.Repository.DetailProfileRepository

class FollowerFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val detailProfileRepository : DetailProfileRepository = DetailProfileRepository()

    fun getFollowerUser(username: String) = detailProfileRepository.getFollower(username = username)

}