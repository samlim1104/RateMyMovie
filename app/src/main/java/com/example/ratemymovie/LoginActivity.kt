package com.example.ratemymovie

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.ratemymovie.databinding.ActivityLoginBinding
import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder

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
            binding.editTextTextPersonName.setText(intent?.getStringExtra(EXTRA_USERNAME))
            binding.editTextTextPassword.setText(intent?.getStringExtra((EXTRA_PASSWORD)))
        }
    }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonRegister.setOnClickListener{
            val registrationIntent = Intent(this, RegistrationActivity :: class.java)
            registrationIntent.putExtra(EXTRA_USERNAME, binding.editTextTextPersonName.text.toString())
            registrationIntent.putExtra(EXTRA_PASSWORD, binding.editTextTextPassword.text.toString())
            startRegistrationForResult.launch(registrationIntent)
        }
        binding.buttonLogin.setOnClickListener {
            Backendless.UserService.login(
                binding.editTextTextPersonName.text.toString(),
                binding.editTextTextPassword.text.toString(),
                object :AsyncCallback<BackendlessUser?>{
                    override fun handleResponse(user: BackendlessUser?) {
                        Log.d(TAG, " handleResponse: ${user?.getProperty("username")} has logged in")
                        val userId = user!!.objectId
                        val loanListIntent = Intent(it.context,MovieListActivity::class.java)
                            // loanListIntent.putExtra(MovieListActivity.EXTRA_OBJECT_ID, user.objectId)
                        it.context.startActivity(loanListIntent)
                    }

                    override fun handleFault(fault: BackendlessFault?) {

                        Log.d(TAG, "handleFault: ${fault?.message}")
                    }
                }  )
        }
    }
}

