package cl.codechunksdev.electriccarcharger2.ui.c_pool.map.c_connector_detail.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.databinding.DialogConfirmStopChargeBinding
import cl.codechunksdev.electriccarcharger2.databinding.FragmentConnectorDetailBinding
import cl.codechunksdev.electriccarcharger2.ui.c_pool.activity.PoolFlowActivity
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.b_pool_detail.service.getConnectorImage
import cl.codechunksdev.electriccarcharger2.ui.c_pool.map.c_connector_detail.ui.connector_detail_viewmodel.ConnectorDetailViewModel
import cl.codechunksdev.electriccarcharger2.ui.c_pool.service.bottomMenuFunc
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CHARGER_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CONNECTOR
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CONNECTOR_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.CONNECTOR_TYPE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LATITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.LONGITUDE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.PISTOLA
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_ADDRESS
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.POOL_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.STATION_IDENTIFIER_TO_BODY
import cl.codechunksdev.electriccarcharger2.utilities.Constants.STATION_NAME
import cl.codechunksdev.electriccarcharger2.utilities.Constants.TITLE
import cl.codechunksdev.electriccarcharger2.utilities.Constants.TITLE_NAME
import cl.codechunksdev.electriccarcharger2.utilities.common_ui_service.CustomTypefaceSpan
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.RoundingMode

@AndroidEntryPoint
class ConnectorDetailFragment : Fragment() {

   private var equipo: String? = null
   private var pistola: String? = null
   private var connectorId: String? = null
   private var poolId: String? = null
   private var latitudArg: String? = null
   private var longitudArg: String? = null
   private var titleName: String? = null
   private var titleAddress: String? = null
   private var _binding: FragmentConnectorDetailBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   private val connectorDetailViewModel: ConnectorDetailViewModel by viewModels()

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentConnectorDetailBinding.inflate(
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
      navController = Navigation.findNavController(
         requireActivity(),
         R.id.nav_host_pool_flow_fragment
      )
      bottomMenuFunc(
         requireActivity(),
         this
      ) { findNavController() }
      firstBundle()
      connectorDetailViewModel.setEquipo(equipo!!)
      connectorDetailViewModel.setPistola(pistola!!)
      backpageBtn()
      chargeBtn()
   }

   @SuppressLint("ClickableViewAccessibility")
   private fun chargeBtn() {
      with(binding.btnStartCharge) {
         this.setOnTouchListener { _: View?, event: MotionEvent ->
            when (event.action) {
               MotionEvent.ACTION_DOWN -> this.background = ContextCompat.getDrawable(
                  requireContext(),
                  R.drawable.bg_btn_round_corner_pressed
               )

               MotionEvent.ACTION_UP -> {
                  background = ContextCompat.getDrawable(
                     requireContext(),
                     R.drawable.bg_btn_round_corner_normal
                  )
                  isEnabled = false

                  connectorDetailViewModel.responseStartCharge.observe(
                     viewLifecycleOwner,
                     Observer {
                        if (it != null) {
                           if (it.isSuccessful && it.code() >= 200 && it.code() < 300) onSuccessResponseStartCharge()
                           else onFailureResponseStartCharge()
                        }
                     })

                  connectorDetailViewModel.startCurrentCharge(connectorId)
               }
            }
            true
         }
      }
   }

   private fun onSuccessResponseStartCharge() {
      lifecycleScope.launch(Dispatchers.IO) {
         withContext(Dispatchers.Main) { mainThreadUpdate() }
         collectUiState()
         stopChargeBtn()
      }
   }

   private fun onFailureResponseStartCharge() {
      lifecycleScope.launch(Dispatchers.Main) {
         dialogResponseDefault(resources.getString(R.string.unespected_error_message))
      }
   }

   @SuppressLint("ClickableViewAccessibility")
   private fun stopChargeBtn() {
      with(binding) {
         with(btnCargaStopCharge) {
            setOnTouchListener { _, event ->
               when (event.action) {
                  MotionEvent.ACTION_DOWN -> {
                     background = ContextCompat.getDrawable(
                        context,
                        R.drawable.bg_btn_round_corner_white_border_pressed
                     )
                     setTextColor(
                        ContextCompat.getColor(
                           context,
                           R.color.blue_1
                        )
                     )
                  }

                  MotionEvent.ACTION_UP -> {
                     background = ContextCompat.getDrawable(
                        context,
                        R.drawable.bg_btn_round_corner_white_border_normal
                     )
                     setTextColor(
                        ContextCompat.getColor(
                           context,
                           R.color.blue_2
                        )
                     )
                     dialogStopCharge()
                  }
               }
               true
            }
         }
      }
   }

