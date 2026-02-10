package com.mashhurbek.my2048game.screen.gamescreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

import by.kirich1409.viewbindingdelegate.viewBinding
import com.mashhurbek.my2048game.R
import com.mashhurbek.my2048game.databinding.GameScreenBinding
import com.mashhurbek.my2048game.repository.AppRepository
import com.mashhurbek.my2048game.screen.touchlistener.MyTouchListener
import com.mashhurbek.my2048game.utils.SideEnum
import com.mashhurbek.my2048game.utils.getBackgroundRes

class GameScreen : Fragment(R.layout.game_screen) {
    private val views = ArrayList<ArrayList<TextView>>()
    private lateinit var repository: AppRepository
    private val binding by viewBinding(GameScreenBinding::class.java)

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repository = AppRepository(requireContext())

        binding.btnRestart.setOnClickListener {
            repository.clearGame()
            repository.resetGame()
            showMatrix()
        }
        binding.menu.setOnClickListener {
            findNavController().navigateUp()
        }
        loadViews()

        val isNewGame = arguments?.getBoolean("isNewGame", false) ?: false

        if (isNewGame) {
            repository.clearGame()
            repository.resetGame()
        } else {
            if (repository.hasSavedGames()) {
                repository.loadGame()
            } else {
                repository.resetGame()
            }
        }

        showMatrix()

        val myTouchListener = MyTouchListener(requireContext())
        myTouchListener.setMoveListener {
            when (it) {
                SideEnum.RIGHT -> repository.moveRight()
                SideEnum.LEFT -> repository.moveLeft()
                SideEnum.UP -> repository.moveUp()
                SideEnum.DOWN -> repository.moveDown()
            }
            showMatrix()
            if (repository.isGameOver()) {
                repository.clearGame()
                repository.resetGame()
                showGameOverDialog()


            }
        }
        binding.container.setOnTouchListener(myTouchListener)
    }

    private fun loadViews() {
        for (i in 0 until 4) {
            val ls = ArrayList<TextView>(4)
            val linearRef = binding.container.getChildAt(i) as LinearLayoutCompat
            for (j in 0 until 4) {
                ls.add(linearRef.getChildAt(j) as TextView)
            }
            views.add(ls)
        }
    }

    override fun onPause() {
        super.onPause()
        repository.saveGame()
    }

    private fun showMatrix() {
        val matrix = repository.getMatrix()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                views[i][j].text = if (matrix[i][j] != 0) matrix[i][j].toString() else ""
                views[i][j].setBackgroundResource(matrix[i][j].getBackgroundRes())
            }
        }
        binding.tvScore.text = "Score: ${repository.getScore()}"
        binding.tvMoves.text = "Moves: ${repository.getMoves()}"
        binding.tvBest.text = "Best: ${repository.getBestScore()}"
    }

    private fun showGameOverDialog() {
        androidx.appcompat.app.AlertDialog.Builder(requireContext())
            .setTitle("Game Over")
            .setCancelable(false)
            .setMessage("Your score: ${repository.getScore()}\nBest: ${repository.getBestScore()}")
            .setPositiveButton("Restart") { _, _ ->
                repository.clearGame()
                repository.resetGame()
                showMatrix()
            }
            .setNegativeButton("Exit") { _, _ ->
                findNavController().navigateUp()
                repository.clearGame()
                repository.resetGame()
            }
            .show()
    }
}

