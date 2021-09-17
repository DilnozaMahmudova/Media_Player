package com.company.dilnoza.player

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.company.dilnoza.player.databinding.AudioListBinding
import com.company.dilnoza.player.model.ModelAudio


/**
 * Created by Mahmudova Dilnoza on 9/17/2021.
 * QQB
 * icebear03051999@gmail.com
 */


class AudioAdapter(private val context: Context, private val listMedia: ArrayList<ModelAudio>) :
    RecyclerView.Adapter<AudioAdapter.MyViewHolder>() {

    private lateinit var binding: AudioListBinding
    private lateinit var onItemClickListener:OnItemClickListener

    inner class MyViewHolder : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener { v-> onItemClickListener.onItemClick(adapterPosition,v) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        binding = AudioListBinding.inflate(LayoutInflater.from(context), parent, false)
        return MyViewHolder()
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val itemView = listMedia[position]
        binding.artist.text = itemView.audioArtist
        binding.apply {
            Glide.with(context)
                .load(R.drawable.ic_baseline_music_note_24)
                .centerCrop()
                .into(binding.image)

        }
        binding.title.text = itemView.audioTitle

    }

    override fun getItemCount(): Int = listMedia.size

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
            this.onItemClickListener = onItemClickListener
    }

    interface OnItemClickListener {
        fun onItemClick(pos: Int, v: View?)
    }
}

