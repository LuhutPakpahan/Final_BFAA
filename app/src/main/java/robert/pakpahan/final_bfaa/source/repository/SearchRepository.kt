package robert.pakpahan.final_bfaa.source.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import robert.pakpahan.final_bfaa.respond.response.Status
import robert.pakpahan.final_bfaa.respond.response.UserSearch
import robert.pakpahan.final_bfaa.source.network.api.ApiEndPoints
import robert.pakpahan.final_bfaa.source.network.api.apiRequest
import robert.pakpahan.final_bfaa.source.network.api.httpClient

class SearchRepository {

    var liveData = MutableLiveData<Status<UserSearch>>()
    var queryData = MutableLiveData<String>()

    fun getUserBySearch(query: String): LiveData<Status<UserSearch>> {

        if (query.isNotBlank()){
            queryData.value = query

            liveData = MutableLiveData<Status<UserSearch>>()

            val httpClient = httpClient()
            val apiRequest = apiRequest<ApiEndPoints>(httpClient)

            apiRequest.getSearchUser(query = queryData.value.toString()).enqueue(object :
                Callback<UserSearch> {
                override fun onResponse(call: Call<UserSearch>, response: Response<UserSearch>) {
                    when {
                        response.isSuccessful -> {
                            when {
                                response.body() != null -> {
                                    liveData.postValue(Status.success(data = response.body()))
                                }
                                else -> {
                                    liveData.postValue(Status.error(message = "Empty data", data = null))
                                }
                            }
                        } else -> {
                        liveData.postValue(Status.error(message = "Failed to search user", data = null))
                    }
                    }
                }

                override fun onFailure(call: Call<UserSearch>, t: Throwable) {
                    liveData.postValue(Status.error(message = "Error, ${t.message}", data = null))
                }
            })

        }
        return liveData
    }

    companion object {
        var TAG = SearchRepository::class.java.simpleName
    }
}