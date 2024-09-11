package cl.codechunksdev.electriccarcharger2.ui.c_pool.service

import android.annotation.SuppressLint
import android.app.Activity
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.ui.c_pool.activity.PoolFlowActivity
import cl.codechunksdev.electriccarcharger2.ui.c_pool.history.ui.RecordFragment
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.a_map_screen.ui.MapFragment
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.ui.PoolDetailFragment
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.c_connector_detail.ui.ConnectorDetailFragment
import cl.codechunksdev.electriccarcharger2.ui.c_pool.settings.ui.SettingsFragment

fun bottomMenuFunc(
   activity: Activity,
   fragment: Fragment,
   findNavController: () -> NavController,
) {
   val configBtn = (activity as PoolFlowActivity).settingsIcon
   val historyBtn = activity.historialIcon
   val mapBtn = activity.mapIcon
   val configTxt = activity.configText
   val historyTxt = activity.historialText
   val mapTxt = activity.mapText

   when (fragment) {
      is MapFragment -> {
         configBtn?.isEnabled = true
         historyBtn?.isEnabled = true
         mapBtn?.isEnabled = false

         configBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_config_light)
         historyBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_historial_light)
         mapBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_map_dark)

         configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_dark))
         historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_dark))
         mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_light))

         bottomButtonConfig(
            activity,
            configBtn,
            mapBtn,
            R.drawable.icon_bottom_menu_btn_map_light,
            R.id.action_map_fragment_to_config_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }

         bottomButtonHistorial(
            activity,
            historyBtn,
            mapBtn,
            R.drawable.icon_bottom_menu_btn_map_light,
            R.id.action_map_fragment_to_upload_history_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }
      }

      is PoolDetailFragment -> {
         configBtn?.isEnabled = true
         historyBtn?.isEnabled = true
         mapBtn?.isEnabled = false

         configBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_config_light)
         historyBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_historial_light)
         mapBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_map_dark)

         configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_dark))
         historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_dark))
         mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_light))

         bottomButtonConfig(
            activity,
            configBtn,
            mapBtn,
            R.drawable.icon_bottom_menu_btn_map_light,
            R.id.action_pool_detail_fragment_to_config_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }

         bottomButtonHistorial(
            activity,
            historyBtn,
            mapBtn,
            R.drawable.icon_bottom_menu_btn_map_light,
            R.id.action_pool_detail_fragment_to_upload_history_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }
      }

      is ConnectorDetailFragment -> {
         configBtn?.isEnabled = true
         historyBtn?.isEnabled = true
         mapBtn?.isEnabled = false

         configBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_config_light)
         historyBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_historial_light)
         mapBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_map_dark)

         configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_dark))
         historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_dark))
         mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_light))

         bottomButtonConfig(
            activity,
            configBtn,
            mapBtn,
            R.drawable.icon_bottom_menu_btn_map_light,
            R.id.action_connector_detail_fragment_to_config_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }

         bottomButtonHistorial(
            activity,
            historyBtn,
            mapBtn,
            R.drawable.icon_bottom_menu_btn_map_light,
            R.id.action_connector_detail_fragment_to_upload_history_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }
         bottomButtonMap(
            activity,
            mapBtn,
            configBtn,
            R.drawable.icon_bottom_menu_btn_config_light,
            R.id.action_connector_detail_fragment_to_map_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }
      }

      is SettingsFragment -> {
         configBtn?.isEnabled = false
         historyBtn?.isEnabled = true
         mapBtn?.isEnabled = true

         configBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_config_dark)
         historyBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_historial_light)
         mapBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_map_light)

         configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_light))
         historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_dark))
         mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_dark))

         bottomButtonMap(
            activity,
            mapBtn,
            configBtn,
            R.drawable.icon_bottom_menu_btn_config_light,
            R.id.action_config_fragment_to_map_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }

         bottomButtonHistorial(
            activity,
            historyBtn,
            configBtn,
            R.drawable.icon_bottom_menu_btn_config_light,
            R.id.action_config_fragment_to_upload_history_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }
      }

      is RecordFragment -> {
         configBtn?.isEnabled = true
         historyBtn?.isEnabled = false
         mapBtn?.isEnabled = true

         configBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_config_light)
         historyBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_historial_dark)
         mapBtn?.setImageResource(R.drawable.icon_bottom_menu_btn_map_light)

         configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_dark))
         historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_light))
         mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_dark))

         bottomButtonMap(
            activity,
            mapBtn,
            historyBtn,
            R.drawable.icon_bottom_menu_btn_historial_light,
            R.id.action_upload_history_fragment_to_map_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }

         bottomButtonConfig(
            activity,
            configBtn,
            historyBtn,
            R.drawable.icon_bottom_menu_btn_historial_light,
            R.id.action_upload_history_fragment_to_config_fragment,
            configTxt,
            historyTxt,
            mapTxt,
         ) { findNavController() }
      }
   }
}

