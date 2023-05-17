package com.example.ratemymovie

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.example.ratemymovie.databinding.ActivityLoginBinding
import com.backendless.exceptions.BackendlessFault

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "login"
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
    }
    val startRegistrationForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            binding.editTextLoginUsername.setText(intent?.getStringExtra(EXTRA_USERNAME))
            binding.editTextLoginPassword.setText(intent?.getStringExtra((EXTRA_PASSWORD)))
        }
    }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Backendless.initApp(this, Constants.APP_ID_BACKENDLESS, Constants.API_BACKENDLESS)
        binding.buttonLoginButton.setOnClickListener {
            Backendless.UserService.login(
                binding.editTextLoginUsername.text.toString(),
                binding.editTextLoginPassword.text.toString(),
                object : AsyncCallback<BackendlessUser?> {
                    override fun handleResponse(user: BackendlessUser?) {
                        Log.d(TAG, " handleResponse: ${user?.getProperty("username")} has logged in ${user?.email}")
                        if (user != null) {
                            val movieListIntent = Intent(it.context, MovieListActivity::class.java)
                            movieListIntent.putExtra(MovieListActivity.EXTRA_USER_ID, user.objectId)
                            it.context.startActivity(movieListIntent)
                            finish()
                        }
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        Log.d(TAG, "handleFault: ${fault?.message}")
                    }
                })
        }
            binding.textViewLoginSignup.setOnClickListener {
                val registrationIntent = Intent(this, RegistrationActivity::class.java)

                registrationIntent.putExtra(EXTRA_USERNAME, binding.editTextLoginUsername.text.toString())
                registrationIntent.putExtra(EXTRA_PASSWORD, binding.editTextLoginPassword.text.toString())
                startActivity(registrationIntent)
                startRegistrationForResult.launch(registrationIntent)
            }
        }
    }

