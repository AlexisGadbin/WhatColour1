package fr.eseo.e5e.ag.whatcolour

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import fr.eseo.e5e.ag.whatcolour.databinding.ActivityMainBinding

const val GAME_SCORE = "GAME_SCORE"
var lastScore = 0
var highScore = 0
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

        binding.btnPlayGame.setOnClickListener {v ->
            val switchActivityIntent = Intent(this, GameActivity::class.java)
            gameActivityLauncher.launch(switchActivityIntent)
        }

    }
}