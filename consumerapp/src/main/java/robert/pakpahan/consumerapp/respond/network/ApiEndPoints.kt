package robert.pakpahan.consumerapp.respond.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import robert.pakpahan.consumerapp.source.database.SearchResponse

interface ApiEndPoints {

    @GET("users/{username}/followers")
    fun getUserFollowers(
        @Path("username") username: String
    ): Call<List<SearchResponse>>

    @GET("users/{username}/following")
    fun getUserFollowing(
        @Path("username") username: String
    ): Call<List<SearchResponse>>
}