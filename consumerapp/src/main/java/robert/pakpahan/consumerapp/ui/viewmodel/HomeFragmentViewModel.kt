package robert.pakpahan.consumerapp.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import robert.pakpahan.consumerapp.source.Repository.FavoriteRepository
import robert.pakpahan.consumerapp.data.model.FavoriteModel

class HomeFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val repository: FavoriteRepository =
        FavoriteRepository()

    fun favoriteUsersList(context: Context): LiveData<List<FavoriteModel>> = repository.getFavoriteUsersList(context = context)
}