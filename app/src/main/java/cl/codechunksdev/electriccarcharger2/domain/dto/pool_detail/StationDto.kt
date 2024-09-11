package cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail


data class StationModel(
   val id: Int,
   val lineas_id: Int,
   val station_alias: String,
   val station_identifier: Long,
   val station_name: String,
   val station_status: Int,
//   val connectors: List<ConnectorModel>,
)

