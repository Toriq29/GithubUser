package com.thoriq.githubuser.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.thoriq.githubuser.data.remote.response.ItemsItem
import com.thoriq.githubuser.databinding.ItemGithubBinding

class GithubAdapter : ListAdapter<ItemsItem, GithubAdapter.MyViewHolder>(DIFF_CALLBACK) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
        println(user.login)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailUserActivity::class.java)
            intentDetail.putExtra(DetailUserActivity.USERNAME, user.login)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(val binding: ItemGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(github: ItemsItem){
            binding.tvItemName.text = github.login
            Glide.with(binding.root.context)
                .load(github.avatarUrl)
                .into(binding.imgItemPhoto)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ItemsItem>() {
            override fun areItemsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: ItemsItem, newItem: ItemsItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}