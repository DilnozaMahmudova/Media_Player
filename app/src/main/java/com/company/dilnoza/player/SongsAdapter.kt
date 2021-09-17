package com.company.dilnoza.player

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.company.dilnoza.player.databinding.PicturePageBinding
import com.company.dilnoza.player.extentions.bindItem

class SongsAdapter(
    private val ls: Array<String?>
) :
    RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    private lateinit var binding: PicturePageBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :ViewHolder{
        binding= PicturePageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder()
    }

    override fun getItemCount() = ls.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind()

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {


        fun bind() = bindItem {
            binding.name.text=ls[adapterPosition]
        }
    }
}
