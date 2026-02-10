package com.mashhurbek.my2048game.utils

import com.mashhurbek.my2048game.R

fun Int.getBackgroundRes() : Int =
    when(this) {
        0 -> R.drawable.bg_empty
        2 -> R.drawable.bg_2
        4 -> R.drawable.bg_4
        8 -> R.drawable.bg_8
        16 -> R.drawable.bg_16
        32 -> R.drawable.bg_32
        64 -> R.drawable.bg_64
        128 -> R.drawable.bg_128
        256 -> R.drawable.bg_256
        512 -> R.drawable.bg_512
        1024 -> R.drawable.bg_1024
        else -> R.drawable.bg_2048
    }