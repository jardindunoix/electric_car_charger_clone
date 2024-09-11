package cl.codechunksdev.electriccarcharger2.domain.repository.map_screen

import cl.codechunksdev.electriccarcharger2.data.network.RetrofitInstance
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolMapResponse
import cl.codechunksdev.electriccarcharger2.utilities.Constants.COMPANY_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.DELAY_FLOW_MAP
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject


class MapRepository @Inject constructor() {

   private var mapCondition = true

   val currentRespPools: Flow<Response<PoolMapResponse>> = flow {
      while (mapCondition) {
         val curr = RetrofitInstance.map.getPools(COMPANY_ID)
         emit(curr)
         delay(DELAY_FLOW_MAP)
      }
   }

   fun stopUpdates() {
      MainScope()
         .launch {
            currentRespPools
               .cancellable()
               .collect { }
         }
         .cancel()
      mapCondition = false
   }
}