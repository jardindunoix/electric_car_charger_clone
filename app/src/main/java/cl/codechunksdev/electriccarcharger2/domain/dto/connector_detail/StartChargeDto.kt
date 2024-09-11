package cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail

data class StartChargeDto(
    val corrienteMaxima: Int,
    val equipo: String,
    val pistola: String,
    val user_id: Int
)