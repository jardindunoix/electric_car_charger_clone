package cl.codechunksdev.electriccarcharger2.ui.c_pool.activity

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cl.codechunksdev.electriccarcharger2.databinding.ActivityPoolFlowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PoolFlowActivity : AppCompatActivity() {

   var uploadIcon: ImageView? = null
   var uploadText: TextView? = null
   var historialIcon: ImageView? = null
   var mapIcon: ImageView? = null
   var settingsIcon: ImageView? = null
   var historialText: TextView? = null
   var mapText: TextView? = null
   var configText: TextView? = null
   private lateinit var binding: ActivityPoolFlowBinding

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      binding = ActivityPoolFlowBinding.inflate(layoutInflater)
      setContentView(binding.root)
      with(binding.bottomMenuInclude) {
         uploadIcon = iconUploadInclude.iconUpload
         settingsIcon = iconConfigInclude.imgSettings
         historialIcon = iconHistorialInclude.imgHistorial
         mapIcon = iconMapInclude.imgMap
         uploadText =iconUploadInclude.tvUpload
         configText = iconConfigInclude.tvSettings
         historialText = iconHistorialInclude.tvHistorial
         mapText = iconMapInclude.tvMap
         settingsIcon!!.isEnabled = true
         historialIcon!!.isEnabled = true
      }
   }
}