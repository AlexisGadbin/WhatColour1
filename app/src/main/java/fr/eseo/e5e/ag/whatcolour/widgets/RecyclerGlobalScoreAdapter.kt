package fr.eseo.e5e.ag.whatcolour.widgets

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.storage.FirebaseStorage
import fr.eseo.e5e.ag.whatcolour.R
import fr.eseo.e5e.ag.whatcolour.databinding.GlobalScoreEntryBinding
import fr.eseo.e5e.ag.whatcolour.model.HighScoreEntry

class RecyclerGlobalScoreAdapter(val options : FirebaseRecyclerOptions<HighScoreEntry>, val username : String)
    : FirebaseRecyclerAdapter<HighScoreEntry, RecyclerGlobalScoreAdapter.ViewHolder>(options){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(GlobalScoreEntryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int, model: HighScoreEntry) {
        holder.bind(model,position,username)
    }
    class ViewHolder (val binding : GlobalScoreEntryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(entry : HighScoreEntry, position : Int, username : String){
            binding.txtPosition.text = position.toString()
            binding.txtUser.text = username
            if(username.equals(entry.name)){
                binding.txtUser.setTextColor(Color.BLUE)
            }
            else{
                binding.txtUser.setTextColor(Color.BLACK)
            }
            val bob = entry.photoUrl as String
            if(bob!=null){
                loadImageIntoView(binding.imgUser,bob)
            }else{
                binding.imgUser.setImageResource(R.drawable.blank)
            }
            binding.txtHighScore.text=entry.score.toString()
        }
        private fun loadImageIntoView(view : ImageView, url : String){
            Log.d("PHOTO",url)
            if(url.startsWith("gs:/")){
                val storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(url)
                storageRef.downloadUrl
                    .addOnSuccessListener { uri ->
                        val downloadURL = uri.toString()
                        Glide.with(view.context)
                            .load(downloadURL)
                            .into(view)
                    }
            }
            else{
                Glide.with(view.context)
                    .load(url)
                    .into(view)
            }
        }
    }
}