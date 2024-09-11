package cl.codechunksdev.electriccarcharger2.ui.a_splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import cl.codechunksdev.electriccarcharger2.databinding.ActivitySplashBinding
import cl.codechunksdev.electriccarcharger2.ui.b_entry.activity.EntryFlowActivity
import cl.codechunksdev.electriccarcharger2.ui.c_pool.activity.PoolFlowActivity
import cl.codechunksdev.electriccarcharger2.utilities.Constants.DELAY_SPLASH
import cl.codechunksdev.electriccarcharger2.utilities.getSessionData
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

   private lateinit var binding: ActivitySplashBinding
   override fun onCreate(savedInstanceState: Bundle?) {
      AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
      super.onCreate(savedInstanceState)
      binding = ActivitySplashBinding.inflate(layoutInflater)
      setContentView(binding.root)
      lifecycleScope.launch {
         getSessionData(this@SplashScreenActivity).collect {
            delay(DELAY_SPLASH)
            intent = if (it.isLoged) Intent(
               this@SplashScreenActivity,
               PoolFlowActivity::class.java
            )
            else Intent(
               this@SplashScreenActivity,
               EntryFlowActivity::class.java
            )
            startActivity(intent)
            finish()
         }
      }
   }
}