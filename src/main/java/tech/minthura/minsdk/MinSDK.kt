package tech.minthura.minsdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tech.minthura.minsdk.models.Error
import tech.minthura.minsdk.models.Mock
import tech.minthura.minsdk.services.MockService

class MinSDK(baseUrl: String) {
    private val retrofitApi : RetrofitApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(RetrofitApi::class.java)
    private val mockService: MockService = MockService(retrofitApi)


    fun getMock(scope: CoroutineScope, onSuccess: (Mock) -> Unit, onError : (error : Error) -> Unit) : Job {
        return mockService.getMock(scope, onSuccess, onError)
    }
}