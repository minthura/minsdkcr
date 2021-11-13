package tech.minthura.minsdk.services

import android.util.Log
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonDataException
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.rxjava3.annotations.NonNull
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Response
import tech.minthura.minsdk.models.Error
import tech.minthura.minsdk.models.ErrorResponse
import tech.minthura.minsdk.models.Errors
import java.io.IOException
import java.lang.Exception
import javax.net.ssl.HttpsURLConnection

const val TAG = "MinSDK"

open class BaseService {

    open fun handleApiError(error: Throwable, onError : (error : Error) -> Unit) {
        if (error is HttpException) {
            Log.e(TAG,error.message())
            when (error.code()) {
                HttpsURLConnection.HTTP_UNAUTHORIZED -> onError(Error(Errors.UNAUTHORIZED, getErrorResponse(error)))
                HttpsURLConnection.HTTP_FORBIDDEN -> onError(Error(Errors.FORBIDDEN, getErrorResponse(error)))
                HttpsURLConnection.HTTP_INTERNAL_ERROR -> onError(Error(Errors.INTERNAL_SERVER_ERROR, getErrorResponse(error)))
                HttpsURLConnection.HTTP_BAD_REQUEST -> onError(Error(Errors.BAD_REQUEST, getErrorResponse(error)))
                else -> onError(Error(Errors.UNKNOWN, getErrorResponse(error)))
            }
        } else {
            error.printStackTrace()
            if (error.message != null){
                onError(Error(Errors.UNKNOWN, ErrorResponse(error.message!!, 500)))
            } else {
                onError(Error(Errors.UNKNOWN, ErrorResponse("Unknown error", 500)))
            }

        }
    }

    open fun <T : Any>coroutineLaunch(scope: CoroutineScope, suspend: suspend () -> T, onSuccess : (t : T) -> Unit, onError : (error : Error) -> Unit) : @NonNull Job {
        val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
            handleApiError(throwable, onError)
        }
        return scope.launch(exceptionHandler) {
            val response = suspend()
            withContext(Dispatchers.Main){
                onSuccess(response)
            }
        }
    }

    private fun buildErrorResponse(json : String) : ErrorResponse? {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val jsonAdapter: JsonAdapter<ErrorResponse> = moshi.adapter(
            ErrorResponse::class.java
        )
        return try {
            jsonAdapter.fromJson(json)
        } catch (e : JsonDataException) {
            ErrorResponse("JsonDataException", 500)
        } catch (e : IOException) {
            ErrorResponse("IOException", 500)
        }

    }

    private fun getErrorResponse(error : HttpException) : ErrorResponse? {
        error.response()?.let { it ->
            it.errorBody()?.let {
                return buildErrorResponse(it.string())
            }
        }
        return null
    }

}