package com.example.intermediatesub.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.intermediatesub.data.pref.UserModel
import com.example.intermediatesub.databinding.ActivityLoginBinding
import com.example.intermediatesub.view.main.MainActivity
import com.example.intermediatesub.view.utils.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding
    private var alertDialog: AlertDialog.Builder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        false.changeLoadingVisibility()
        setupView()
        setupAction()
        playAnimation()
    }

    private fun playAnimation() {
        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(300)
        val pass =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val passText =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(300)
        val email =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val emailText =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(title, emailText, email, passText, pass, login)
            start()
        }

    }

    private fun Boolean.changeLoadingVisibility() {
        binding.loadingLogin.visibility = if (this) View.VISIBLE else View.GONE
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
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            viewModel.login(email, password)
            viewModel.resultLogin.observe(this) {
                if (it.error == true) {
                    alertDialog = AlertDialog.Builder(this).apply {
                        setTitle("Oops !")
                        setMessage(it.message)
                        setNegativeButton("Next") { dialog, _ ->
                            dialog.dismiss()
                        }
                    }
                    alertDialog?.create()
                    alertDialog?.show()
                } else {
                    alertDialog = AlertDialog.Builder(this).apply {
                        setTitle("Yeah !")
                        setMessage("Login Success")
                        setPositiveButton("Next") { dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    alertDialog?.create()
                    alertDialog?.show()
                    viewModel.saveSession(UserModel(email, it.loginResult?.token!!))
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        alertDialog = null
    }
}