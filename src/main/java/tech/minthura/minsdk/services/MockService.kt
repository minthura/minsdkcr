package tech.minthura.minsdk.services

import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import tech.minthura.minsdk.RetrofitApi
import tech.minthura.minsdk.models.Error
import tech.minthura.minsdk.models.Mock

class MockService(private val retrofitApi: RetrofitApi) : BaseService() {

    fun getMock(scope: CoroutineScope, onSuccess: (Mock) -> Unit, onError : (error : Error) -> Unit): @NonNull Job {
        return coroutineLaunch(scope, { retrofitApi.getMock() }, onSuccess, onError)
    }
}