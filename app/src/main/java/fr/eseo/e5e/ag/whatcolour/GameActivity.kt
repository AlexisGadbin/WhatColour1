package fr.eseo.e5e.ag.whatcolour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import androidx.recyclerview.widget.LinearLayoutManager
import fr.eseo.e5e.ag.whatcolour.databinding.ActivityGameBinding
import fr.eseo.e5e.ag.whatcolour.model.Colours
import fr.eseo.e5e.ag.whatcolour.model.Result
import fr.eseo.e5e.ag.whatcolour.widgets.HistoryAdapter
import java.text.DecimalFormat
import java.util.Locale
import java.util.Random

const val END_TIME = "END_TIME"
const val SCORE = "SCORE"

class GameActivity : AppCompatActivity() {
    lateinit var binding : ActivityGameBinding
    lateinit var historyAdapter : HistoryAdapter



    val rand = Random()
    var mysteryColourIndex : Int = 0
    var score : Int = 0
    var timeLeft : Long = 0
    var  timeOut : CountDownTimer? = null
    val df = DecimalFormat("0.0")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val llm = LinearLayoutManager(this)
        llm.orientation = LinearLayoutManager.VERTICAL
        llm.stackFromEnd = true
        binding.recyclerHistory.layoutManager = llm
        historyAdapter = HistoryAdapter(this)
        binding.recyclerHistory.adapter = historyAdapter

        score = 0
        timeLeft = 60000
        binding.progressTimeLeft.max = timeLeft.toInt()
        binding.progressTimeLeft.progress = timeLeft.toInt()

        updateInterface()
    }

    override fun onStart() {
        super.onStart()
        startTimer()
    }

    fun startTimer() {
        timeOut = object : CountDownTimer(timeLeft, 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                binding.progressTimeLeft.progress = timeLeft.toInt()
                binding.txtTimeLeft.text = df.format(timeLeft/1000.0)
                if(timeLeft > 10000) {
                    binding.txtTimeLeft.setTextColor(getColor(R.color.blue))
                } else {
                    binding.txtTimeLeft.setTextColor(getColor(R.color.red))
                }
            }

            override fun onFinish() {
                val intent = Intent()
                intent.putExtra(GAME_SCORE, Math.max(score, 0))
                setResult(RESULT_OK, intent)
                finish()
            }
        }.start()
    }

    fun updateInterface() {
        mysteryColourIndex = rand.nextInt(Colours.size)
        val mysteryColourTextIndex = rand.nextInt(Colours.size)
        var falseColourTextIndex = rand.nextInt(Colours.size)

        while(falseColourTextIndex == mysteryColourTextIndex) {
            falseColourTextIndex = rand.nextInt(Colours.size)
        }

        var choice1 = mysteryColourIndex
        var choice2 = falseColourTextIndex
        if(rand.nextBoolean()) {
            choice1 = falseColourTextIndex
            choice2 = mysteryColourIndex
        }
        binding.btnOne.setText(getString(Colours.stringReferences[choice1]))
        binding.btnTwo.setText(getString(Colours.stringReferences[choice2]))
        binding.canvasMysteryColour.setColour(mysteryColourIndex, mysteryColourTextIndex)

        binding.btnOne.setOnClickListener { v -> buttonClicked(1) }
        binding.btnTwo.setOnClickListener { v -> buttonClicked(2) }
    }

    fun buttonClicked(number: Int) {
        val correct = getString(Colours.stringReferences[mysteryColourIndex]).uppercase(Locale.ROOT)
        val actual = if(number==1) binding.btnOne.text.toString().uppercase(Locale.ROOT) else binding.btnTwo.text.toString().uppercase(Locale.ROOT)

        if(actual.equals(correct)) {
            score++
        } else {
            score--
            timeLeft = timeLeft - 1000
            timeOut?.cancel()

            startTimer()
        }
        historyAdapter.results.add(Result(actual, correct))
        historyAdapter.notifyDataSetChanged()
        binding.recyclerHistory.smoothScrollToPosition(historyAdapter.itemCount)
        updateInterface()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(END_TIME, timeLeft+System.currentTimeMillis())
        outState.putInt(SCORE, score)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        val endTime = savedInstanceState.getLong(END_TIME)
        timeLeft = endTime - System.currentTimeMillis()
        timeOut?.cancel()
        score = savedInstanceState.getInt(SCORE)
        startTimer()
    }
}