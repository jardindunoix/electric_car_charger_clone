package cl.codechunksdev.electriccarcharger2.data.network.connector_detail

import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.ChargingDto
import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.StartChargeDto
import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.StartChargeResponseDto
import cl.antoinette.electriccarchargerklon.domain.dto.connector_detail.StopChargeDto
import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.StopChargeResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ConnectorDetailApiCall {

    @GET("pools/current/{connector_id}/")
    suspend fun setCurrentCharge(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") token: String,
        @Path("connector_id") connectorID: String,
    ): ChargingDto


    @POST("pools/commands/stopCharge/")
    suspend fun stopCharge(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") token: String,
        @Body stopChargeObj: StopChargeDto,
    ): Response<StopChargeResponseDto>


    @POST("pools/commands/startCharge/")
    suspend fun startCharge(
        @Header("Content-Type") contentType: String,
        @Header("Authorization") token: String,
        @Body startChargeObj: StartChargeDto,
    ): Response<StartChargeResponseDto>

}