@SuppressLint("ClickableViewAccessibility")
private fun bottomButtonHistorial(
   activity: Activity,
   imageViewHistorialBtn: ImageView?,
   viewBtn: ImageView?,
   imagRes: Int,
   navi: Int,
   configTxt: TextView?,
   historyTxt: TextView?,
   mapTxt: TextView?,
   findNavController: () -> NavController,
) {
   imageViewHistorialBtn?.setOnTouchListener { _: View, event: MotionEvent ->
      when (event.action) {
         MotionEvent.ACTION_DOWN -> {
            imageViewHistorialBtn.setImageResource(R.drawable.icon_bottom_menu_btn_historial_dark)
            viewBtn?.setImageResource(imagRes)
            configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_dark))
            historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_light))
            mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_dark))
            imageViewHistorialBtn.invalidate()
         }

         MotionEvent.ACTION_UP -> {
            findNavController().navigate(navi)
         }
      }
      true
   }
}

@SuppressLint("ClickableViewAccessibility")
private fun bottomButtonMap(
   activity: Activity,
   imageViewMapBtn: ImageView?,
   viewBtn: ImageView?,
   imagRes: Int,
   navi: Int,
   configTxt: TextView?,
   historyTxt: TextView?,
   mapTxt: TextView?,
   findNavController: () -> NavController,
) {
   imageViewMapBtn?.setOnTouchListener { _: View, event: MotionEvent ->
      when (event.action) {
         MotionEvent.ACTION_DOWN -> {
            imageViewMapBtn.setImageResource(R.drawable.icon_bottom_menu_btn_map_dark)
            viewBtn?.setImageResource(imagRes)
            configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_dark))
            historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_dark))
            mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_light))
            imageViewMapBtn.invalidate()
         }

         MotionEvent.ACTION_UP -> {
            findNavController().navigate(navi)
         }
      }
      true
   }
}

@SuppressLint("ClickableViewAccessibility")
private fun bottomButtonConfig(
   activity: Activity,
   imageViewConfigBtn: ImageView?,
   viewBtn: ImageView?,
   imagRes: Int,
   navi: Int,
   configTxt: TextView?,
   historyTxt: TextView?,
   mapTxt: TextView?,
   findNavController: () -> NavController,
) {
   imageViewConfigBtn?.setOnTouchListener { _: View, event: MotionEvent ->
      when (event.action) {
         MotionEvent.ACTION_DOWN -> {
            imageViewConfigBtn.setImageResource(R.drawable.icon_bottom_menu_btn_config_dark)
            viewBtn?.setImageResource(imagRes)
            configTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_config_light))
            historyTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_historial_dark))
            mapTxt!!.setTextColor(ContextCompat.getColor(activity, R.color.bottom_menu_txt_map_dark))
            imageViewConfigBtn.invalidate()
         }

         MotionEvent.ACTION_UP -> {
            findNavController().navigate(navi)
         }
      }
      true
   }
}
