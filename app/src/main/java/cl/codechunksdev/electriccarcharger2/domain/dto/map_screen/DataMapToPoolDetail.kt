package cl.codechunksdev.electriccarcharger2.domain.dto.map_screen

import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailStation

data class DataMapToPoolDetail(
   val poolId: String,
   val poolName: String,
   val poolAddress: String,
   val latitude: String,
   val longitude: String,
   val stationList: MutableList<PoolDetailStation>
)
