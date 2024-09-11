package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.c_connector_detail.ui.connector_detail_viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cl.antoinette.electriccarchargerklon.domain.dto.connector_detail.StopChargeDto
import cl.codechunksdev.electriccarcharger2.data.network.RetrofitInstance
import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.StartChargeDto
import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.StartChargeResponseDto
import cl.codechunksdev.electriccarcharger2.domain.dto.connector_detail.StopChargeResponseDto
import cl.codechunksdev.electriccarcharger2.domain.repository.connector_detail.ConnectorDetailRepository
import cl.codechunksdev.electriccarcharger2.ui.c_pool.activity.PoolFlowActivity
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.c_connector_detail.ui.ConnectorDetailUiState
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CORRIENTE_MAX
import cl.codechunksdev.electriccarcharger2.utilities.Constants.TAG
import cl.codechunksdev.electriccarcharger2.utilities.Constants.USER_ID_FOR_CHARGE
import cl.codechunksdev.electriccarcharger2.utilities.getSessionData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import retrofit2.Response

class ConnectorDetailViewModel : ViewModel() {

   private val _uiState = MutableStateFlow<ConnectorDetailUiState>(ConnectorDetailUiState.Loading)
   val uiState: StateFlow<ConnectorDetailUiState> = _uiState

   private val _token = MutableLiveData<String>()
   private val _equipo = MutableLiveData<String>()
   private val _connectorID = MutableLiveData<String>()
   private val _pistola = MutableLiveData<String>()
   private val _responseStartCharge = MutableLiveData<Response<StartChargeResponseDto>>()
   val responseStartCharge: LiveData<Response<StartChargeResponseDto>> = _responseStartCharge
   val responseStopCharge = MutableLiveData<Response<StopChargeResponseDto>>()

   init {
      setToken()
   }

   private val connectorDetailRepository = ConnectorDetailRepository(
      _token.value.toString(),
      _connectorID.value.toString()
   )

   fun startFlow() = viewModelScope.launch {
      connectorDetailRepository.currentCharge
         .catch { _uiState.value = ConnectorDetailUiState.Error(it.message.orEmpty()) }
         .flowOn(Dispatchers.IO)
         .collect { _uiState.value = ConnectorDetailUiState.Success(it) }
   }

   fun stopFlow() = connectorDetailRepository.stopUpdates()

   fun setEquipo(equipo: String) {
      _equipo.value = equipo
   }

   fun setPistola(pistola: String) {
      _pistola.value = pistola
   }

   fun startCurrentCharge(connId: String?) = viewModelScope.launch {
      _connectorID.value = connId!!
      _responseStartCharge.value = RetrofitInstance.connectordetail.startCharge(
         "application/json; charset=utf-8",
         "Bearer ${_token.value.toString()}",
         setStartChargeObj()
      )

      Log.d(
         TAG,
         "startCurrentCharge: ${_responseStartCharge.value}"
      )

   }

   fun stopCurrentCharge() = viewModelScope.launch {
      responseStopCharge.value = RetrofitInstance.connectordetail.stopCharge(
         "application/json; charset=utf-8",
         "Bearer ${_token.value.toString()}",
         setStopChargeObj()
      )
   }

   private fun setToken() = viewModelScope.launch { getSessionData(PoolFlowActivity()).collect { _token.value = it.token } }

   private fun setStartChargeObj(): StartChargeDto = StartChargeDto(
      corrienteMaxima = CORRIENTE_MAX,
      equipo = _equipo.value.toString(),
      pistola = _pistola.value.toString(),
      user_id = USER_ID_FOR_CHARGE
   )

   private fun setStopChargeObj(): StopChargeDto = StopChargeDto(
      equipo = _equipo.value.toString(),
      pistola = _pistola.value.toString(),
   )
}