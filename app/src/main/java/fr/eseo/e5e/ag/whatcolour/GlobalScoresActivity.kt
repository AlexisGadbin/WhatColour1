package fr.eseo.e5e.ag.whatcolour

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.eseo.e5e.ag.whatcolour.databinding.ActivityGlobalScoreBinding
import fr.eseo.e5e.ag.whatcolour.databinding.ActivityMainBinding
import fr.eseo.e5e.ag.whatcolour.model.HighScoreEntry
import fr.eseo.e5e.ag.whatcolour.widgets.RecyclerGlobalScoreAdapter

class GlobalScoresActivity : AppCompatActivity() {

    lateinit var binding : ActivityGlobalScoreBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGlobalScoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = FirebaseDatabase.getInstance()
        var ref = db.reference
        val firebaseQuery = ref.child(HIGH_SCORE_TABLE).orderByChild("score")
            .limitToLast(100)
        var options = FirebaseRecyclerOptions.Builder<HighScoreEntry>()
            .setQuery(firebaseQuery, HighScoreEntry::class.java)
            .setLifecycleOwner(this)
            .build()
        var llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        llm.reverseLayout = true
        val adapter = RecyclerGlobalScoreAdapter(options, getUsername())
        binding.recyclerHighScores.adapter = adapter
        binding.recyclerHighScores.layoutManager = llm
    }

    private fun getUsername() : String {
        if(FirebaseAuth.getInstance()!=null){
            val user = FirebaseAuth.getInstance().currentUser
            return user?.displayName ?: "????"
        }
        return "????"
    }
}