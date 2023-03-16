package com.muhamapps.githubconsumer.adapter


import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.muhamapps.githubconsumer.R
import com.muhamapps.githubconsumer.databinding.ItemRowUserBinding
import com.muhamapps.githubconsumer.entity.GitUser

class FavoriteUserAdapter(private val activity: Activity) : RecyclerView.Adapter<FavoriteUserAdapter.NoteViewHolder>() {

    var listFavoriteUsers = ArrayList<GitUser>()
        set(listFavoriteUser) {
            if (listFavoriteUser.size > 0) {
                this.listFavoriteUsers.clear()
            }
            this.listFavoriteUsers.addAll(listFavoriteUser)

            notifyDataSetChanged()
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
            println("buku ${gitUser.avatar}")
            with(binding) {
                binding.txtUsername.text = gitUser.username
                Glide.with(itemView.context)
                    .load(gitUser.avatar)
                    .apply(RequestOptions().override(55, 55))
                    .into(imgAvatar)
            }
        }
    }

}