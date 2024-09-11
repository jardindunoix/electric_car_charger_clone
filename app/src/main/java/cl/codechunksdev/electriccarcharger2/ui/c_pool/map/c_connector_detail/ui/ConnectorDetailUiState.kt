package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.c_connector_detail.ui

import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.ChargingDto

sealed class ConnectorDetailUiState {
   object Loading : ConnectorDetailUiState()
   data class Success(val curr: ChargingDto) : ConnectorDetailUiState()
   data class Error(val msg: String) : ConnectorDetailUiState()
}