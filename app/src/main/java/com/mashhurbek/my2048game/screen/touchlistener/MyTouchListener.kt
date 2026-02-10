package com.mashhurbek.my2048game.screen.touchlistener

import android.content.Context
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import com.mashhurbek.my2048game.utils.SideEnum
import kotlin.math.abs
import kotlin.math.sqrt

class MyTouchListener(private val context: Context):View.OnTouchListener {

    private val gestureDetector = GestureDetector(context,MyGesturDetector())
    private var moveListener : ((SideEnum) -> Unit)?= null

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }

    inner class MyGesturDetector : SimpleOnGestureListener(){
        override fun onFling(e1: MotionEvent?, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            if (e1 == null) return true
            val deltaX = abs(e1.x - e2.x)
            val deltaY = abs(e1.y - e2.y)
            if (sqrt(Math.pow(deltaX.toDouble(), 2.0) + Math.pow(deltaY.toDouble(), 2.0)) < 100) return true

            if (deltaX > deltaY) {
                // horizontal
                if (e2.x > e1.x) moveListener?.invoke(SideEnum.RIGHT)
                else moveListener?.invoke(SideEnum.LEFT)
            } else {
                // vertical
                if (e2.y > e1.y) moveListener?.invoke(SideEnum.DOWN)
                else moveListener?.invoke(SideEnum.UP)
            }
            return true
        }
    }
    fun setMoveListener(block:(SideEnum)->Unit){
        moveListener = block
    }
}