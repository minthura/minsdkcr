package tech.minthura.minsdk

import retrofit2.Response
import retrofit2.http.GET
import tech.minthura.minsdk.models.Mock

interface RetrofitApi {
    @GET("201")
    suspend fun getMock() : Mock
}