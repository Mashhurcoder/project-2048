package com.mashhurbek.my2048game.screen.infoScreen

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mashhurbek.my2048game.R

import com.mashhurbek.my2048game.databinding.InfoScreenBinding

class InfoScreen:Fragment(R.layout.info_screen) {
    private val binding by viewBinding(InfoScreenBinding::class.java)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.telegramBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Nasrullayev04"))
            startActivity(intent)
        }
    }
}