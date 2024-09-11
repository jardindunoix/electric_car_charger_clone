package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.pool_detail_viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import cl.codechunksdev.electriccarcharger2.domain.repository.connector_detail.pool_detail.PoolDetailRepository
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.PoolDetailUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class PoolDetailViewModel @Inject constructor(private val poolDetailRepository: PoolDetailRepository) : ViewModel() {
   
   private val _uiState = MutableStateFlow<PoolDetailUiState>(PoolDetailUiState.Loading)
   val uiState: StateFlow<PoolDetailUiState> = _uiState
   
   fun startFlow() =
      viewModelScope.launch {
         poolDetailRepository.currentRespPools
            .catch { _uiState.value = PoolDetailUiState.Error(it.message.orEmpty()) }
            .flowOn(Dispatchers.IO)
            .collect { _uiState.value = PoolDetailUiState.Success(it) }
      }
   
   fun stopFlow() = poolDetailRepository.stopUpdates()
   
}