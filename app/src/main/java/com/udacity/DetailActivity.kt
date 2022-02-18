package com.udacity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.udacity.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.contentDetail.statusTv.text = intent.getStringExtra("status")
        binding.contentDetail.filenameTv.text = intent.getStringExtra("filename")
        binding.contentDetail.button.setOnClickListener {
            finish()
        }
    }


}
