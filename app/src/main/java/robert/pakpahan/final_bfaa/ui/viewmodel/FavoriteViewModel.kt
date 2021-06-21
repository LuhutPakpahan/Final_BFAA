package robert.pakpahan.final_bfaa.ui.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import robert.pakpahan.final_bfaa.source.database.FavoriteModel
import robert.pakpahan.final_bfaa.source.repository.FavoriteRepository

class FavoriteViewModel (application: Application): AndroidViewModel(application) {

    private val repository: FavoriteRepository = FavoriteRepository()

    fun favoriteUsersList(context: Context): LiveData<List<FavoriteModel>> = repository.getFavoriteUsersList(context = context)
}