package cl.codechunksdev.electriccarcharger2.ui.b_entry.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cl.codechunksdev.electriccarcharger2.databinding.ActivityEntryFlowBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntryFlowActivity : AppCompatActivity() {

   override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      val binding = ActivityEntryFlowBinding.inflate(layoutInflater)
      setContentView(binding.root)

   }
}