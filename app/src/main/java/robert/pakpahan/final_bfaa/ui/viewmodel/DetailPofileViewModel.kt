package robert.pakpahan.final_bfaa.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import robert.pakpahan.final_bfaa.source.database.AppLocalDatabase
import robert.pakpahan.final_bfaa.source.database.FavoriteModel
import robert.pakpahan.final_bfaa.source.repository.DetailProfileRepository

class DetailPofileViewModel (application: Application): AndroidViewModel(application) {

    private val favoriteDao = AppLocalDatabase.getDatabase(application).favoriteDao()

    private val detailProfileRepository: DetailProfileRepository = DetailProfileRepository(favoriteDao = favoriteDao)

    fun getDetailProfile(username: String) = detailProfileRepository.getByUsername(username = username)

    fun insertFavoriteUsers(user: FavoriteModel, context: Context) = viewModelScope.launch(
        Dispatchers.IO){
        detailProfileRepository.insertFavorite(user = user, context = context)
    }

    fun deleteFavoriteUsers(user_id: Int, context: Context) = viewModelScope.launch(Dispatchers.IO){
        detailProfileRepository.deleteFavorite(user_id = user_id, context = context)
    }

    fun selectFavoriteUser(user_id: Int, context: Context) = detailProfileRepository.getDetailUser(user_id = user_id, context = context)
}