package com.example.savekitty

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import android.view.View
import java.lang.Exception

class GameView (var c:Context, var gameTask: GameTask):View(c){

    private var myPaint:Paint? = null
    private var speed = 1
    private var time = 0
    private var score = 0
    private var myCatPosition = 0
    private val otherIcons = ArrayList<HashMap<String,Any>>()

    var viewWidth = 0
    var viewHeight = 0
    init{
        myPaint = Paint()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        viewWidth = this.measuredWidth
        viewHeight = this.measuredHeight

        if(time %  700 < 10 + speed){
            val map = HashMap<String,Any>()
            map["lane"] = (0..2).random()
            map["startTime"] = time
            otherIcons.add(map)
        }

        time = time + 10 + speed
        val catWidth = viewWidth /5 * 2
        val catHeight = catWidth + 20
        myPaint!!.style = Paint.Style.FILL
        val d = resources.getDrawable(R.drawable.cat,null)

        d.setBounds(
            myCatPosition *  viewWidth / 3 + viewWidth / 15 + 25,
            viewHeight - catHeight - 2,
            myCatPosition * viewWidth / 3 + viewWidth / 15 + catWidth - 25,
            viewHeight -2
        )

        d.draw(canvas!!)
        myPaint!!.color = Color.GREEN
        var highScore = 0

        for(i in otherIcons.indices){
            try {
                val iconX = otherIcons[i]["lane"] as  Int * viewWidth / 3 + viewWidth / 15
                var iconY = time - otherIcons[i]["startTime"] as Int
                val d2 = resources.getDrawable(R.drawable.spike,null)
                d2.setBounds(
                    iconX + 25 , iconY - catHeight, iconX + catWidth -25, iconY
                )
                d2.draw(canvas)

                if(otherIcons[i]["lane"] as Int == myCatPosition){
                    if(iconY > viewHeight - 2 - viewHeight && iconY < viewHeight -2 ){
                        gameTask.closeGame(score)
                    }
                }

                if(iconY > viewHeight + catHeight){
                    otherIcons.removeAt(i)
                    score ++
                    speed = 1 + Math.abs(score / 8)
                    if(score > highScore){
                        highScore =  score
                    }
                }
            }
            catch (e:Exception){
                e.printStackTrace()
            }
        }

        myPaint!!.color = Color.WHITE
        myPaint!!.textSize = 40f
        canvas.drawText("Score: $score", 80f, 80f, myPaint!!)
        canvas.drawText("Speed: $speed", 30f, 80f, myPaint!!)

        invalidate()

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_DOWN -> {
                val x1 = event.x
                if(x1 < viewWidth/2){
                    if(myCatPosition > 0){
                        myCatPosition--
                    }
                }
                if(x1 > viewWidth/2){
                    if(myCatPosition<2) {
                        myCatPosition++
                    }
                }

                invalidate()
            }
            MotionEvent.ACTION_UP -> {}

        }
        return true
    }

}