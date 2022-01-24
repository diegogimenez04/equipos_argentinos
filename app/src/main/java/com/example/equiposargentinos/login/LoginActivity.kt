package com.example.equiposargentinos.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.equiposargentinos.main.MainActivity
import com.example.equiposargentinos.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseUser

class LoginActivity: AppCompatActivity() {
    lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]

        viewModel.state.observe(this) {
            when {
                (!it.loginError && it.user != null) -> {
                    logUser(it.user)
                    startActivity(Intent(this, MainActivity::class.java))
                }
                (!it.loginError && it.user == null) -> {
                    Toast.makeText(this, "Loged out.", Toast.LENGTH_SHORT).show()
                    binding.lgEditEmail.visibility = View.VISIBLE
                    binding.lgEditPassword.visibility = View.VISIBLE
                    binding.lgButtonLogin.text = "LOGIN"
                }
                (it.loginError) -> {
                    Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        binding.lgButtonLogin.setOnClickListener {
            if(viewModel.state.value?.user == null) {
                val email = binding.lgEditEmail.text.toString()
                val password = binding.lgEditPassword.text.toString()
                val sent = viewModel.login(email, password)
                if (!sent) Toast
                    .makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT)
                    .show()
            } else {
                viewModel.logout()
            }
        }
    }

    private fun logUser(user: FirebaseUser) {
        user.apply {
            Toast.makeText(this@LoginActivity, "Logged!", Toast.LENGTH_SHORT).show()
            email?.let { Log.d("login", it) }
            isEmailVerified.let { Log.d("login", it.toString()) }
            uid.let { Log.d("login", it) }
        }
    }
}