package cl.codechunksdev.electriccarcharger2.ui.b_entry.login.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.data.network.RetrofitInstance
import cl.codechunksdev.electriccarcharger2.databinding.FragmentLoginBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.login.EmailVerified
import cl.codechunksdev.electriccarcharger2.ui.b_entry.service.isValidEmail
import cl.codechunksdev.electriccarcharger2.utilities.Constants.COMPANY_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.KEY_EMAIL
import cl.codechunksdev.electriccarcharger2.utilities.gotoPoolFlowActivity
import cl.codechunksdev.electriccarcharger2.utilities.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response

val Context.dataStore by preferencesDataStore(name = "SESSION")

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
//    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        navController = Navigation.findNavController(view)
        with(binding) {
            with(buttonNextInclude) {
                buttonNext.isEnabled = false
                buttonNext.setOnTouchListener { v: View, event: MotionEvent ->
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            v.background = androidx.core.content.ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_btn_round_corner_pressed
                            )
                            v.invalidate()
                        }

                        MotionEvent.ACTION_UP -> {
                            v.background = androidx.core.content.ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.bg_btn_round_corner_normal
                            )
                            v.invalidate()
                            lifecycleScope.launch {
                                val email = editTextEmailInclude.editTextEmail.text.toString()
                                val response: Response<EmailVerified> =
                                    RetrofitInstance.login.verifiyEmail(
                                        COMPANY_ID,
                                        email
                                    )
                                val bundle = bundleOf(KEY_EMAIL to email)
                                if (response.isSuccessful && response.code() == 200)
                                    findNavController().navigate(
                                    R.id.action_nav_login_to_nav_sign_in,
                                    bundle
                                )
                                else findNavController().navigate(
                                    R.id.action_nav_login_to_nav_sign_up,
                                    bundle
                                )
                            }
                        }
                    }
                    true
                }
                editTextEmailInclude.editTextEmail.addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        // TODO
                    }

                    override fun afterTextChanged(editable: Editable) {}
                    override fun onTextChanged(
                        charSequence: CharSequence,
                        i: Int,
                        i1: Int,
                        i2: Int
                    ) {
                        val isValid = isValidEmail(charSequence)
                        buttonNext.isEnabled = isValid
                        buttonNext
                            .setBackgroundResource(R.drawable.bg_btn_round_corner_normal)
                        buttonNext.alpha = if (isValid) 1f else 0.3f
                    }

                })
                skipRegistryText.setOnTouchListener { v: View, event: MotionEvent ->
                    event.actionIndex
                    when (event.action) {
                        MotionEvent.ACTION_DOWN -> {
                            binding.skipRegistryText.setTextColor(
                                androidx.core.content.ContextCompat.getColor(
                                    requireContext(),
                                    R.color.blue_1
                                )
                            )
                            v.invalidate()
                        }

                        MotionEvent.ACTION_UP -> {
                            binding.skipRegistryText.setTextColor(
                                androidx.core.content.ContextCompat.getColor(
                                    requireContext(),
                                    R.color.blue_2
                                )
                            )
                            v.invalidate()
                            gotoPoolFlowActivity(requireActivity(), true)
                        }
                    }
                    true
                }
            }
            loginMainLayout.setOnClickListener { _: View -> activity?.hideKeyboard() }
        }
    }

}
