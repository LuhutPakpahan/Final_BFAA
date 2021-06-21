package robert.pakpahan.consumerapp.source.Repository

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import robert.pakpahan.consumerapp.respond.utils.FAVORITE_URI
import robert.pakpahan.consumerapp.respond.utils.toListFavoriteModel
import robert.pakpahan.consumerapp.data.model.FavoriteModel

class FavoriteRepository () {
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