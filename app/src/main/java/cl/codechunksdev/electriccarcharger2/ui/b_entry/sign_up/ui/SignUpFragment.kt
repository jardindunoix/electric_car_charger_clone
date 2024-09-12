package cl.codechunksdev.electriccarcharger2.ui.b_entry.sign_up.ui

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import cl.codechunksdev.electriccarcharger2.R
import cl.codechunksdev.electriccarcharger2.data.network.RetrofitInstance
import cl.codechunksdev.electriccarcharger2.databinding.FragmentSignUpBinding
import cl.codechunksdev.electriccarcharger2.domain.dto.sign_up.SigUpResponse
import cl.codechunksdev.electriccarcharger2.domain.dto.sign_up.SignUpPojo
import cl.codechunksdev.electriccarcharger2.ui.b_entry.service.containNumber
import cl.codechunksdev.electriccarcharger2.ui.b_entry.service.containSymbol
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
class SignUpFragment : Fragment() {

   private var showConfirmPass = false
   private var showPass = false
   private var isValidFields = false
   private var emailGlobal = ""
   private var passwordGlobal = ""
   private var passwordConfirmGlobal = ""
   private var _binding: FragmentSignUpBinding? = null
   private val binding get() = _binding!!

   override fun onCreateView(
      inflater: LayoutInflater,
      container: ViewGroup?,
      savedInstanceState: Bundle?
   ): View {
      _binding = FragmentSignUpBinding.inflate(
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
      isValidFields = false;
      showConfirmPass = false;
      showPass = false;
      emailGlobal = arguments
         ?.getString(KEY_EMAIL)
         .toString()
      with(binding) {
         with(editTextEmailInclude) {
            with(editTextPasswordInclude) {
               with(editTextConfirmPasswordInclude) {
                  with(buttonNextInclude) {
                     editTextEmail.setText(emailGlobal);
                     val textWatcher: TextWatcher = object : TextWatcher {
                        override fun beforeTextChanged(
                           s: CharSequence?,
                           start: Int,
                           count: Int,
                           after: Int
                        ) {
                        }

                        override fun afterTextChanged(s: Editable?) {}
                        override fun onTextChanged(
                           s: CharSequence?,
                           start: Int,
                           before: Int,
                           count: Int
                        ) {
                           isValidFields = isValidEmail(editTextEmail.text.toString()) && editTextConfirmPassword.text.toString().length >= 8 && editTextConfirmPassword.text.toString() == editTextPassword.text.toString()
                           if (!isValidFields) {
                              buttonNext.isEnabled = false
                              buttonNext.setBackgroundResource(R.drawable.bg_btn_round_corner_normal)
                              buttonNext.alpha = 0.3f
                           } else if (binding.textTermsAndConditionsInclude.acceptTermsSwitch.isChecked) {
                              buttonNext.isEnabled = true
                              buttonNext.setBackgroundResource(R.drawable.bg_btn_round_corner_normal)
                              buttonNext.alpha = 1f
                           }
                        }
                     }
                     editTextEmail.addTextChangedListener(textWatcher)
                     editTextPassword.transformationMethod = CustomPasswordTransformationMethod()
                     editTextPassword.addTextChangedListener(textWatcher)
                     editTextPasswordIconHideInclude.showPassword.setOnClickListener { _ ->
                        if (showPass) {
                           showPass = false;
                           editTextPasswordIconHideInclude.eyeIcon.setImageDrawable(
                              ContextCompat.getDrawable(
                                 requireContext(),
                                 R.drawable.icon_password_hide
                              )
                           );
                           editTextPassword.transformationMethod = PasswordTransformationMethod();
                           editTextPassword.transformationMethod = CustomPasswordTransformationMethod();
                           editTextPassword.letterSpacing = 0.3f;
                           editTextPassword.setHint(R.string.component_edit_text_confirm_password_hint);
                        } else {
                           showPass = true;
                           editTextPasswordIconHideInclude.eyeIcon.setImageDrawable(
                              ContextCompat.getDrawable(
                                 requireContext(),
                                 R.drawable.icon_password_show
                              )
                           );
                           editTextPassword.transformationMethod = HideReturnsTransformationMethod();
                           editTextPassword.letterSpacing = 0f;
                           editTextPassword.setHint(R.string.empty_line);
                        }
                     };
                     editTextConfirmPassword.transformationMethod = CustomPasswordTransformationMethod()
                     editTextConfirmPassword.addTextChangedListener(textWatcher)
                     editTextConfirmPasswordIconHideInclude.showPassword.setOnClickListener { _ ->
                        if (showConfirmPass) {
                           showConfirmPass = false;
                           editTextConfirmPasswordIconHideInclude.eyeIcon.setImageDrawable(
                              ContextCompat.getDrawable(
                                 requireContext(),
                                 R.drawable.icon_password_hide
                              )
                           );
                           editTextConfirmPassword.transformationMethod = PasswordTransformationMethod();
                           editTextConfirmPassword.transformationMethod = CustomPasswordTransformationMethod();
                           editTextConfirmPassword.letterSpacing = 0.3f;
                           editTextConfirmPassword.setHint(R.string.component_edit_text_confirm_password_hint);
                        } else {
                           showConfirmPass = true;
                           editTextConfirmPasswordIconHideInclude.eyeIcon.setImageDrawable(
                              ContextCompat.getDrawable(
                                 requireContext(),
                                 R.drawable.icon_password_show
                              )
                           );
                           editTextConfirmPassword.transformationMethod = HideReturnsTransformationMethod();
                           editTextConfirmPassword.letterSpacing = 0f;
                           editTextConfirmPassword.setHint(R.string.empty_line);
                        }
                     };
                     buttonNext.isEnabled = false
                     buttonNext.setOnTouchListener { v, event ->
                        when (event.action) {
                           android.view.MotionEvent.ACTION_DOWN -> {
                              v.background = ContextCompat.getDrawable(
                                 requireContext(),
                                 R.drawable.bg_btn_round_corner_pressed
                              )
                              v.invalidate();
                           }

                           android.view.MotionEvent.ACTION_UP -> {
                              v.background = ContextCompat.getDrawable(
                                 requireContext(),
                                 R.drawable.bg_btn_round_corner_normal
                              )
                              v.invalidate();
                              if (!containNumber(editTextConfirmPassword.text.toString()) || !containSymbol(editTextConfirmPassword.text.toString())) {
                                 android.app.AlertDialog
                                    .Builder(requireContext())
                                    .setTitle(resources.getString(R.string.registry_error_title))
                                    .setMessage(resources.getString(R.string.registry_error_message))
                                    .setPositiveButton(resources.getString(R.string.registry_positive_text)) {
                                          dialogInterface: DialogInterface,
                                          _: Int,
                                       ->
                                       dialogInterface.dismiss()
                                    }
                                    .create()
                                    .show()
                              } else {
                                 lifecycleScope.launch {
                                    emailGlobal = editTextEmail.text.toString()
                                    passwordGlobal = editTextPassword.text.toString()
                                    val signUpPojo = SignUpPojo(
                                       emailGlobal,
                                       passwordGlobal,
                                       COMPANY_ID
                                    )
                                    val response: Response<SigUpResponse> = RetrofitInstance.signUp.singUp(signUpPojo)
                                    val token: String = response.body()?.token.toString()
                                    saveValues(
                                       true,
                                       token,
                                       requireContext()
                                    )
                                    gotoPoolFlowActivity(
                                       requireActivity(),
                                       false
                                    )
                                 }
                              }
                           }
                        }
                        true
                     }
                     textTermsAndConditionsInclude.acceptTermsSwitch.setOnCheckedChangeListener { _, isChecked: Boolean ->
                        buttonNext.isEnabled = isValidFields
                        if (isValidFields && isChecked) {
                           buttonNext.setBackgroundResource(R.drawable.bg_btn_round_corner_normal)
                           buttonNext.alpha = 1f
                           buttonNext.isEnabled = true
                        } else {
                           buttonNext.setBackgroundResource(R.drawable.bg_btn_round_corner_normal)
                           buttonNext.alpha = 0.3f
                           buttonNext.isEnabled = false
                        }
                     }
                  }
               }
            }
         }
         signUpMainLayout.setOnClickListener { _: View -> activity?.hideKeyboard() }
      }
   }
}