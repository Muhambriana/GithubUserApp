package com.muhamapps.githubuserapp3.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.muhamapps.githubuserapp3.R
import com.muhamapps.githubuserapp3.adapter.FavoriteUserAdapter
import com.muhamapps.githubuserapp3.adapter.SectionsPagerAdapter
import com.muhamapps.githubuserapp3.databinding.ActivityGitUserDetailBinding
import com.muhamapps.githubuserapp3.db.FavoriteUserContract
import com.muhamapps.githubuserapp3.db.FavoriteUserContract.FavoriteUserColumns.Companion.CONTENT_URI
import com.muhamapps.githubuserapp3.db.FavoriteUserHelper
import com.muhamapps.githubuserapp3.entity.GitUser
import com.muhamapps.githubuserapp3.helper.MappingHelper


class GitUserDetailActivity : AppCompatActivity() {

    private lateinit var adapterFavoriteUser: FavoriteUserAdapter
    private val binding: ActivityGitUserDetailBinding by lazy {
        ActivityGitUserDetailBinding.inflate(layoutInflater)
    }

    companion object {
        var EXTRA_USER = "extra_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        supportActionBar?.title = "Detail User"

        val listUser = intent.getParcelableExtra<GitUser>(EXTRA_USER)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(this)
        sectionsPagerAdapter.username = listUser?.username
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            if (position == 0) {
                tab.text = getString(R.string.followers_user, listUser?.followers)
            } else {
                tab.text = getString(R.string.following_user, listUser?.following)
            }
        }.attach()
        supportActionBar?.elevation = 0f

        //untuk memasukan data yang sudah didapat kedalam TextView
        binding.textViewUsername.text = getString(R.string.et, listUser?.username)
        binding.textViewNameUser.text = listUser?.name
        binding.textViewLocationUser.text = listUser?.location
        binding.textViewRepository.text = getString(R.string.repository_user, listUser?.repository)
        binding.textViewCompanyUser.text = listUser?.company
        Glide.with(this).asBitmap().load(listUser?.avatar).into(binding.imageViewUser)

        adapterFavoriteUser = FavoriteUserAdapter(this)

        val gitUser = GitUser(
            listUser?.username,
            listUser?.name,
            listUser?.location,
            listUser?.repository,
            listUser?.company,
            listUser?.avatar
        )

       val uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + listUser?.username)

        var statusFavorite = listUser?.username?.let { getState(it) }
        statusFavorite?.let { setStatusFavorite(it) }
        binding.fabFavoriteUser.setOnClickListener {
            if (statusFavorite == 1) {
                statusFavorite = 0
                    contentResolver.delete(uriWithId, null, null)
                        showSnackbarMessage("Data Berhasil Dihapus")
            } else {
                statusFavorite = 1
                val values = ContentValues()
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_USERNAME,
                    listUser?.username
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_NAME,
                    listUser?.name
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_LOCATION,
                    listUser?.location
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_REPOSITORY,
                    listUser?.repository
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_COMPANY,
                    listUser?.company
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_FOLLOWERS,
                    listUser?.followers
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_FOLLOWING,
                    listUser?.following
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_AVATAR,
                    listUser?.avatar
                )
                values.put(
                    FavoriteUserContract.FavoriteUserColumns.COLUMN_NAME_STATUS,
                    listUser?.status
                )


                contentResolver.insert(CONTENT_URI, values)
                showSnackbarMessage("Data Berhasil Ditambah")

                adapterFavoriteUser.addItem(gitUser)
            }
            setStatusFavorite(statusFavorite!!)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setStatusFavorite(statusFavorite: Int) {
        if(statusFavorite == 1){
            binding.fabFavoriteUser.setImageDrawable(getDrawable(R.drawable.favorite_icon_fill_foreground))
        }
        else{
            binding.fabFavoriteUser.setImageDrawable(getDrawable(R.drawable.favorite_icon_blank_foreground))
        }
    }

    private fun getState(username: String): Int {
        var bool: Int = 0

        val favoriteUserHelper = FavoriteUserHelper.getInstance(applicationContext)
        favoriteUserHelper.open()
        val cursor = favoriteUserHelper.queryById(username)
        val gitUser = MappingHelper.mapCursorToObject(cursor)
        if(gitUser != null)
            bool = 1
        else
            bool = 0
        favoriteUserHelper.close()

        return bool
    }

    private fun showSnackbarMessage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }


}