   @SuppressLint("ClickableViewAccessibility")
   private fun dialogStopCharge() {
      val alert: View = LayoutInflater
         .from(context)
         .inflate(
            R.layout.dialog_confirm_stop_charge,
            null
         )
      val builder = AlertDialog.Builder(requireContext())
      val dialog = builder
         .setView(alert)
         .create()
      dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
      dialog.show()
      with(DialogConfirmStopChargeBinding.bind(alert)) {
         btnStopNo.setOnTouchListener { _: View?, event: MotionEvent ->
            when (event.action) {
               MotionEvent.ACTION_DOWN -> {
                  btnStopNo.background = ContextCompat.getDrawable(
                     requireContext(),
                     R.drawable.bg_btn_round_corner_pressed
                  )
                  btnStopNo.invalidate()
               }

               MotionEvent.ACTION_UP -> {
                  btnStopNo.background = ContextCompat.getDrawable(
                     requireContext(),
                     R.drawable.bg_btn_round_corner_normal
                  )
                  btnStopNo.invalidate()
                  dialog.cancel()
               }
            }
            true
         }
         btnStopYes.setOnTouchListener { _: View?, event: MotionEvent ->
            when (event.action) {
               MotionEvent.ACTION_DOWN -> {
                  btnStopYes.background = ContextCompat.getDrawable(
                     requireContext(),
                     R.drawable.bg_btn_round_corner_white_border_pressed
                  )
                  btnStopYes.setTextColor(
                     ContextCompat.getColor(
                        requireContext(),
                        R.color.blue_1
                     )
                  )
                  btnStopYes.invalidate()
               }

               MotionEvent.ACTION_UP -> {
                  btnStopYes.background = ContextCompat.getDrawable(
                     requireContext(),
                     R.drawable.bg_btn_round_corner_white_border_pressed
                  )
                  btnStopYes.setTextColor(
                     ContextCompat.getColor(
                        requireContext(),
                        R.color.blue_2
                     )
                  )
                  btnStopYes.invalidate()
                  connectorDetailViewModel.stopCurrentCharge()
                  connectorDetailViewModel.responseStopCharge.observe(
                     viewLifecycleOwner,
                     Observer {
                        if (it.isSuccessful && it.code() >= 200 && it.code() < 300) {
                           connectorDetailViewModel.stopFlow()
                           with(binding) {
                              btnCargaStopCharge.visibility = View.GONE
                              connectorDetailsChargeStatus.text = resources.getString(R.string.charge_end)
                              connectorTitleTimeCharging.text = resources.getString(R.string.time_charging)
                              btnCloseVoucher.visibility = View.VISIBLE
                              btnCloseVoucher.setOnTouchListener { _: View, event: MotionEvent ->
                                 if (event.action == MotionEvent.ACTION_DOWN) {
                                    btnCloseVoucher.background = ContextCompat.getDrawable(
                                       requireContext(),
                                       R.drawable.bg_btn_round_corner_pressed
                                    )
                                    btnCloseVoucher.invalidate()
                                 } else if (event.action == MotionEvent.ACTION_UP) {
                                    btnCloseVoucher.background = ContextCompat.getDrawable(
                                       requireContext(),
                                       R.drawable.bg_btn_round_corner_normal
                                    )
                                    btnCloseVoucher.invalidate()
//                              use it when its time
//                              dialogResponseDefault("Voucher")
                                 }
                                 true
                              }
                              dialog.cancel()
                           }
                        } else {
                           // TODO: ?
                        }
                     })
               }
            }
            true
         }
      }

      val wmlp = dialog.window!!.attributes
      wmlp.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
      wmlp.y = 60
      dialog.window!!.setLayout(
         800,
         450
      )
   }

   private suspend fun mainThreadUpdate() {
      withContext(Dispatchers.Main) {
         visibilityProcess()
         enableBtn()
         settingTexts()
         (context as PoolFlowActivity).mapIcon!!.setImageResource(R.drawable.icon_bottom_menu_btn_map_light)
         (context as PoolFlowActivity).mapIcon!!.isEnabled = true
         (context as PoolFlowActivity).mapText!!.text = resources.getString(R.string.menu_bottom_map)
         (context as PoolFlowActivity).mapText!!.setTextColor(
            ContextCompat.getColor(
               requireContext(),
               R.color.bottom_menu_txt_map_dark
            )
         )
      }
   }

