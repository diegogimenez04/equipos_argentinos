package com.example.equiposargentinos

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.equiposargentinos.databinding.ListItemBinding

class FbAdapter: ListAdapter<Team, FbAdapter.FbViewHolder>(DiffCallBack) {

    companion object DiffCallBack : DiffUtil.ItemCallback<Team>() {
        override fun areItemsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem.strTeam == newItem.strTeam
        }

        override fun areContentsTheSame(oldItem: Team, newItem: Team): Boolean {
            return oldItem == newItem
        }
    }

    lateinit var onItemClickListener: (Team) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FbViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context))
        return FbViewHolder(binding)
    }


    override fun onBindViewHolder(holder: FbViewHolder, position: Int) {
        val team = getItem(position)
        holder.bind(team)
    }

    inner class FbViewHolder(private val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(team: Team) {
            binding.lblName.text = team.strTeam
            binding.lblAbrv.text = team.strTeamShort

            val image = team.strTeamBadge
            Glide.with(binding.root)
                .load(image)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                })
                .error(R.drawable.ic_baseline_image_not_supported_24)
                .into(binding.imgCrest)

            binding.root.setOnClickListener {
                if (::onItemClickListener.isInitialized){
                    onItemClickListener(team)
                } else
                    Log.d("ADAPTER", "onItemClickListener not initialized")
            }

            binding.executePendingBindings()
        }

    }

}