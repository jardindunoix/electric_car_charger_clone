package cl.codechunksdev.electriccarcharger2.data.network.sign_up

import cl.codechunksdev.electriccarcharger2.domain.dto.sign_up.SigUpResponse
import cl.codechunksdev.electriccarcharger2.domain.dto.sign_up.SignUpPojo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface SignUpApiCall {

    @POST("auth/login/register/")
    suspend fun singUp(@Body signUpPojo: SignUpPojo): Response<SigUpResponse>

}