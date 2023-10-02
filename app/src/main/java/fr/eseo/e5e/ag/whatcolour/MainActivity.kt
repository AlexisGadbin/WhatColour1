package fr.eseo.e5e.ag.whatcolour

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import fr.eseo.e5e.ag.whatcolour.databinding.ActivityMainBinding

const val GAME_SCORE = "GAME_SCORE"
var lastScore = 0
var highScore = 0

const val WHAT_COLOUR = "WHAT_COLOUR"
const val LAST_SCORE = "LAST_SCORE"
const val HIGH_SCORE = "HIGH_SCORE"
class MainActivity : AppCompatActivity() {
    val gameActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result -> Log.d("SCORE", "Returned")
        if(result.resultCode.equals(Activity.RESULT_OK)) {
            Log.d("SCORE", "Score="+result.data?.getIntExtra(GAME_SCORE, -1))
            lastScore = result.data?.getIntExtra(GAME_SCORE, 0) ?: 0
            binding.txtLocalLastScore.text = lastScore.toString()
            if(lastScore!!.compareTo(highScore!!) > 0) {
                highScore = lastScore
                binding.txtLocalBestScore.text = highScore.toString()
            }
        }
    }
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        val prefs = this.getSharedPreferences(WHAT_COLOUR, Context.MODE_PRIVATE)
        lastScore = prefs.getInt(LAST_SCORE, 0)
        highScore = prefs.getInt(HIGH_SCORE, 0)
        binding.txtLocalLastScore.text = lastScore.toString()
        binding.txtLocalBestScore.text = highScore.toString()

        binding.btnPlayGame.setOnClickListener {v ->
            val switchActivityIntent = Intent(this, GameActivity::class.java)
            switchActivityIntent.putExtra(GAME_SCORE, lastScore)
            gameActivityLauncher.launch(switchActivityIntent)
        }

    }

    override fun onStop() {
        super.onStop()

        val prefs = this.getSharedPreferences(WHAT_COLOUR, Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putInt(HIGH_SCORE, highScore)
        editor.putInt(LAST_SCORE, lastScore)
        editor.apply()
    }
}