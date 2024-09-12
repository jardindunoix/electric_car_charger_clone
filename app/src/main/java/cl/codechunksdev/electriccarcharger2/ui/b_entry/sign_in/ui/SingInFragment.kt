package cl.codechunksdev.electriccarcharger2.ui.b_entry.sign_in.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.data.network.RetrofitInstance
import cl.codechunksdev.electriccarcharger2.databinding.FragmentSignInBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.sign_in.SigInResponse
import cl.codechunksdev.electriccarcharger2.domain.dto.sign_in.SignInPojo
import cl.codechunksdev.electriccarcharger2.ui.b_entry.service.isValidEmail
import cl.codechunksdev.electriccarcharger2.utilities.Constants.COMPANY_ID
import cl.codechunksdev.electriccarcharger2.utilities.Constants.KEY_EMAIL
import cl.codechunksdev.electriccarcharger2.utilities.common_ui_service.CustomPasswordTransformationMethod
import cl.codechunksdev.electriccarcharger2.utilities.gotoPoolFlowActivity
import cl.codechunksdev.electriccarcharger2.utilities.hideKeyboard
import cl.codechunksdev.electriccarcharger2.utilities.saveValues
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.Response


@AndroidEntryPoint
class SingInFragment : Fragment() {

   private var showPass = false
   private var isValidFields = false
   private var emailGlobal = ""
   private var passwordGlobal = ""
   private var _binding: FragmentSignInBinding? = null
   private val binding get() = _binding!!
   private lateinit var navController: NavController
   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentSignInBinding.inflate(
         inflater,
         container,
         false
      )
      return binding.root
   }

   @SuppressLint("ClickableViewAccessibility")
   override fun onViewCreated(
      view: View,
      savedInstanceState: Bundle?
   ) {
      super.onViewCreated(
         view,
         savedInstanceState
      )
      emailGlobal = arguments
         ?.getString(KEY_EMAIL)
         .toString()
      showPass = false;
      isValidFields = false;
      with(binding) {
         with(buttonNextInclude) {
            with(editTextPasswordInclude) {
               editTextEmailInclude.editTextEmail.setText(emailGlobal);
               buttonNext.isEnabled = false;
               val textWatcher = object : TextWatcher {
                  override fun beforeTextChanged(
                     charSequence: CharSequence,
                     i: Int,
                     i1: Int,
                     i2: Int
                  ) {
                  }

                  override fun afterTextChanged(editable: Editable) {}
                  override fun onTextChanged(
                     charSequence: CharSequence,
                     i: Int,
                     i1: Int,
                     i2: Int
                  ) {
                     isValidFields = (isValidEmail(editTextEmailInclude.editTextEmail.text.toString())) && (editTextPasswordInclude.editTextPassword.text.toString().length >= 8)
                     buttonNext.isEnabled = isValidFields
                     buttonNext.setBackgroundResource(R.drawable.bg_btn_round_corner_normal)
                     buttonNext.alpha = if (isValidFields) 1f else 0.3f
                  }
               }
               buttonNext.setOnTouchListener { v: View, event: MotionEvent ->
                  when (event.action) {
                     MotionEvent.ACTION_DOWN -> {
                        v.background = ContextCompat.getDrawable(
                           requireContext(),
                           R.drawable.bg_btn_round_corner_pressed
                        )
                        v.invalidate();
                     }

                     MotionEvent.ACTION_UP -> {
                        v.background = ContextCompat.getDrawable(
                           requireContext(),
                           R.drawable.bg_btn_round_corner_normal
                        )
                        v.invalidate();
                        textViewWrongCredentials.text = ""
                        lifecycleScope.launch {
                           emailGlobal = editTextEmailInclude.editTextEmail.text.toString()
                           passwordGlobal = editTextPasswordInclude.editTextPassword.text.toString()
                           val signInPojo = SignInPojo(
                              emailGlobal,
                              passwordGlobal,
                              COMPANY_ID
                           )
                           val response: Response<SigInResponse> = RetrofitInstance.signIn.singIn(signInPojo)
                           val token: String = response.body()?.token.toString()
                           if (response.isSuccessful && response.code() == 200) {
                              saveValues(
                                 true,
                                 token,
                                 requireContext()
                              )
                              gotoPoolFlowActivity(
                                 requireActivity(),
                                 false
                              )
                           } else {
                              textViewWrongCredentials.setText(R.string.fragment_signin_wrong_credentials)
                           }
                        }
                     }
                  }
                  true;
               }
               editTextEmailInclude.editTextEmail.addTextChangedListener(textWatcher)
               editTextPassword.addTextChangedListener(textWatcher)
               editTextPassword.transformationMethod = CustomPasswordTransformationMethod()
               editTextPasswordIconHideInclude.showPassword.setOnClickListener { _ ->
                  if (!showPass) {
                     showPass = true
                     editTextPasswordIconHideInclude.eyeIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                           requireContext(),
                           R.drawable.icon_password_show
                        )
                     )
                     editTextPassword.transformationMethod = HideReturnsTransformationMethod()
                     editTextPassword.letterSpacing = 0f
                     editTextPassword.setHint(R.string.empty_line)
                  } else {
                     showPass = false
                     editTextPasswordIconHideInclude.eyeIcon.setImageDrawable(
                        ContextCompat.getDrawable(
                           requireContext(),
                           R.drawable.icon_password_hide
                        )
                     )
                     editTextPassword.transformationMethod = PasswordTransformationMethod()
                     editTextPassword.transformationMethod = CustomPasswordTransformationMethod()
                     editTextPassword.letterSpacing = 0.3f
                     editTextPassword.setHint(R.string.component_edit_text_confirm_password_hint)
                  }
               }
            }
         }
         signInMainLayout.setOnClickListener { _: View -> activity?.hideKeyboard() }
      }
   }
}