package cl.codechunksdev.electriccarcharger2.data.network.login

import cl.codechunksdev.electriccarcharger2.domain.dto.login.EmailVerified
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface LoginApiCall {
   @GET("auth/exists/{company}/{email}")
   suspend fun verifyEmail(
      @Path("company")
      company: String,
      @Path("email")
      email: String,
   ): Response<EmailVerified>

}