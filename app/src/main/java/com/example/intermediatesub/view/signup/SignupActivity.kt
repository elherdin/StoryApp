package com.example.intermediatesub.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.intermediatesub.databinding.ActivitySignupBinding
import com.example.intermediatesub.view.login.LoginActivity
import com.example.intermediatesub.view.utils.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        changeLoadingVisibility(false)
        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(300)
        val pass =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val passText =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val email =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val emailText =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)
        val name =
            ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val nameText = ObjectAnimator.ofFloat(binding.nameTextView, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, nameText, name, emailText, email, passText, pass, signup)
            start()
        }

    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        viewModel.resultRegister.observe(this) {
            val alertDialog: AlertDialog.Builder?
            if (it.error == true) {
                alertDialog = AlertDialog.Builder(this).apply {
                    setTitle("Oops !")
                    setMessage(it.message)
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.cancel()
                        finish()
                    }
                    create()
                }
                alertDialog.show()
            } else {
                alertDialog = AlertDialog.Builder(this).apply {
                    setTitle("Yippi !")
                    setMessage(it.message)
                    setNegativeButton("Next") { dialog, _ ->
                        dialog.dismiss()
                        dialog.cancel()
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    create()
                }
                alertDialog.show()
            }
        }
        binding.signupButton.setOnClickListener {
            changeLoadingVisibility(true)
            viewModel.registerUser(
                binding.nameEditText.text.toString(),
                binding.emailEditText.text.toString(),
                binding.passwordEditText.text.toString()
            )
        }
    }

    private fun changeLoadingVisibility(active: Boolean) {
        binding.loadingSignup.visibility = if (active) View.VISIBLE else View.GONE
    }
}