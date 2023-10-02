package fr.eseo.e5e.ag.whatcolour.widgets

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.eseo.e5e.ag.whatcolour.R
import fr.eseo.e5e.ag.whatcolour.databinding.CardHistoryLayoutBinding
import fr.eseo.e5e.ag.whatcolour.model.Result

class HistoryAdapter (private val context : Context) : RecyclerView.Adapter<HistoryAdapter.Holder>() {
    var results : ArrayList<Result> = ArrayList()

    class Holder (val binding : CardHistoryLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = CardHistoryLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return this.results.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val item = this.results.get(position)
        holder.binding.imgResult.setImageDrawable(context.getDrawable(if(item.isCorrect) R.drawable.yes else R.drawable.no))
        holder.binding.txtCorrectChoice.text = String.format(context.getString(R.string.correct_colour_name), item.actualColour)
        holder.binding.txtUsersChoice.text = String.format(context.getString(R.string.users_choice), item.guessedColour)
    }
}