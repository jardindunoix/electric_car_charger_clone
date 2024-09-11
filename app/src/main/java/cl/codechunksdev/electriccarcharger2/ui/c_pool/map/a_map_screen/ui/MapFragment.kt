package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.data.network.RetrofitInstance
import cl.codechunksdev.electriccarcharger2.databinding.FragmentMapBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.map_screen.DataMapToPoolDetail
import cl.codechunksdev.electriccarcharger2.domain.dto.map_screen.Pool
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailConnector
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolDetailStation
import cl.codechunksdev.electriccarcharger2.domain.dto.pool_detail.PoolMapResponse
import cl.codechunksdev.electriccarcharger2.ui.c_pool.history.service.startLocationUpdates
import cl.codechunksdev.electriccarcharger2.ui.c_pool.history.service.stopLocationUpdates
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.service.boundsMap
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.service.generateDialogInfography
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.service.getMarkerInfo
import cl.codechunksdev.electriccarcharger2.ui.c_pool.service.bottomMenuFunc
import cl.codechunksdev.electriccarcharger2.utilities.Constants.COMPANY_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LATITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LONGITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_ADDRESS
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.TAG
import cl.codechunksdev.electriccarcharger2.utilities.Constants.ZOOM
import cl.codechunksdev.electriccarcharger2.utilities.getSessionData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import retrofit2.Response
import java.util.function.Consumer

@AndroidEntryPoint
class MapFragment : Fragment() {

