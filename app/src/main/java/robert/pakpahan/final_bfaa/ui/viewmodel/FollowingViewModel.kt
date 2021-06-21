package robert.pakpahan.final_bfaa.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import robert.pakpahan.final_bfaa.source.database.AppLocalDatabase
import robert.pakpahan.final_bfaa.source.repository.DetailProfileRepository

class FollowingViewModel (application: Application): AndroidViewModel(application){

    private val favoriteDao = AppLocalDatabase.getDatabase(application).favoriteDao()

    private val detailProfileRepository: DetailProfileRepository = DetailProfileRepository(favoriteDao = favoriteDao)

    fun getFollowingUser(username: String) = detailProfileRepository.getFollowing(username)

}