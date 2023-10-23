package fr.eseo.e5e.ag.whatcolour

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.eseo.e5e.ag.whatcolour.databinding.ActivityMainBinding
import fr.eseo.e5e.ag.whatcolour.model.HighScoreEntry

const val GAME_SCORE = "GAME_SCORE"
var lastScore = 0
var highScore = 0

const val WHAT_COLOUR = "WHAT_COLOUR"
const val LAST_SCORE = "LAST_SCORE"
const val HIGH_SCORE = "HIGH_SCORE"
const val HIGH_SCORE_TABLE = "high_score_table"

class MainActivity : AppCompatActivity() {
  val gameActivityLauncher =
      registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        Log.d("SCORE", "Returned")
        if (result.resultCode.equals(Activity.RESULT_OK)) {
          Log.d("SCORE", "Score=" + result.data?.getIntExtra(GAME_SCORE, -1))
          lastScore = result.data?.getIntExtra(GAME_SCORE, 0) ?: 0
          binding.txtLocalLastScore.text = lastScore.toString()
          if (lastScore!!.compareTo(highScore!!) > 0) {
            highScore = lastScore
            binding.txtLocalBestScore.text = highScore.toString()
          }
          val db = FirebaseDatabase.getInstance()
          val entry = HighScoreEntry(lastScore, getUsername(), getPhotoUrl())
          db.reference.child(HIGH_SCORE_TABLE).push().setValue(entry)
        }
      }

  private val signInLauncher =
      registerForActivityResult(FirebaseAuthUIActivityResultContract()) { res ->
        this.onSignInResult(res)
      }

  lateinit var binding: ActivityMainBinding
  private lateinit var firebaseAnalytics: FirebaseAnalytics
  private lateinit var auth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    firebaseAnalytics = FirebaseAnalytics.getInstance(this)

    binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    setSupportActionBar(binding.toolbar)

    // switchActivityIntent.putExtra(GAME_SCORE, lastScore)
    auth = FirebaseAuth.getInstance()
    createSignInIntent()
  }

  override fun onStop() {
    super.onStop()

    val prefs = this.getSharedPreferences(WHAT_COLOUR, Context.MODE_PRIVATE)
    val editor = prefs.edit()
    editor.putInt(HIGH_SCORE, highScore)
    editor.putInt(LAST_SCORE, lastScore)
    editor.apply()
  }

  private fun createSignInIntent() {
    val providers =
        arrayListOf(
            // AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())
    val signInIntent =
        AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build()
    signInLauncher.launch(signInIntent)
  }

  private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {
    val response = result.idpResponse
    if (result.resultCode == RESULT_OK) {
      val prefs = getSharedPreferences(WHAT_COLOUR, Context.MODE_PRIVATE)
      lastScore = prefs.getInt(LAST_SCORE, 0)
      highScore = prefs.getInt(HIGH_SCORE, 0)
      binding.txtLocalLastScore.text = lastScore.toString()
      binding.txtLocalBestScore.text = highScore.toString()
      binding.btnPlayGame.setOnClickListener { v ->
        val gameIntent = Intent(this, GameActivity::class.java)
        gameActivityLauncher.launch(gameIntent)
      }
      binding.btnGlobalScores.setOnClickListener { _ ->
        val highIntent = Intent(this, GlobalScoresActivity::class.java)
        startActivity(highIntent)
      }
    } else {
      Toast.makeText(this, "Error signing in", Toast.LENGTH_LONG).show()
      finish()
    }
  }

  private fun getPhotoUrl(): String? {
    val user = auth.currentUser
    if (user != null && user.photoUrl != null) {
      return user.photoUrl.toString()
    }
    return null
  }

  private fun getUsername(): String {
    if (FirebaseAuth.getInstance() != null) {
      val user = FirebaseAuth.getInstance().currentUser
      return user?.displayName ?: "????"
    }
    return "????"
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.main_menu, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.sign_out_menu) {
      auth.signOut()
      finish()
      return true
    }
    return super.onOptionsItemSelected(item)
  }
}
