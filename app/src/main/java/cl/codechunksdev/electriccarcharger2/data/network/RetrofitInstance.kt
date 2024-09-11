package cl.codechunksdev.electriccarcharger2.data.network

import cl.codechunksdev.electriccarcharger2.data.network.connector_detail.ConnectorDetailApiCall
import cl.codechunksdev.electriccarcharger2.data.network.interceptors.HeaderInterceptor
import cl.codechunksdev.electriccarcharger2.data.network.login.LoginApiCall
import cl.codechunksdev.electriccarcharger2.data.network.map_screen.MapApiCall
import cl.codechunksdev.electriccarcharger2.data.network.sign_in.SignInApiCall
import cl.codechunksdev.electriccarcharger2.data.network.sign_up.SignUpApiCall
import cl.codechunksdev.electriccarcharger2.utilities.Constants.URL_BASE
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {


   private val api by lazy {
      Retrofit
         .Builder()
         .baseUrl(URL_BASE)
         .addConverterFactory(GsonConverterFactory.create())
         .client(getClient()) // client or inyterceptors
         .build()
   }

   private fun getClient(): OkHttpClient = OkHttpClient
      .Builder()
      .addInterceptor(HeaderInterceptor())
      .build()


   val login: LoginApiCall by lazy {
      api.create(LoginApiCall::class.java)
   }

   val signin: SignInApiCall by lazy {
      api.create(SignInApiCall::class.java)
   }

   val signup: SignUpApiCall by lazy {
      api.create(SignUpApiCall::class.java)
   }

   val connectordetail: ConnectorDetailApiCall by lazy {
      api.create(ConnectorDetailApiCall::class.java)
   }

   val map: MapApiCall by lazy {
      api.create(MapApiCall::class.java)
   }

}