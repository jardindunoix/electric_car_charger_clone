package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.service

import android.content.Context
import android.content.DialogInterface
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.databinding.DialogInfographyBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.map_screen.Pool
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ui.IconGenerator
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

fun generateDialogInfography(context: Context?, imageViewQuestionBtn: ImageView) {
    val alert: View = LayoutInflater.from(context).inflate(R.layout.dialog_infography, null)
    val builder = AlertDialog.Builder(context!!)
    val bindingDialog = DialogInfographyBinding.bind(alert)
    //
//    val textSizeTitles = 11f
//    val textSize = 10f
//    val textSizeSub = 8f
//    bindingDialog.textStates.textSize = textSizeTitles
//    bindingDialog.textTerminal.textSize = textSizeTitles
//    bindingDialog.textConnectorsAvailable.textSize = textSizeSub
//    bindingDialog.textConnectorsInStation.textSize = textSizeSub
//    bindingDialog.textOutOfService.textSize = textSize
//    bindingDialog.textNoneAvailable.textSize = textSize
//    bindingDialog.textSomeAvailable.textSize = textSize
//    bindingDialog.textAllAvailable.textSize = textSize
    val dialog = builder.setView(alert).create()
    dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    dialog.setOnCancelListener { _: DialogInterface? -> imageViewQuestionBtn.setImageResource(R.drawable.icon_map_btn_infography_light) }
    dialog.show()
    bindingDialog.textCloseIcon.setOnClickListener { v: View? ->
        imageViewQuestionBtn.setImageResource(R.drawable.icon_map_btn_infography_light)
        dialog.cancel()
    }
    val wmlp = dialog.window!!.attributes
    wmlp.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
    wmlp.y = -90
    dialog.window!!.setLayout(1100, 1800)
}

fun boundsMap(pools: MutableList<Pool>?, map: GoogleMap?, lat1: Double, lon1: Double) {
    // Primero ordena la lista
    val n = pools!!.size
    var temp: Pool?
    val ordererPools: MutableList<Pool> = pools
    for (i in 0 until n) {
        for (j in 1 until n - 1) {
            val pan = pools[j - 1]
            val pac = pools[j]
            if (calculateDistance(
                    lat1,
                    lon1,
                    pan.latLng.latitude,
                    pan.latLng.longitude
                ) > calculateDistance(lat1, lon1, pac.latLng.latitude, pac.latLng.longitude)
            ) {
                temp = pan
                ordererPools[j - 1] = pac
                ordererPools[j] = temp
            }
        }
    }
    val latlonbounds = LatLngBounds.Builder()
    var cont = 0
    for (x in ordererPools.indices) {
        if (ordererPools[x].marker.isVisible) {
            latlonbounds.include(ordererPools[x].latLng)
            cont++
        }
        if (cont == 2) break
    }
    latlonbounds.include(LatLng(lat1, lon1))
    val bounds = latlonbounds.build()
//   println("Orderer list: " + ordererPools.size)
    map?.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 250))
}

fun calculateDistance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
    val REF = 6371
    val latDistance = Math.toRadians(lat2 - lat1)
    val lonDistance = Math.toRadians(lon2 - lon1)
    val a = (sin(latDistance / 2) * sin(latDistance / 2)
            + (cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2))
            * sin(lonDistance / 2) * sin(lonDistance / 2)))
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return sqrt(REF * c * 1000) // convert to meters
}

fun getMarkerInfo(label: String?, color: Int, context: Context): Bitmap? {
    val iconGenerator = IconGenerator(context)
    val mv = LayoutInflater.from(context).inflate(R.layout.component_marker_pin, null)
    val counter = mv.findViewById<TextView>(R.id.pin_map_contador)
    val image = mv.findViewById<ImageView>(R.id.pin_map_fondo)
    image.imageTintList = ColorStateList.valueOf(context.getColor(color))
    counter.text = label
    iconGenerator.setContentView(mv)
    iconGenerator.setBackground(null)
    return iconGenerator.makeIcon()
}

