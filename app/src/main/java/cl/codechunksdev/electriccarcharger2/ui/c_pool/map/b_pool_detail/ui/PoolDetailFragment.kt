package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.databinding.FragmentPoolDetailBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailConnector
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailStation
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolMapResponse
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.service.goToMapButtonHowToGet
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.pool_detail_adapter.ItemAdapter
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.pool_detail_viewmodel.PoolDetailViewModel
import cl.codechunksdev.electriccarcharger2.ui.c_pool.service.bottomMenuFunc
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LATITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LONGITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_ADDRESS
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_NAME
import cl.codechunksdev.electriccarcharger2.utilities.common_ui_service.CustomTypefaceSpan
import cl.codechunksdev.electriccarcharger2.utilities.common_ui_service.DividerItemDecorator
import cl.codechunksdev.electriccarcharger2.utilities.getSessionData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONException

@AndroidEntryPoint
class PoolDetailFragment : Fragment() {

   private var poolId: String? = null
   private var poolName: String? = null
   private var poolAddress: String? = null
   private var latitudArg: String? = null
   private var longitudArg: String? = null
   private var token: String? = ""
   private var cargadores: PoolMapResponse? = null
   private var adapter: ItemAdapter? = null
   private var stationList = listOf<PoolDetailStation>()
   private val poolDetailViewModel: PoolDetailViewModel by viewModels()
   private var _binding: FragmentPoolDetailBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentPoolDetailBinding.inflate(
         inflater,
         container,
         false
      )
      return binding.root
   }

   override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
   ) {
      super.onViewCreated(
         view,
         savedInstanceState
      )
      bottomMenuFunc(
         requireActivity(),
         this
      ) { findNavController() }
      lifecycleScope.launch { getSessionData(requireActivity()).collect { if (it.token.isNotEmpty()) token = it.token } }
   }

   override fun onStart() {
      super.onStart()
      onBundleFirst()
      onStringTitleSet()
      onBackPage()
      goToMapButtonHowToGet(
         binding.btnHowToGo,
         requireContext(),
         latitudArg,
         longitudArg
      )
      chargers
   }

   private val chargers: Unit
      get() {
         stationList = listOf()
         lifecycleScope.launch(Dispatchers.IO) {
            collectUiState()
            initData()
         }
      }

   @SuppressLint(
      "PotentialBehaviorOverride",
      "ClickableViewAccessibility"
   )
   private suspend fun collectUiState() {
      lifecycleScope.launch(Dispatchers.IO) {
         repeatOnLifecycle(Lifecycle.State.STARTED) {
            poolDetailViewModel.uiState.collect { uiState ->
               when (uiState) {
                  is PoolDetailUiState.Error -> {
                     // TODO: ?
                  }

                  PoolDetailUiState.Loading -> {
                     // TODO: ?
                  }

                  is PoolDetailUiState.Success -> {
                     withContext(Dispatchers.Main) {
                        val resp = uiState.curr
                        if (resp.isSuccessful && resp.code() >= 200 && resp.code() < 300) {
                           try {
                              if (cargadores != null && stationList.isNotEmpty()) cargadores!!.clear()
                              stationList = listOf()
                              cargadores = resp.body()
                              callChargers()
                              initData()
                           } catch (e: JSONException) {
                              e.printStackTrace()
                           }
                        } else {
                           // TODO: ?
                        }
                     }
                  }

                  else -> {}
               }
            }
         }
      }
      poolDetailViewModel.startFlow()
   }

   private fun callChargers() {
      for (i in 0 until cargadores!!.size) {
         val cargador = cargadores!![i]
         if (poolId == cargador.id.toString()) {
            for (x in 0 until cargador.stations.size) {
               val st = cargador.stations[x]
               val station = PoolDetailStation(
                  st.id.toString(),
                  st.lineas_id,
                  st.station_name,
                  st.station_alias,
                  st.station_status,
                  st.station_identifier
               )
               val poolDetailConnectors: MutableList<PoolDetailConnector> = ArrayList()
               for (y in 0 until st.connectors.size) {
                  val con = st.connectors[y]
                  val objectConn = PoolDetailConnector(
                     con.id.toString(),
                     con.connector_name,
                     con.connector_type,
                     con.connector_type_alias,
                     con.connector_status
                  )
                  poolDetailConnectors.add(objectConn)
               }
               station.connectors = poolDetailConnectors
               stationList = stationList.plus(station)
            }
         }
      }
   }

   private fun initData() {
      val layoutManager = LinearLayoutManager(requireContext())
      binding.recyclerViewPoolDetail.recyclerStations.layoutManager = layoutManager
      val dividerItemDecoration: ItemDecoration = DividerItemDecorator(
         ContextCompat.getDrawable(
            requireContext(),
            R.drawable.divider_line_dotted
         )!!
      )
      binding.recyclerViewPoolDetail.recyclerStations.addItemDecoration(dividerItemDecoration)
      adapter = ItemAdapter(
         stationList,
         poolId,
         poolName,
         poolAddress,
         latitudArg,
         longitudArg,
         requireContext()
      )
      adapter!!.udpateList(stationList)
      binding.recyclerViewPoolDetail.recyclerStations.adapter = adapter
   }

   @SuppressLint("ClickableViewAccessibility")
   private fun onBackPage() {
      binding.backIcon.setOnTouchListener { v: View, event: MotionEvent ->
         if (event.action == MotionEvent.ACTION_DOWN) {
            binding.backIcon.alpha = 0.3f
            v.invalidate()
         } else if (event.action == MotionEvent.ACTION_UP) {
            binding.backIcon.alpha = 1f
            v.invalidate()
            findNavController().navigate(R.id.action_pool_detail_fragment_to_map_fragment)
         }
         true
      }
   }

   @SuppressLint("ClickableViewAccessibility")
   private fun onStringTitleSet() {
      val distance = ""
      val montserratBold = ResourcesCompat.getFont(
         requireContext(),
         R.font.nunito_bold
      )
      val montserratRegular = ResourcesCompat.getFont(
         requireContext(),
         R.font.nunito_medium
      )
      val poolNameSS = SpannableStringBuilder(poolName)
      val poolAddressSS = SpannableStringBuilder(poolAddress)
      val distanceSS = SpannableStringBuilder(distance)
      poolNameSS.setSpan(
         CustomTypefaceSpan(
            "",
            montserratBold!!
         ),
         0,
         poolName!!.length,
         Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
      poolAddressSS.setSpan(
         CustomTypefaceSpan(
            "",
            montserratRegular!!
         ),
         0,
         poolAddress!!.length,
         Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
      distanceSS.setSpan(
         CustomTypefaceSpan(
            "",
            montserratBold
         ),
         0,
         distance.length,
         Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
      binding.textTerminalAddress.text = TextUtils.concat(
         poolNameSS,
         " ",
         poolAddressSS,
         " ",
         distanceSS
      )
   }

   private fun onBundleFirst() {
      Handler(Looper.getMainLooper()).postDelayed(
         {
            binding.recyclerViewPoolDetail.recyclerPool.isVisible = true
            binding.recyclerViewPoolDetailLoading.recyclerPoolLoading.isVisible = false
         },
         2000
      )

      poolId = arguments
         ?.getString(POOL_ID)
         .toString()
      poolName = arguments
         ?.getString(POOL_NAME)
         .toString()
      poolAddress = arguments
         ?.getString(POOL_ADDRESS)
         .toString()
      latitudArg = arguments
         ?.getString(LATITUDE)
         .toString()
      longitudArg = arguments
         ?.getString(LONGITUDE)
         .toString()
   }

   override fun onDestroy() {
      super.onDestroy()
      poolDetailViewModel.stopFlow()
      _binding = null
   }
}