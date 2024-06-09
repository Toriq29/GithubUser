package com.thoriq.githubuser.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thoriq.githubuser.data.database.FavoriteUser
import com.thoriq.githubuser.databinding.ItemGithubBinding

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>() {

    private val listFavUser = ArrayList<FavoriteUser>()

    fun setListFavUser(listNotes: List<FavoriteUser>) {
        this.listFavUser.clear()
        this.listFavUser.addAll(listNotes)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(listFavUser[position])
        println(listFavUser[position].username)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetail.putExtra(DetailUserActivity.USERNAME, listFavUser[position].username)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int {
        return listFavUser.size
    }

    class MyViewHolder(val binding: ItemGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(github: FavoriteUser){
            binding.tvItemName.text = github.username
            Glide.with(binding.root.context)
                .load(github.avatarUrl)
                .into(binding.imgItemPhoto)
        }
    }
}