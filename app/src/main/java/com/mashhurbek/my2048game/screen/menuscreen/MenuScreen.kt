package com.mashhurbek.my2048game.screen.menuscreen

import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.mashhurbek.my2048game.R
import com.mashhurbek.my2048game.databinding.MenuFragmentBinding
import com.mashhurbek.my2048game.repository.AppRepository

class MenuScreen : Fragment(R.layout.menu_fragment) {

    private val binding by viewBinding(MenuFragmentBinding::class.java)
    private lateinit var repository: AppRepository

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = AppRepository(requireContext())
        StartAnime(binding.mainText,R.anim.my_anime)

        if (repository.hasSavedGames()) {
            binding.continuebtn.visibility = View.VISIBLE
        } else {
            binding.continuebtn.visibility = View.GONE
        }

        binding.continuebtn.setOnClickListener {
            findNavController().navigate(
                R.id.action_menuScreen_to_gameScreen,
                Bundle().apply { putBoolean("isNewGame", false) }
            )
        }

        binding.playButton.setOnClickListener {
            findNavController().navigate(
                R.id.action_menuScreen_to_gameScreen,
                Bundle().apply { putBoolean("isNewGame", true) }
            )
        }

        binding.info.setOnClickListener {
            findNavController().navigate(R.id.action_menuScreen_to_infoScreen)
        }
    }
    private fun StartAnime(view:View,id:Int){
        val animation = AnimationUtils.loadAnimation(requireContext(),id)
        view.startAnimation(animation)
    }
}
