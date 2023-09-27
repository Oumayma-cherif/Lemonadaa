package com.example.lemonadaa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {


    private val LEMONADE_STATE = "LEMONADE_STATE"
    private val LEMON_SIZE = "LEMON_SIZE"
    private val SQUEEZE_COUNT = "SQUEEZE_COUNT"

    // SELECT représente l'état "cueillir du citron"
    private val SELECT = "select"

    // SQUEEZE représente l'état "presser le citron"
    private val SQUEEZE = "squeeze"

    // DRINK représente l'état "boire de la limonade"
    private val DRINK = "drink"

    // RESTART représente l'état où la limonade a été bue et le verre est vide
    private val RESTART = "restart"

    // Par défaut l'état est à select
    private var lemonadeState = "select"

    // Taille de citron par défaut est à -1
    private var lemonSize = -1

    // Par défaut le squeezeCount est à -1
    private var squeezeCount = -1

    private var lemonTree = LemonTree()
    private var lemonImage: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // === NE PAS MODIFIER LE CODE DANS LA DÉCLARATION IF SUIVANTE ===
        if (savedInstanceState != null) {
            lemonadeState = savedInstanceState.getString(LEMONADE_STATE, "select")
            lemonSize = savedInstanceState.getInt(LEMON_SIZE, -1)
            squeezeCount = savedInstanceState.getInt(SQUEEZE_COUNT, -1)
        }
        // === FIN IF ===

        lemonImage = findViewById(R.id.image_lemon_state)
        setViewElements()

        lemonImage!!.setOnClickListener {
            clickLemonImage()
        }

        lemonImage!!.setOnLongClickListener {
            squeezeCount += 1
            lemonSize -= 1
            showSnackbar()
        }
    }


    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(LEMONADE_STATE, lemonadeState)
        outState.putInt(LEMON_SIZE, lemonSize)
        outState.putInt(SQUEEZE_COUNT, squeezeCount)
        super.onSaveInstanceState(outState)
    }


    private fun clickLemonImage() {
        when (lemonadeState) {
            SELECT -> {
                lemonadeState = SQUEEZE
                lemonSize = lemonTree.pick()
                squeezeCount = 0
            }
            SQUEEZE -> {
                if (lemonSize == 0) {
                    lemonadeState = DRINK
                    lemonSize = -1
                }
            }
            DRINK -> lemonadeState = RESTART
            RESTART -> lemonadeState = SELECT
        }
        setViewElements()
    }


    private fun setViewElements() {
        val textAction: TextView = findViewById(R.id.text_action)
        when (lemonadeState) {
            SELECT -> {
                textAction.setText(R.string.lemon_select)
                lemonImage!!.setImageResource(R.drawable.lemon_tree)
            }

            SQUEEZE -> {
                textAction.setText(R.string.lemon_squeeze)
                lemonImage!!.setImageResource(R.drawable.lemon_squeeze)
            }

            DRINK -> {
                textAction.setText(R.string.lemon_drink)
                lemonImage!!.setImageResource(R.drawable.lemon_drink)
            }

            RESTART -> {
                textAction.setText(R.string.lemon_empty_glass)
                lemonImage!!.setImageResource(R.drawable.lemon_restart)
            }
        }

    }


    private fun showSnackbar(): Boolean {
        if (lemonadeState != SQUEEZE || lemonSize == 0) {
            return false
        }
        val squeezeText = getString(R.string.squeeze_count, lemonSize)
        Snackbar.make(
            findViewById(R.id.constraint_Layout),
            squeezeText,
            Snackbar.LENGTH_SHORT
        ).show()
        return true
    }


}

class LemonTree {
    fun pick(): Int {
        return (2..4).random()
    }
}
