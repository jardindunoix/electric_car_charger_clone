package cl.codechunksdev.electriccarcharger2.data.network.sign_in

import cl.codechunksdev.electriccarcharger2.domain.dto.sign_in.SigInResponse
import cl.codechunksdev.electriccarcharger2.domain.dto.sign_in.SignInPojo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignInApiCall {
    @POST("auth/login/")
    suspend fun singIn(@Body signInPojo: SignInPojo): Response<SigInResponse>

}