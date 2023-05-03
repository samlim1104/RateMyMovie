package com.example.ratemymovie

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.service.controls.ControlsProviderService
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.example.ratemymovie.databinding.ActivityRegistrationBinding

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val username = intent.getStringExtra(LoginActivity.EXTRA_USERNAME) ?: ""
        val password = intent.getStringExtra(LoginActivity.EXTRA_PASSWORD) ?: ""

        binding.editTextRegUsername.setText(username)
        binding.editTextRegPassword.setText((password))

        binding.buttonRegRegisterButton.setOnClickListener {
            val password = binding.editTextRegPassword.text.toString()
            val confirm = binding.editTextConfPass.text.toString()
            val username = binding.editTextRegUsername.text.toString()
            val email = binding.editTextRegEmail.text.toString()
            if (RegistrationUtil.validatePassword(
                    password,
                    confirm
                ) && RegistrationUtil.validateUsername(username)
            ){

                val resultIntent = Intent().apply {
                    //apply {putExtra()} is doing the sane thing as resultIntent.putExtra()
                    putExtra(
                        LoginActivity.EXTRA_USERNAME,
                        binding.editTextRegUsername.text.toString()
                    )
                    putExtra(LoginActivity.EXTRA_PASSWORD, password)
                }
                val user = BackendlessUser()
                user.setProperty("email", email)
                user.setProperty("username", username)
                user.password = password.toString()

                Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(registeredUser: BackendlessUser?) {
                        Log.d(
                            ControlsProviderService.TAG,
                            "handleResponse : ${user?.getProperty("username")} has been registered"
                        )
                    }

                    override fun handleFault(fault: BackendlessFault) {
                        Log.d(TAG, "handleFault: ${fault?.message}")
                    }
                })
                finish()
            }
        }
    }
}