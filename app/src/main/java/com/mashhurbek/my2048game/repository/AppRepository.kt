package com.mashhurbek.my2048game.repository

import android.content.Context
import android.content.SharedPreferences
import kotlin.random.Random

class AppRepository(private val context: Context) {

    private var matrix = emptyMatrix()
    private var moves = 0
    private var score = 0
    private val prefs: SharedPreferences =
        context.getSharedPreferences("game_prefs", Context.MODE_PRIVATE)


    init {
        addElement()
        addElement()
    }

    fun getMatrix() = matrix
    fun getMoves() = moves
    fun getScore() = score
    fun getBestScore() = prefs.getInt("best_score", 0)

    private fun addElement() {
        val ls = ArrayList<Int>(16)
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                if (matrix[i][j] == 0) ls.add(i * 4 + j)
            }
        }
        if (ls.isEmpty()) {
            return
        }

        val randomIndex = Random.nextInt(ls.size)
        matrix[ls[randomIndex] / 4][ls[randomIndex] % 4] = 2
    }


    private fun updateBestScore() {
        val best = getBestScore()
        if (score > best) {
            prefs.edit().putInt("best_score", score).apply()
        }
    }

    private fun makeMove(newMatrix: Array<Array<Int>>) {
        if (newMatrix.flatten() != matrix.flatten()) {
            matrix = newMatrix
            moves++
            updateBestScore()
            addElement()
        }
    }

    fun moveLeft() {
        val workerMatrix = emptyMatrix()
        for (i in 0 until 4) {
            var isAdded = false
            val ls = ArrayList<Int>()

            for (j in 0 until 4) {
                if (matrix[i][j] != 0) {
                    if (ls.isEmpty()) {
                        ls.add(matrix[i][j])
                    } else {
                        if (matrix[i][j] == ls.last() && !isAdded) {
                            val newValue = 2 * ls.last()
                            ls[ls.lastIndex] = newValue
                            score += newValue
                            isAdded = true
                        } else {
                            ls.add(matrix[i][j])
                            isAdded = false
                        }
                    }
                }
            }

            for (k in ls.indices) {
                workerMatrix[i][k] = ls[k]
            }
        }
        makeMove(workerMatrix)
    }

    fun moveRight() {
        val workerMatrix = emptyMatrix()
        for (i in 0 until 4) {
            var isAdded = false
            val ls = ArrayList<Int>()
            for (j in 3 downTo 0) {
                if (matrix[i][j] != 0) {
                    if (ls.isEmpty()) ls.add(matrix[i][j])
                    else {
                        if (matrix[i][j] == ls.last() && !isAdded) {
                            val newValue = 2 * ls.last()
                            ls[ls.lastIndex] = newValue
                            score += newValue
                            isAdded = true
                        } else {
                            ls.add(matrix[i][j])
                            isAdded = false
                        }
                    }
                }
            }
            ls.reverse()
            for (k in ls.indices)
                workerMatrix[i][4 - ls.size + k] = ls[k]
        }
        makeMove(workerMatrix)
    }

    fun moveUp() {
        val workerMatrix = emptyMatrix()
        for (j in 0 until 4) {
            var isAdded = false
            val ls = ArrayList<Int>()

            for (i in 0 until 4) {
                if (matrix[i][j] != 0) {
                    if (ls.isEmpty()) {
                        ls.add(matrix[i][j])
                    } else {
                        if (matrix[i][j] == ls.last() && !isAdded) {
                            val newValue = 2 * ls.last()
                            ls[ls.lastIndex] = newValue
                            score += newValue
                            isAdded = true
                        } else {
                            ls.add(matrix[i][j])
                            isAdded = false
                        }
                    }
                }
            }

            for (k in ls.indices) {
                workerMatrix[k][j] = ls[k]
            }
        }
        makeMove(workerMatrix)
    }

    fun moveDown() {
        val workerMatrix = emptyMatrix()
        for (j in 0 until 4) {
            var isAdded = false
            val ls = ArrayList<Int>()

            for (i in 3 downTo 0) {
                if (matrix[i][j] != 0) {
                    if (ls.isEmpty()) {
                        ls.add(matrix[i][j])
                    } else {
                        if (matrix[i][j] == ls.last() && !isAdded) {
                            val newValue = 2 * ls.last()
                            ls[ls.lastIndex] = newValue
                            score += newValue
                            isAdded = true
                        } else {
                            ls.add(matrix[i][j])
                            isAdded = false
                        }
                    }
                }
            }

            for (k in ls.indices) {
                workerMatrix[3 - k][j] = ls[k]
            }
        }
        makeMove(workerMatrix)
    }

    fun isGameOver(): Boolean {
        if (matrix.any { row -> row.contains(0) }) return false
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                val value = matrix[i][j]
                if (i < 3 && matrix[i + 1][j] == value) return false
                if (j < 3 && matrix[i][j + 1] == value) return false
            }
        }
        return true
    }

    private fun emptyMatrix() = arrayOf(
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )

    fun resetGame() {
        matrix = emptyMatrix()
        moves = 0
        score = 0
        addElement()
        addElement()
    }

    fun setMatrix(matrix: Array<Array<Int>>) {
        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                this.matrix[i][j] = matrix[i][j]
            }
        }
    }

    fun saveGame() {
        val editor = prefs.edit()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                editor.putInt("cell_${i}_${j}", matrix[i][j])
            }
        }
        editor.putInt("score", score)
        editor.putInt("moves", moves)

        editor.putBoolean("gameInProgress", moves > 0)
        editor.apply()
    }

    fun loadGame() {
        val newMatrix = emptyMatrix()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                newMatrix[i][j] = prefs.getInt("cell_${i}_${j}", 0)
            }
        }
        matrix = newMatrix
        score = prefs.getInt("score", 0)
        moves = prefs.getInt("moves", 0)
    }

    fun hasSavedGames():Boolean{
        return prefs.getBoolean("gameInProgress",false)
    }

    fun clearGame() {
        val editor = prefs.edit()
        for (i in 0 until 4) {
            for (j in 0 until 4) {
                editor.remove("cell_${i}_${j}")
            }
        }
        editor.remove("score")
        editor.remove("moves")
        editor.remove("gameInProgress")
        editor.apply()
    }
}
