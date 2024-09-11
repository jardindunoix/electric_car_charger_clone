package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.ui

import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolMapResponse
import retrofit2.Response

sealed class MapUiState {
   object Loading : MapUiState()
   data class Success(val curr: Response<PoolMapResponse>) : MapUiState()
   data class Error(val msg: String) : MapUiState()
}