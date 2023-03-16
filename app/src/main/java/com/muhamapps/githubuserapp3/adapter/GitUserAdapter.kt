package com.muhamapps.githubuserapp3.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.muhamapps.githubuserapp3.databinding.ItemRowUserBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muhamapps.githubuserapp3.entity.GitUser

class GitUserAdapter(private val listUser: ArrayList<GitUser>) : RecyclerView.Adapter<GitUserAdapter.ListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val binding = ItemRowUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    override fun getItemCount(): Int = listUser.size

    inner class ListViewHolder(private val binding: ItemRowUserBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(gitUser: GitUser) {
            with(binding){
                binding.txtUsername.text = gitUser.username
                println("kocak  "+gitUser.avatar)
                Glide.with(itemView.context)
                    .load(gitUser.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgAvatar)

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(gitUser) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: GitUser)
    }

}