   private var map: GoogleMap? = null
   private var latitud = 0.0
   private var longitud = 0.0
   private var fusedLocationClient: FusedLocationProviderClient? = null
   private var locationCallback: LocationCallback? = null
   private var locationRequest: LocationRequest? = null
   private var pools: MutableList<Pool>? = null
   private var imageViewLocationBtnValidated = false
   private var imageViewFilterPoolBtnValidated = false
   private var filtered = false
   private var token: String? = ""
   private var _binding: FragmentMapBinding? = null
   private val binding get() = _binding!!


   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentMapBinding.inflate(
         inflater,
         container,
         false
      )
      return binding.root
   }

   @SuppressLint(
      "PotentialBehaviorOverride",
      "ClickableViewAccessibility"
   )
   override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
   ) {
      super.onViewCreated(
         view,
         savedInstanceState
      )
      imageViewFilterPoolBtnValidated = true
      imageViewLocationBtnValidated = true
      bottomMenuFunc(
         requireActivity(),
         this
      ) { findNavController() }
      pools = ArrayList()
      lifecycleScope.launch {
         getSessionData(requireActivity()).collect {
            if (it.token.isNotEmpty()) token = it.token
         }
      }
      fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
      locationRequest = LocationRequest()
      locationCallback = object : LocationCallback() {
         override fun onLocationResult(locationResult: LocationResult) {
            for (location in locationResult.locations) {
               centerMap()
               stopLocationUpdates(
                  fusedLocationClient!!,
                  locationCallback
               )
            }
         }
      }
      val mapFragment = this.childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment?
      mapFragment!!.getMapAsync { googleMap: GoogleMap? ->
         map = googleMap
         map!!.uiSettings.isMyLocationButtonEnabled = false
         requestPermissions()
         map!!.setOnMarkerClickListener { marker: Marker? ->
            goToPoolDetail(marker!!)
            true
         }
      }
      filtered = false
      with(binding) {
         with(btnFiltrarDisponiblesInclude) {
            btnFiltrarDisponibles.setOnTouchListener { v: View, event: MotionEvent ->
               if (!filtered) {
                  if (event.action == MotionEvent.ACTION_DOWN) {
                     imageViewLocationBtnValidated = false
                     if (imageViewFilterPoolBtnValidated) {
                        btnFiltrarDisponibles.setImageResource(R.drawable.icon_map_btn_filter_pool_dark)
                        tvFiltrarDisponibles.setTextColor(
                           ContextCompat.getColor(
                              requireContext(),
                              R.color.fragment_map_filter_pool_light_txt
                           )
                        )
                        v.invalidate()
                     }
                  } else if (event.action == MotionEvent.ACTION_UP) {
                     if (imageViewFilterPoolBtnValidated) {
                        pools!!.forEach(Consumer { pool: Pool ->
                           pool.marker.isVisible = false
                           if (pool.connectorsAvailables > 0) pool.marker.isVisible = true
                        })
                        filtered = true
                     }
                     imageViewLocationBtnValidated = true
                  }
                  boundsMap(
                     pools,
                     map,
                     latitud,
                     longitud
                  )
               } else {
                  if (event.action == MotionEvent.ACTION_DOWN) {
                     imageViewLocationBtnValidated = false
                     if (imageViewFilterPoolBtnValidated) {
                        btnFiltrarDisponibles.setImageResource(R.drawable.icon_map_btn_filter_pool_light)
                        tvFiltrarDisponibles.setTextColor(
                           ContextCompat.getColor(
                              requireContext(),
                              R.color.fragment_map_filter_pool_dark_txt
                           )
                        )
                        v.invalidate()
                     }
                  } else if (event.action == MotionEvent.ACTION_UP) {
                     if (imageViewFilterPoolBtnValidated) {
                        pools!!.forEach(Consumer { pool: Pool ->
                           pool.marker.isVisible = true
                        })
                        filtered = false
                     }
                     imageViewLocationBtnValidated = true
                  }
               }
               true
            }
            btnLocation.setOnTouchListener { _: View?, event: MotionEvent ->
               if (event.action == MotionEvent.ACTION_DOWN) {
                  imageViewFilterPoolBtnValidated = false
                  if (imageViewLocationBtnValidated) {
                     btnLocation.setImageResource(R.drawable.icon_map_btn_location_dark)
                  }
               } else if (event.action == MotionEvent.ACTION_UP) {
                  if (imageViewLocationBtnValidated) {
                     btnLocation.setImageResource(R.drawable.icon_map_btn_location_light)
                     val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                     if (ContextCompat.checkSelfPermission(
                           requireContext(),
                           Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                           requireContext(),
                           Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                     ) {
                        return@setOnTouchListener true
                     }
                     val selfLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
                     val selfLoc = LatLng(
                        selfLocation!!.latitude,
                        selfLocation.longitude
                     )
                     val update = CameraUpdateFactory.newLatLngZoom(
                        selfLoc,
                        ZOOM
                     )
                     map!!.animateCamera(update)
                  }
                  imageViewFilterPoolBtnValidated = true
               }
               true
            }
            btnInfo.setOnTouchListener { v: View, event: MotionEvent ->
               if (event.action == MotionEvent.ACTION_DOWN) {
                  btnInfo.setImageResource(R.drawable.icon_map_btn_infography_dark)
                  generateDialogInfography(
                     requireContext(),
                     btnInfo
                  )
                  v.invalidate()
               }
               true
            }
         }
      }
   }

   private fun getChargers() {
      lifecycleScope.launch(Dispatchers.Main) {
         val response: Response<PoolMapResponse> = RetrofitInstance.map.getPools(COMPANY_ID)
         if (response.isSuccessful && response.code() == 200) {
            val cargadores = response.body()
            try {
               for (i in 0 until cargadores!!.size) {
                  val cargador = cargadores[i]
                  val poolObj = Pool(
                     cargador.id.toString(),
                     cargador.pool_name,
                     cargador.pool_address,
                     cargador.pool_latitude,
                     cargador.pool_longitude
                  )
                  val stations: MutableList<PoolDetailStation> = ArrayList()
                  for (x in 0 until cargador.stations.size) {
                     val station1 = cargador.stations[x]
                     val station = PoolDetailStation(
                        station1.id.toString(),
                        station1.lineas_id,
                        station1.station_name,
                        station1.station_alias,
                        station1.station_status,
                        station1.station_identifier
                     )
                     val poolDetailConnectors: MutableList<PoolDetailConnector> = ArrayList()
                     for (y in 0 until station1.connectors.size) {
                        val con = station1.connectors[y]
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
                     stations.add(station)
                  }
                  poolObj.stations = stations
                  val connectors = poolObj.connectorsLen
                  val disponibles = poolObj.connectorsAvailables
                  val isOffline = poolObj.isPoolOffline
                  var color: Int
                  var index: Int
                  if (disponibles == connectors) {
                     color = R.color.fragment_map_pin_green
                     index = 1010
                  } else if (disponibles == 0 && connectors > 0) {
                     color = R.color.fragment_map_pin_yellow
                     index = 1008
                  } else if (isOffline) {
                     color = R.color.fragment_map_pin_red
                     index = 1007
                  } else {
                     color = R.color.fragment_map_pin_blue
                     index = 1009
                  }
                  val disp: String = if (disponibles < 10) "0$disponibles" else "" + disponibles
                  val conn: String = if (connectors < 10) "0$connectors" else "" + connectors
                  val bitmap: Bitmap? = if (isOffline) getMarkerInfo(
                     "--/--",
                     color,
                     requireActivity()
                  )
                  else getMarkerInfo(
                     "$disp/$conn",
                     color,
                     requireActivity()
                  )

                  val marker = map!!.addMarker(
                     MarkerOptions()
                        .position(
                           LatLng(
                              cargador.pool_latitude,
                              cargador.pool_longitude
                           )
                        )
                        .draggable(false)
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap!!))
                        .flat(false)
                        .anchor(
                           0.5f,
                           1.0f
                        )
                        .zIndex(index.toFloat())
                  )
                  poolObj.marker = marker
                  pools!!.add(poolObj)
               }
               boundsMap(
                  pools,
                  map,
                  latitud,
                  longitud
               )
            } catch (e: JSONException) {
               e.printStackTrace()
            }
         } else {
            Log.d(
               TAG,
               "---:${response.isSuccessful} -- ${response.code()} "
            )
         }
      }
   }

   private fun requestPermissions() {
      if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
         ) != PackageManager.PERMISSION_GRANTED
      ) {
         requestPermissions(
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            2000
         )
      } else {
         centerMap()
         getChargers()
      }
   }

   @Deprecated("Deprecated in Java")
   override fun onRequestPermissionsResult(
      requestCode: Int,
      permissions: Array<out String>,
      grantResults: IntArray
   ) {
      super.onRequestPermissionsResult(
         requestCode,
         permissions,
         grantResults
      )
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED && requestCode == 2000) centerMap();
   }

   @Override
   fun centerMap() {
      if (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
         ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
         ) != PackageManager.PERMISSION_GRANTED
      ) {
         return
      }
      fusedLocationClient!!.lastLocation.addOnSuccessListener { location: Location? ->
         if (location != null) {
            latitud = location.latitude
            longitud = location.longitude
            val direccion = LatLng(
               latitud,
               longitud
            )
            map!!.animateCamera(
               CameraUpdateFactory.newLatLngZoom(
                  direccion,
                  10f
               )
            )
            if (ContextCompat.checkSelfPermission(
                  requireContext(),
                  Manifest.permission.ACCESS_FINE_LOCATION
               ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                  requireContext(),
                  Manifest.permission.ACCESS_COARSE_LOCATION
               ) != PackageManager.PERMISSION_GRANTED
            ) {
               return@addOnSuccessListener
            }
            map!!.isMyLocationEnabled = true
            getChargers()
         } else {
            startLocationUpdates(
               fusedLocationClient!!,
               locationCallback,
               locationRequest,
               requireContext()
            )
         }
      }
   }

   private fun goToPoolDetail(
      marker: Marker,
   ): DataMapToPoolDetail? {
      try {
         var dataMapToHowToGet: DataMapToPoolDetail? = null
         var pool: Pool? = null
         for (i in pools!!.indices) if (pools!![i].marker == marker) pool = pools!![i]
         if (pool != null) {
            dataMapToHowToGet = DataMapToPoolDetail(
               poolId = pool.id,
               poolName = pool.name + ", ",
               poolAddress = pool.address,
               latitude = pool.latLng.latitude.toString(),
               longitude = pool.latLng.longitude.toString(),
               stationList = pool.stations
            )
            val bundle = bundleOf(
               POOL_ID to dataMapToHowToGet.poolId,
               POOL_NAME to dataMapToHowToGet.poolName,
               POOL_ADDRESS to dataMapToHowToGet.poolAddress,
               LATITUDE to dataMapToHowToGet.latitude,
               LONGITUDE to dataMapToHowToGet.longitude,
            )
            findNavController().navigate(
               R.id.action_map_fragment_to_pool_detail_fragment,
               bundle
            )
         }
         return dataMapToHowToGet
      } catch (e: Exception) {
         Log.d(
            TAG,
            "error en el : $e "
         )
         return null
      }

   }

}