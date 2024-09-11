package cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail

data class PoolMapResponseItem(
    val id: Int,
    val pool_address: String,
    val pool_latitude: Double,
    val pool_longitude: Double,
    val pool_name: String,
    val stations: List<Station>
)