package com.muhamapps.githubuserapp3.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muhamapps.githubuserapp3.R
import com.muhamapps.githubuserapp3.databinding.ItemRowUserBinding
import com.muhamapps.githubuserapp3.entity.GitUser

class FavoriteUserAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteUserAdapter.NoteViewHolder>() {

    private var onItemClickCallback: FavoriteUserAdapter.OnItemClickCallback? = null

    var listFavoriteUsers = ArrayList<GitUser>()
        set(listFavoriteUser) {
            if (listFavoriteUser.size > 0) {
                this.listFavoriteUsers.clear()
            }
            this.listFavoriteUsers.addAll(listFavoriteUser)

            notifyDataSetChanged()
        }

    fun setOnItemClickCallback(onItemClickCallback: FavoriteUserAdapter.OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun addItem(gitUser: GitUser) {
        this.listFavoriteUsers.add(gitUser)
        notifyItemInserted(this.listFavoriteUsers.size - 1)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return NoteViewHolder(view)
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        holder.bind(listFavoriteUsers[position])
    }

    override fun getItemCount(): Int = this.listFavoriteUsers.size

    inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowUserBinding.bind(itemView)
        fun bind(gitUser: GitUser) {
            with(binding) {
                binding.txtUsername.text = gitUser.username
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