package robert.pakpahan.consumerapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import robert.pakpahan.consumerapp.source.Repository.DetailProfileRepository
import robert.pakpahan.consumerapp.data.model.FavoriteModel

class DetailProfileFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val detailProfileRepository: DetailProfileRepository = DetailProfileRepository()

    fun insertFavoriteUsers(user: FavoriteModel, context: Context) = viewModelScope.launch(
        Dispatchers.IO){
        detailProfileRepository.insertFavorite(user = user, context = context)
    }

    fun deleteFavoriteUsers(user_id: Int, context: Context) = viewModelScope.launch(Dispatchers.IO){
        detailProfileRepository.deleteFavorite(user_id = user_id, context = context)
    }

}