package cl.codechunksdev.electriccarcharger2.data.network.map_screen

import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolMapResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface MapApiCall {

    @GET("pools/")
    suspend fun getPools(
        @Query(
            value = "company",
            encoded = false
        ) enterpriceNumber: String
    ): Response<PoolMapResponse>


    @Multipart
    @POST
    suspend fun setMultipart(
        @Part iamge: MultipartBody.Part
    ): Call<*>

//    convert image file to multipart body
//    val requestBody = RequestBody.create(MediaType.parse(getContentResolver().getType(fileUri)), file);
//    val a = MultipartBody.Part.creartFormData("picture", file.getName(), requestBody);

}