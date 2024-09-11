package cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail

data class ChargingDto(
    val first_date: String,
    val kw: String,
    val kwh: String,
    val seconds: String
)