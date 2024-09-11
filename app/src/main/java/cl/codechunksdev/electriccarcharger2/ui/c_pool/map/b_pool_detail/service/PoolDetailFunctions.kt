package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.service

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat
import cl.codechunksdev.electriccarcharger2.R

fun getConnectorName(
   name: String,
   typeAlias: String
): String {
   return if (name.isEmpty()) typeAlias else "$name - $typeAlias"
}

fun getConnectorImage(type: String?): Int {
   var img = 0
   when (type) {
      "EVPhysicalConnectorType_CHADEMO" -> img = R.drawable.icon_conn_chademo
      "EVPhysicalConnectorType_GBT_AC" -> img = R.drawable.icon_conn_gbt_ac
      "EVPhysicalConnectorType_GBT_DC" -> img = R.drawable.icon_conn_gbt_dc
      "EVPhysicalConnectorType_IEC_62196_T1", "EVPhysicalConnectorType_CHAOJI" -> img = R.drawable.icon_conn_type_1
      "EVPhysicalConnectorType_IEC_62196_T1_COMBO" -> img = R.drawable.icon_conn_combo1
      "EVPhysicalConnectorType_IEC_62196_T2" -> img = R.drawable.icon_conn_type_2
      "EVPhysicalConnectorType_IEC_62196_T2_COMBO" -> img = R.drawable.icon_conn_combo2
      "EVPhysicalConnectorType_TESLA_R" -> img = R.drawable.icon_conn_tesla
   }
   return img
}

fun getChargerImage(available: String? = "not available"): Int {
   return when (available) {
      "Available" -> R.drawable.charger_available
      "Connected" -> R.drawable.charger_available
      else -> R.drawable.charger_not_available
   }
}

@SuppressLint("ClickableViewAccessibility")
fun goToMapButtonHowToGet(
   vieW: AppCompatButton?,
   context: Context,
   latitud: String?,
   longitud: String?
) {
   vieW!!.setOnTouchListener { v: View, event: MotionEvent ->
      if (event.action == MotionEvent.ACTION_DOWN) {
         v.background = ContextCompat.getDrawable(
            vieW.context,
            R.drawable.bg_btn_round_corner_pressed
         )
         v.invalidate()
      } else if (event.action == MotionEvent.ACTION_UP) {
         v.background = ContextCompat.getDrawable(
            vieW.context,
            R.drawable.bg_btn_round_corner_normal
         )
         v.invalidate()
         val gmmIntentUri = Uri.parse("google.navigation:q=$latitud,$longitud")
         val mapIntent = Intent(
            Intent.ACTION_VIEW,
            gmmIntentUri
         )
         context.startActivity(mapIntent)
      }
      true
   }
}