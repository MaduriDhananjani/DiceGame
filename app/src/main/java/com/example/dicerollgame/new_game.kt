package com.example.dicerollgame

import android.content.ClipData.Item
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView

class new_game : AppCompatActivity() {

    var userScore = mutableListOf<Dice>(Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0))
    var userLockedDice = mutableListOf<Dice>()
    var computerScore = mutableListOf<Dice>(Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0))
    var computerLockedDice = mutableListOf<Dice>()


    var maxNoRounds = 3
    var userRoundAmount = 0
    var computerRoundAmount = 3
    var canComputerPlay = true

    var userTotal = 0
    var computerTotal = 0

    var winScore = 101
    var computerWinCount = 0
    var userWinCount = 0

    var isGameDraw = false
    var hasGameStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_game)
        val btn1NG = findViewById<Button>(R.id.button1_NG)
        val playerText = findViewById<TextView>(R.id.textView3)
        val computerText = findViewById<TextView>(R.id.textView)
        val setScoreButton = findViewById<Button>(R.id.button4)
        val setScoreInput = findViewById<EditText>(R.id.editTextNumber)

        val diceImage6 = findViewById<ImageView>(R.id.imageView6)
        val diceImage7 = findViewById<ImageView>(R.id.imageView7)
        val diceImage8 = findViewById<ImageView>(R.id.imageView8)
        val diceImage9 = findViewById<ImageView>(R.id.imageView9)
        val diceImage10 = findViewById<ImageView>(R.id.imageView10)
        val imageViewList = mutableListOf<ImageView>(diceImage6, diceImage7, diceImage8, diceImage9, diceImage10)

        btn1NG.setOnClickListener {
            hasGameStarted = true
            rollDiceNG()
            if(canComputerPlay) {
                rollDiceComputerNG(false)
                canComputerPlay = false
            }

            if(userRoundAmount == 3) {
                for(image in imageViewList) {
                    image.foreground = ContextCompat.getDrawable(this, android.R.color.transparent)
                }
                score()
                canComputerPlay = true
                val popUpWin = AlertDialog.Builder(this)
                popUpWin.setMessage("Round is over. Scores are added to the total")
                popUpWin.setPositiveButton("OK") { dialog, _ ->
                    playerText.text = "User 3"
                    computerRoundAmount = 3
                    userRoundAmount = 0
                    computerLockedDice = mutableListOf<Dice>()
                    userLockedDice = mutableListOf<Dice>()
                    computerText.text = "Computer " + computerRoundAmount.toString()
                    dialog.dismiss()
                }
                val popUp = popUpWin.create()
                popUp.setCancelable(false)
                popUp.setCanceledOnTouchOutside(false)
                popUp.show()
            }

            playerText.text = "User " + (3 - userRoundAmount).toString()
            computerText.text = "Computer " + computerRoundAmount.toString()

        }

        val scoreBtn = findViewById<Button>(R.id.button2)
        scoreBtn.setOnClickListener {
            canComputerPlay = true
            val popUpWin = AlertDialog.Builder(this)
            popUpWin.setMessage("Round is over. Scores are added to the total")
            popUpWin.setPositiveButton("OK") { dialog, _ ->
                if(computerRoundAmount > 0) {
                    rollDiceComputerNG(true)
                }
                score()
                playerText.text = "User 3"
                computerRoundAmount = 3
                userRoundAmount = 0
                computerText.text = "Computer " + computerRoundAmount.toString()
                computerLockedDice = mutableListOf<Dice>()
                userLockedDice = mutableListOf<Dice>()
                dialog.dismiss()
            }
            val popUp = popUpWin.create()
            popUp.setCancelable(false)
            popUp.setCanceledOnTouchOutside(false)
            popUp.show()

        }

        setScoreButton.setOnClickListener {
            if(!hasGameStarted){
                val winningScoreUpdated = setScoreInput.text.toString()
                val winningScoreText = findViewById<TextView>(R.id.textView5)
                winningScoreText.text = winningScoreUpdated
                winScore = Integer.parseInt(winningScoreUpdated)
            }

        }
    }


    private fun rollDiceNG() {
        val diceNG = DiceNG(6)
        val diceImage6 = findViewById<ImageView>(R.id.imageView6)
        val diceImage7 = findViewById<ImageView>(R.id.imageView7)
        val diceImage8 = findViewById<ImageView>(R.id.imageView8)
        val diceImage9 = findViewById<ImageView>(R.id.imageView9)
        val diceImage10 = findViewById<ImageView>(R.id.imageView10)
        val imageViewList = mutableListOf<ImageView>(diceImage6, diceImage7, diceImage8, diceImage9, diceImage10)

        if(!isGameDraw) {
            userRoundAmount = userRoundAmount + 1

            for((i, imageView) in imageViewList.withIndex()) {
                val randomNG = diceNG.rollNG()

                val isDiceLocked = userLockedDice.find { it.index == i }

                if(isDiceLocked == null) {
                    imageView.setImageResource(getImageForRandomNumber(randomNG))
                    userScore[i] = Dice(i, randomNG)
                }


                imageView.setOnClickListener {
                    if(isDiceLocked == null) {
                        imageView.foreground = ContextCompat.getDrawable(this, R.drawable.image_overlay)
                        userLockedDice.add(Dice(i, randomNG))
                    }
                }
            }
        } else {
            for((i, imageView) in imageViewList.withIndex()) {
                val randomNG = diceNG.rollNG()
                imageView.setImageResource(getImageForRandomNumber(randomNG))
                userScore[i] = Dice(i, randomNG)
            }
        }
    }

    private fun getImageForRandomNumber(i: Int): Int {
        return when (i) {
            1 -> R.drawable.dice1
            2 -> R.drawable.dice2
            3 -> R.drawable.dice3
            4 -> R.drawable.dice4
            5 -> R.drawable.dice5
            6 -> R.drawable.dice6
            else -> R.drawable.dice1
        }
    }

    private fun rollDiceComputerNG(isScoreClicked: Boolean) {
        val diceNG = DiceNG(6)

        val diceImage1 = findViewById<ImageView>(R.id.imageView)
        val diceImage2 = findViewById<ImageView>(R.id.imageView2)
        val diceImage3 = findViewById<ImageView>(R.id.imageView3)
        val diceImage4 = findViewById<ImageView>(R.id.imageView4)
        val diceImage5 = findViewById<ImageView>(R.id.imageView5)

        val imageViewList = mutableListOf<ImageView>(diceImage1, diceImage2, diceImage3, diceImage4, diceImage5)

        if(!isGameDraw) {
            if(!isScoreClicked) {
                val noOfRoundsToReRoll = (1..maxNoRounds).random()
                computerRoundAmount = computerRoundAmount - noOfRoundsToReRoll
                val noOfLockedDice = (1..5).random()

                for(j in 0..noOfRoundsToReRoll)  {
                    for((i, imageView) in imageViewList.withIndex()) {
                        val randomNG = diceNG.rollNG()
                        val isDiceLocked = computerLockedDice.find { it.index == i }

                        if(isDiceLocked == null) {
                            imageView.setImageResource(getImageForRandomNumber(randomNG))
                            computerScore[i] = Dice(i, randomNG)
                        }
                    }
                    for(k in 1..noOfLockedDice) {
                        computerLockedDice.add(Dice((1..5).random(), 0))
                    }
                }
            } else {
                val noOfLockedDice = (1..5).random()

                for(j in 0..computerRoundAmount)  {
                    for((i, imageView) in imageViewList.withIndex()) {
                        val randomNG = diceNG.rollNG()
                        val isDiceLocked = computerLockedDice.find { it.index == i }

                        if(isDiceLocked == null) {
                            imageView.setImageResource(getImageForRandomNumber(randomNG))
                            computerScore[i] = Dice(i, randomNG)
                        }
                    }
                    for(k in 1..noOfLockedDice) {
                        computerLockedDice.add(Dice((1..5).random(), 0))
                    }
                }
            }
        } else {
            for((i, imageView) in imageViewList.withIndex()) {
                val randomNG = diceNG.rollNG()
                imageView.setImageResource(getImageForRandomNumber(randomNG))
                computerScore[i] = Dice(i, randomNG)
            }
        }
    }

    private fun score() {
        val textWins = findViewById<TextView>(R.id.textView2)
        val textScore = findViewById<TextView>(R.id.textView4)

        userTotal = userTotal + userScore.sumOf { it.value }
        computerTotal = computerTotal + computerScore.sumOf { it.value }

        textScore.text = "Score H:" + userTotal + "/C:" + computerTotal

        if(userTotal > winScore || computerTotal > winScore) {
            val popUpWin = AlertDialog.Builder(this)
            if(userTotal > winScore) {
                if(userTotal > computerTotal) {
                    popUpWin.setMessage("Game over. You won")
                    userWinCount = userWinCount + 1
                    hasGameStarted = false
                }
            }
            if(computerTotal > winScore) {
                if(computerTotal > userTotal) {
                    popUpWin.setMessage("Game over. You lost")
                    computerWinCount = computerWinCount + 1
                    hasGameStarted = false
                }
            }
            if(computerTotal == userTotal) {
                popUpWin.setMessage("Game drawn. Next roll with highest score wins")
                isGameDraw = true
            }
            popUpWin.setPositiveButton("OK") { dialog, _ ->
                if(!isGameDraw) {
                    textWins.text = "Wins H:" + userWinCount + "/C:" + computerWinCount
                    userScore = mutableListOf<Dice>(Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0))
                    userLockedDice = mutableListOf<Dice>()
                    computerScore = mutableListOf<Dice>(Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0), Dice(0,0))
                    computerLockedDice = mutableListOf<Dice>()


                    maxNoRounds = 3
                    userRoundAmount = 0
                    computerRoundAmount = 3
                    canComputerPlay = true

                    userTotal = 0
                    computerTotal = 0
                }
                dialog.dismiss()
            }
            val popUp = popUpWin.create()
            popUp.setCancelable(false)
            popUp.setCanceledOnTouchOutside(false)
            popUp.show()
        }

    }

    class DiceNG(private val numSidesNG: Int) {
        fun rollNG(): Int {
            return (1..numSidesNG).random()
        }
    }

    data class Dice(var index: Int, var value: Int)

}

