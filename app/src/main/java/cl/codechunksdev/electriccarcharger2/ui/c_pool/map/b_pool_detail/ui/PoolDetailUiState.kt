package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui

import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolMapResponse
import retrofit2.Response

sealed class PoolDetailUiState {
   object Loading : PoolDetailUiState()
   data class Success(val curr: Response<PoolMapResponse>) : PoolDetailUiState()
   data class Error(val msg: String) : PoolDetailUiState()
}