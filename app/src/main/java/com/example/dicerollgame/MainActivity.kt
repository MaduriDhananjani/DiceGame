package com.example.dicerollgame

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var newGameBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newGameBtn = findViewById(R.id.button3)
        newGameBtn.setOnClickListener {
            val intent =Intent(this,new_game::class.java) // connect
            startActivity(intent)
        }

        val btn1 = findViewById<Button>(R.id.button)
        btn1.setOnClickListener {
            val popUpWin = AlertDialog.Builder(this)
            popUpWin.setMessage("Maduri Dhananjani/W1871614-I confirm that I understand what plagiarism is and have read and\n" +
                    "understood the section on Assessment Offences in the Essential\n" +
                    "Information for Students. The work that I have submitted is\n" +
                    "entirely my own. Any work from other authors is duly referenced\n" +
                    "and acknowledged.\n")
            popUpWin.setPositiveButton("OK"){
                    dialog, _ -> dialog.dismiss()
            }
            val popUp=popUpWin.create()
            popUp.show()

        }
    }
}