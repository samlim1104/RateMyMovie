package com.example.ratemymovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ratemymovie.databinding.ActivityMyListBinding

class MyList : AppCompatActivity() {
    private lateinit var binding: ActivityMyListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyListBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}