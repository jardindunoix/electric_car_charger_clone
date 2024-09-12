package cl.codechunksdev.electriccarcharger2.domain.repository.connector_detail

import cl.codechunksdev.electriccarcharger2.data.network.RetrofitInstance
import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.ChargingDto
import cl.codechunksdev.electriccarcharger2.utilities.Constants.DELAY_FLOW_CHARGE
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class ConnectorDetailRepository(
   token: String,
   connectorID: String?
) {

   private var chargeCondition = true
   val currentCharge: Flow<ChargingDto> = flow {
      while (chargeCondition) {
         val curr = RetrofitInstance.connectorDetail.setCurrentCharge(
            "application/json; charset=utf-8",
            "Bearer $token",
            "16"
         )
         emit(curr)
         delay(DELAY_FLOW_CHARGE)
      }
   }

   fun stopUpdates() {
      MainScope()
         .launch {
            currentCharge
               .cancellable()
               .collect { }
         }
         .cancel()
      chargeCondition = false
   }
}