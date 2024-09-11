package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.ui.map_viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.codechunksdev.electriccarcharger2.domain.repository.map_screen.MapRepository
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolMapResponse
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.ui.MapUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(private val mapRepository: MapRepository) : ViewModel() {

    private var _responsePools = MutableLiveData<Response<PoolMapResponse>>()
    val responsePools: LiveData<Response<PoolMapResponse>> = _responsePools


    private val _uiState = MutableStateFlow<MapUiState>(MapUiState.Loading)
    val uiState: StateFlow<MapUiState> = _uiState
//   private val mapRepository = MapRepository()

    fun startFlow() =
        viewModelScope.launch {
            mapRepository.currentRespPools
                .catch { _uiState.value = MapUiState.Error(it.message.orEmpty()) }
                .flowOn(Dispatchers.IO)
                .collect { _uiState.value = MapUiState.Success(it) }
        }

    fun stopFlow() = mapRepository.stopUpdates()

}
