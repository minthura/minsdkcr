package tech.minthura.minsdk

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import tech.minthura.minsdk.models.Error
import tech.minthura.minsdk.models.Mock

class Session(private val minSDK: MinSDK) {
    companion object  {
        lateinit var instance : Session
        fun init(baseUrl: String) {
            instance = Session(MinSDK(baseUrl))
        }
    }

    fun getMock(scope: CoroutineScope, onSuccess: (Mock) -> Unit, onError : (error : Error) -> Unit): Job {
        return minSDK.getMock(scope, onSuccess, onError)
    }
}