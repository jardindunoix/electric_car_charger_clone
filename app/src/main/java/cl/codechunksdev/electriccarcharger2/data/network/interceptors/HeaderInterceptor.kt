package cl.codechunksdev.electriccarcharger2.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

//class AuthInterceptor @Inject constructor(private val tokenManager: TokenManager) : Interceptor {
class HeaderInterceptor @Inject constructor() : Interceptor {


    // for all retrofit calls
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain
            .request()
            .newBuilder()
            .header("Content-Type", "Application/json") // Accept
            //  .header("Autorization", tokenManager.getToken())
            .build()
        return chain.proceed(request)
    }

}

//class TokenManager @Inject constructor() {
//    fun getToken(): String = "Bearer "
//}