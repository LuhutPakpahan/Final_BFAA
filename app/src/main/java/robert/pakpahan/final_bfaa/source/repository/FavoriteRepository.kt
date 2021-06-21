package robert.pakpahan.final_bfaa.source.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import robert.pakpahan.final_bfaa.source.database.FavoriteModel
import robert.pakpahan.final_bfaa.source.utils.FAVORITE_URI
import robert.pakpahan.final_bfaa.source.utils.toListFavoriteModel

class FavoriteRepository {

    fun getFavoriteUsersList(context: Context): LiveData<List<FavoriteModel>> {
        val liveData = MutableLiveData<List<FavoriteModel>>()

        val cursor = context.contentResolver.query(FAVORITE_URI.toUri(), null, null, null, null)
        cursor?.let {

            liveData.postValue(it.toListFavoriteModel())

            cursor.close()
        }

        return liveData
    }

}