package fr.eseo.e5e.ag.whatcolour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.eseo.e5e.ag.whatcolour.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    lateinit var binding : ActivityGameBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}