package robert.pakpahan.final_bfaa.source.repository

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import robert.pakpahan.final_bfaa.respond.response.SearchResponse
import robert.pakpahan.final_bfaa.respond.response.Status
import robert.pakpahan.final_bfaa.respond.response.UserDetail
import robert.pakpahan.final_bfaa.source.database.FavoriteDao
import robert.pakpahan.final_bfaa.source.database.FavoriteModel
import robert.pakpahan.final_bfaa.source.network.api.ApiEndPoints
import robert.pakpahan.final_bfaa.source.network.api.apiRequest
import robert.pakpahan.final_bfaa.source.network.api.httpClient
import robert.pakpahan.final_bfaa.source.utils.FAVORITE_URI
import robert.pakpahan.final_bfaa.source.utils.toContentValues
import robert.pakpahan.final_bfaa.source.utils.toFavoriteModel

class DetailProfileRepository(private val favoriteDao: FavoriteDao) {

    fun getByUsername(username: String): LiveData<Status<UserDetail>> {

        val detailLiveData = MutableLiveData<Status<UserDetail>>()

        val httpClient =
            httpClient()
        val apiRequest =
            apiRequest<ApiEndPoints>(
                httpClient
            )

        apiRequest.getSearchByUserName(username = username).enqueue(object: Callback<UserDetail> {
            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                when {
                    response.isSuccessful -> {
                        when {
                            response.body() != null -> {
                                detailLiveData.postValue(
                                    Status.success(
                                        data = response.body()
                                    )
                                )
                            } else -> {
                            detailLiveData.postValue(
                                Status.error(
                                    message = "Empty data",
                                    data = null
                                )
                            )
                        }
                        }
                    } else -> {
                    detailLiveData.postValue(
                        Status.error(
                            message = "Failed to search user",
                            data = null
                        )
                    )
                }
                }
            }

            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                detailLiveData.postValue(
                    Status.error(
                        message = "Error, ${t.message}",
                        data = null
                    )
                )
            }
        })

        return detailLiveData
    }

    fun getFollower(username: String): LiveData<Status<List<SearchResponse>>> {
        val followerLiveData = MutableLiveData<Status<List<SearchResponse>>>()

        val httpClient = httpClient()
        val apiRequest = apiRequest<ApiEndPoints>(httpClient)

        apiRequest.getUserFollowers(username = username).enqueue(object :
            Callback<List<SearchResponse>> {
            override fun onResponse(
                call: Call<List<SearchResponse>>,
                response: Response<List<SearchResponse>>
            ) {
                when {
                    response.isSuccessful -> {
                        when {
                            response.body() != null -> {
                                followerLiveData.postValue(Status.success(data = response.body()))
                            }
                            else -> {
                                followerLiveData.postValue(Status.error(message = "Empty data", data = null))
                            }
                        }
                    }
                    else -> {
                        followerLiveData.postValue(Status.error(message = "Failed to search user", data = null))
                    }
                }
            }

            override fun onFailure(call: Call<List<SearchResponse>>, t: Throwable) {
                followerLiveData.postValue(Status.error(message = "Error ${t.message}", data = null))
            }
        })

        return followerLiveData
    }

    fun getFollowing(username: String): LiveData<Status<List<SearchResponse>>> {
        val followingLiveData = MutableLiveData<Status<List<SearchResponse>>>()

        val httpClient = httpClient()
        val apiRequest = apiRequest<ApiEndPoints>(httpClient)

        apiRequest.getUserFollowing(username = username).enqueue(object :
            Callback<List<SearchResponse>> {
            override fun onResponse(
                call: Call<List<SearchResponse>>,
                response: Response<List<SearchResponse>>
            ) {
                when {
                    response.isSuccessful -> {
                        when {
                            response.body() != null -> {
                                followingLiveData.postValue(Status.success(data = response.body()))
                            }
                            else -> {
                                followingLiveData.postValue(Status.error(message = "Empty data", data = null))
                            }
                        }
                    }
                    else -> {
                        followingLiveData.postValue(Status.error(message = "Failed to search user", data = null))
                    }
                }
            }

            override fun onFailure(call: Call<List<SearchResponse>>, t: Throwable) {
                followingLiveData.postValue(Status.error(message = "Error ${t.message}", data = null))
            }
        })

        return followingLiveData
    }

    fun insertFavorite(user: FavoriteModel, context: Context){
        context.contentResolver.insert(FAVORITE_URI.toUri(), user.toContentValues())
    }

    fun deleteFavorite(user_id: Int, context: Context){

        val uri = "$FAVORITE_URI/$user_id".toUri()

        context.contentResolver.delete(uri, null, null)
    }

    fun getDetailUser(user_id: Int, context: Context): LiveData<Status<FavoriteModel>> {
        val liveData = MutableLiveData<Status<FavoriteModel>>()

        val uri = "$FAVORITE_URI/$user_id".toUri()
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {

            if (cursor.moveToFirst()){
                liveData.postValue(Status.success(data = it.toFavoriteModel()))
            }else {
                liveData.postValue(Status.error(message = "Not user favorited", data = null))
            }

            cursor.close()
        }

        return liveData
    }

}