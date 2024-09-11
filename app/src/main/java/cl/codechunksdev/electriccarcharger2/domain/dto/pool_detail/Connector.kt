package cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail

data class Connector(
    val connector_name: String,
    val connector_number: Int,
    val connector_status: String,
    val connector_type: String,
    val connector_type_alias: String,
    val id: Int
)