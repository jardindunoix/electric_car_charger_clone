package cl.codechunksdev.electriccarcharger2.ui.c_pool.settings.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import cl.codechunksdev.electriccarcharger2.databinding.FragmentSettingsBinding
import cl.codechunksdev.electriccarcharger2.ui.c_pool.service.bottomMenuFunc
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

   private var _binding: FragmentSettingsBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?,
   ): View {
      _binding = FragmentSettingsBinding.inflate(inflater, container, false)
      return binding.root
   }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
      super.onViewCreated(view, savedInstanceState)
      bottomMenuFunc(requireActivity(), this) { findNavController() }
   }
}