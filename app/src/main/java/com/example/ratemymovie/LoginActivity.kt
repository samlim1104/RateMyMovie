package com.example.ratemymovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.ratemymovie.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    companion object{
        val TAG = "login"
        val EXTRA_USERNAME = "username"
        val EXTRA_PASSWORD = "password"
    }
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.buttonLogin.setOnClickListener {
            Backendless.UserService.login(
                binding.editTextTextPersonName.text.toString(),
                binding.editTextTextPassword.text.toString(),
                object :AsyncCallback<BackendlessUser?>{
                    override fun handleResponse(user: BackendlessUser?) {
                        Log.d(TAG, " handleResponse: ${user?.getProperty("username")} has logged in")
                        val userId = user!!.objectId
                        val loanListIntent = Intent(it.context,LoanListActivity::class.java)
                        loanListIntent.putExtra(LoanListActivity.EXTRA_OBJECT_ID, user.objectId)
                        it.context.startActivity(loanListIntent)
                    }

                    override fun handleFault(fault: BackendlessFault?) {

                        Log.d(TAG, "handleFault: ${fault?.message}")
                    }
                }  )
        }
    }
}

