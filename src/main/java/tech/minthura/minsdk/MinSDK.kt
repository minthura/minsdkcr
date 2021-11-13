package tech.minthura.minsdk

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import tech.minthura.minsdk.models.Error
import tech.minthura.minsdk.models.Mock
import tech.minthura.minsdk.services.MockService

class MinSDK(baseUrl: String) {
    private val retrofitApi : RetrofitApi
    private val mockService: MockService

    init {
//        val interceptor = HttpLoggingInterceptor()
//        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
//        val client: OkHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()
        retrofitApi = Retrofit.Builder()
            .baseUrl(baseUrl)
//            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .build()
            .create(RetrofitApi::class.java)
        mockService = MockService(retrofitApi)
    }

    fun getMock(scope: CoroutineScope, onSuccess: (Mock) -> Unit, onError : (error : Error) -> Unit) : @NonNull Job {
        return mockService.getMock(scope, onSuccess, onError)
    }
}