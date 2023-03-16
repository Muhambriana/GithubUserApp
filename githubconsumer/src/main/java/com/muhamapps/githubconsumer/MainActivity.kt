package com.muhamapps.githubconsumer

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.muhamapps.githubconsumer.databinding.ActivityMainBinding
import com.muhamapps.githubconsumer.adapter.FavoriteUserAdapter
import com.muhamapps.githubconsumer.db.FavoriteUserContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.muhamapps.githubconsumer.entity.GitUser
import com.muhamapps.githubconsumer.helper.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

        private lateinit var adapterFavoriteUser: FavoriteUserAdapter
        private lateinit var binding: ActivityMainBinding

        companion object{
            private const val EXTRA_STATE = "EXTRA_STATE"
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            supportActionBar?.title = "Consumer GitHub"

            adapterFavoriteUser = FavoriteUserAdapter(this)

            val handlerThread = HandlerThread("DataObserver")
            handlerThread.start()
            val handler = Handler(handlerThread.looper)

            val myObserver = object : ContentObserver(handler) {
                override fun onChange(self: Boolean) {
                    loadFavoriteUsersAsync()
                }
            }

            contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

            showRecycleList()

            if (savedInstanceState == null) {
                loadFavoriteUsersAsync()
            } else {
                val list = savedInstanceState.getParcelableArrayList<GitUser>(EXTRA_STATE)
                if (list != null) {
                    binding.progressBar.visibility = View.INVISIBLE
                }
            }
        }

        override fun onSaveInstanceState(outState: Bundle) {
            super.onSaveInstanceState(outState)
            outState.putParcelableArrayList(EXTRA_STATE, adapterFavoriteUser.listFavoriteUsers)
        }

        override fun onResume() {
            loadFavoriteUsersAsync()
            super.onResume()
        }

        private fun loadFavoriteUsersAsync() {
            GlobalScope.launch(Dispatchers.Main) {
                binding.progressBar.visibility = View.VISIBLE

                val deferredFavoriteUsers = async(Dispatchers.IO) {
                    val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                    MappingHelper.mapCursorToArrayList(cursor)
                }
                binding.progressBar.visibility = View.INVISIBLE
                val favoriteUsers = deferredFavoriteUsers.await()
                if (favoriteUsers.size > 0) {
                    adapterFavoriteUser.listFavoriteUsers = favoriteUsers
                } else {
                    adapterFavoriteUser.listFavoriteUsers.clear()
                    adapterFavoriteUser.notifyDataSetChanged()
                    showSnackbarMessage("No Favorites User For Now")
                }
            }
        }

        private fun showRecycleList(){
            binding.rvFavoriteUsers.layoutManager = LinearLayoutManager(this)
            binding.rvFavoriteUsers.adapter = adapterFavoriteUser
        }

        private fun showSnackbarMessage(message: String) {
            Snackbar.make(binding.rvFavoriteUsers, message, Snackbar.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.INVISIBLE
        }
}
