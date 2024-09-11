package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

@OptIn(ExperimentalCoroutinesApi::class)
class LocationService {

   suspend fun getUserLocation(context: Context): Any? {
      val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
      val isUserLocationPermissionGranted = true
      val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
      val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
      if (!isGpsEnabled || !isUserLocationPermissionGranted) {
         return null
      }

      return suspendCancellableCoroutine { cont ->
         if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            true
         }
         fusedLocationProviderClient.lastLocation.apply {
            if (isComplete) {
               if (isSuccessful) cont.resume(result) {}
               else cont.resume(null) {}
               return@suspendCancellableCoroutine
            }
            addOnSuccessListener { cont.resume(it) {} }
            addOnFailureListener { cont.resume(null) {} }
            addOnCanceledListener { cont.resume(null) {} }
         }
      }
   }
}