   private suspend fun collectUiState() {
      lifecycleScope.launch {
         repeatOnLifecycle(Lifecycle.State.STARTED) {
            connectorDetailViewModel.uiState.collect { uiState ->
               when (uiState) {
                  is ConnectorDetailUiState.Error -> {
                     // TODO: ?
                  }

                  ConnectorDetailUiState.Loading -> {
                     // TODO: ?
                  }

                  is ConnectorDetailUiState.Success -> {
                     with(binding) {
                        val seconds = "${
                           if (uiState.curr.seconds == "null" || uiState.curr.seconds.isNullOrBlank()) "--"
                           else secondsFormat(uiState.curr.seconds)
                        } ${resources.getString(R.string.minutes)}"
                        val kilowatts = "${
                           if (uiState.curr.kwh == "null" || uiState.curr.kwh.isNullOrBlank()) "--"
                           else kwhFormat(uiState.curr.kwh)
                        } ${resources.getString(R.string.kwh)}"
                        connectorDetailsChargeStatus.text = resources.getString(R.string.upload_in_progress)
                        connectorTitleTimeCharging.text = resources.getString(R.string.time_charging)
                        connectorDetailsChargeTime.text = seconds
                        connectorDetailsChargeKwh.text = kilowatts
                     }
                  }

                  else -> {}
               }
            }
         }
      }
      connectorDetailViewModel.startFlow()
   }

   private fun dialogResponseDefault(msg: String) {
      with(binding) {
         android.app.AlertDialog
            .Builder(requireContext())
            .setMessage(msg)
            .setPositiveButton("Aceptar") { _: DialogInterface, _: Int ->
               btnStartCharge.isEnabled = true
            }
            .setCancelable(false)
            .create()
            .show()
      }
   }

   private fun firstBundle() {
      with(binding) {
         with(requireArguments()) {
            poolId = this
               .getString(POOL_ID)
               .toString()
            titleName = this
               .getString(TITLE_NAME)
               .toString()
            titleAddress = this
               .getString(TITLE)
               .toString()
            latitudArg = this
               .getString(LATITUDE)
               .toString()
            longitudArg = this
               .getString(LONGITUDE)
               .toString()
            pistola = this
               .getInt(PISTOLA)
               .toString()
            equipo = this
               .getString(STATION_IDENTIFIER_TO_BODY)
               .toString()
            connectorId = this
               .getString(CONNECTOR_ID)
               .toString()
            chargerTitle.text = this
               .getString(CHARGER_NAME)
               .toString()
            connectorDetailImg.setImageResource(
               getConnectorImage(
                  this
                     .getString(CONNECTOR_TYPE)
                     .toString()
               )
            )
            connectorDetailConectorName.text = this
               .getString(CONNECTOR)
               .toString()
            connectorDetailConnectorTypeAlias.text = this
               .getString(STATION_NAME)
               .toString()
            connectorDetailTitle.text = setTitleColorFont()
         }
      }
   }

   @SuppressLint("ClickableViewAccessibility")
   private fun backpageBtn() {
      with(binding) {
         backIcon.setOnTouchListener { v: View, event: MotionEvent ->
            if (event.action == MotionEvent.ACTION_DOWN) {
               backIcon.alpha = 0.3f
               v.invalidate()
            } else if (event.action == MotionEvent.ACTION_UP) {
               backIcon.alpha = 1f
               v.invalidate()
               val bundle = bundleOf(
                  POOL_ID to poolId,
                  POOL_NAME to titleName,
                  POOL_ADDRESS to titleAddress,
                  LATITUDE to latitudArg,
                  LONGITUDE to longitudArg
               )
               navController.navigate(
                  R.id.pool_detail_fragment,
                  bundle
               )
            }
            true
         }
      }
   }

   private fun setTitleColorFont(): CharSequence? {
      val montserratBold = ResourcesCompat.getFont(
         requireContext(),
         R.font.nunito_bold
      )
      val montserratRegular = ResourcesCompat.getFont(
         requireContext(),
         R.font.nunito_medium
      )
      val titleNameSS = SpannableStringBuilder(titleName)
      val titleAddressSS = SpannableStringBuilder(titleAddress)
      titleNameSS.setSpan(
         CustomTypefaceSpan(
            "",
            montserratBold!!
         ),
         0,
         titleName!!.length,
         Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
      titleAddressSS.setSpan(
         CustomTypefaceSpan(
            "",
            montserratRegular!!
         ),
         0,
         titleAddress!!.length,
         Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
      )
      return TextUtils.concat(
         titleNameSS,
         " ",
         titleAddressSS
      )
   }

   private fun enableBtn() {
      with(binding) {
         btnCargaStopCharge.isEnabled = true
         btnStartCharge.isEnabled = true
      }
   }

   private fun visibilityProcess() {
      with(binding) {
         connectorDetailTitle.textSize = 19f
         backIcon.visibility = View.GONE
         btnStartCharge.visibility = View.INVISIBLE
         btnCargaStopCharge.visibility = View.VISIBLE
         connectorDetailsChargeDetails.visibility = View.VISIBLE
      }
   }

   private fun secondsFormat(seconds: String): String = "%.2f".format(
      (seconds.toFloat() * 5 / 3 / 100)
         .toBigDecimal()
         .setScale(
            2,
            RoundingMode.FLOOR
         )
   )

   private fun kwhFormat(kwh: String): String = "%.2f".format(
      kwh
         .toBigDecimal()
         .setScale(
            2,
            RoundingMode.FLOOR
         )
   )

   private fun settingTexts() {
      with(binding) {
         connectorDetailsChargeStatus.text = resources.getString(R.string.upload_in_progress)
         connectorTitleTimeCharging.text = resources.getString(R.string.time_charging)
         btnStartCharge.text = resources.getString(R.string.stop_charge)
         connectorDetailsChargeKwh.text = resources.getString(R.string.kwh)
         connectorDetailsChargeTime.text = resources.getString(R.string.minutes)
      }
   }
}
