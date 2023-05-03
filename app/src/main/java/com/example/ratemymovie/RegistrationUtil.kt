package com.example.ratemymovie

import androidx.core.util.PatternsCompat
import java.lang.NumberFormatException

object RegistrationUtil {

    var existingUsers = listOf("cosmicF", "cosmicY", "bob", "alice")

    fun validateUsername(username: String) : Boolean {
        if((username.isNotEmpty()) && username.length > 3 )
        {
            for(i in existingUsers.indices)
            {
                if(existingUsers[i] == username)
                {
                    return false
                }
            }
            return true
        }
        return false
    }

    // make sure meets security requirements (deprecated ones that are still used everywhere)
    // min length 8 chars
    // at least one digit
    // at least one capital letter
    // both passwords match
    // not empty
    fun validatePassword(password : String, confirmPassword: String) : Boolean {
        var hasNum = 0

        if((password.isNotEmpty()) && confirmPassword.isNotEmpty())
        {
            if(password == confirmPassword)
            {
                if(password.length > 7)
                {
                    val length = password.length
                    for(i in 0 until length)
                    {
                        val temp = password.substring(i,i+1)
                        try {
                            temp.toInt()
                        }catch (ex: NumberFormatException){
                            hasNum++
                        }
                        if(hasNum == length)
                        {
                            return false
                        }

                    }
                    if(password.lowercase() == password)
                    {
                        return false
                    }
                    return true
                }
                return false
            }
            return false
        }
        return false
    }

    // isn't empty
    fun validateName(name: String) : Boolean {
        if(name.isNotEmpty())
        {
            return true
        }
        return false
    }

    // isn't empty
    // make sure the email isn't used
    // make sure it's in the proper email format user@domain.tld
    fun validateEmail(email: String) : Boolean {
        return email.isNotEmpty() && PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

}