package cl.codechunksdev.electriccarcharger2.ui.c_pool.history.service

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Throws(ParseException::class)
fun transformTimeToLocalTime(dateStr: String?): String {
   var dateStr = dateStr
   dateStr = dateStr!!.replace("T", " ")
   dateStr = dateStr.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[0]
   val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
   df.timeZone = TimeZone.getTimeZone("UTC")
   val date = df.parse(dateStr)
   df.timeZone = TimeZone.getDefault()
   val formattedDate = df.format(date!!)
   val finalDate = formattedDate.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
   return finalDate[0] + ":" + finalDate[1]
}


fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient, locationCallback: LocationCallback?, locationRequest: LocationRequest?, context: Context) {
   if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return
   }
   fusedLocationClient!!.requestLocationUpdates(locationRequest!!, locationCallback!!, Looper.getMainLooper())
}

fun stopLocationUpdates(fusedLocationClient: FusedLocationProviderClient, locationCallback: LocationCallback?) =
   fusedLocationClient.removeLocationUpdates(locationCallback!!)



