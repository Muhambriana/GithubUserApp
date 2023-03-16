package com.muhamapps.githubuserapp3.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.muhamapps.githubuserapp3.BuildConfig
import com.muhamapps.githubuserapp3.R
import com.muhamapps.githubuserapp3.adapter.GitUserAdapter
import com.muhamapps.githubuserapp3.databinding.ActivityMainBinding
import com.muhamapps.githubuserapp3.entity.GitUser
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private val listGitUser = ArrayList<GitUser>()
    private val currentUser = ArrayList<GitUser>()
    private val tempUser = ArrayList<GitUser>()
    private lateinit var binding: ActivityMainBinding

    companion object {
        private const val STATE_LIST = "STATE_LIST"
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Github Users"

        binding.rvUsers.setHasFixedSize(true)

        if (savedInstanceState == null) {

            binding.progressBar.visibility = View.VISIBLE //Memunculkan progess bar

            //Proses get data dari API
            val client = AsyncHttpClient()
            val url = "https://api.github.com/users"
            client.addHeader("Authorization", "token ${BuildConfig.URL}")
            client.addHeader("User-Agent", "request")
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<Header>,
                    responseBody: ByteArray
                ) {
                    //Here the Code, if connection success
                    binding.progressBar.visibility = View.INVISIBLE


                    val result = String(responseBody)
                    Log.d(TAG, result)
                    try {
                        val jsonArray = JSONArray(result)

                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val username = jsonObject.getString("login")
                            val avatar = jsonObject.getString("avatar_url")

                            val gitUser =
                                GitUser(
                                    username,
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    "",
                                    avatar
                                )
                            listGitUser.add(gitUser)
                            currentUser.add(gitUser)
                        }
                        showRecycleList(listGitUser)
                    }
                    catch (e: Exception){
                        Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable
                ) {
                    //Here the Code, if connection fail
                    binding.progressBar.visibility = View.INVISIBLE

                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error.message}"

                    }
                    Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })

        }
        else {

            val stateList = savedInstanceState.getParcelableArrayList<GitUser>(STATE_LIST)

            if (stateList != null) {
                listGitUser.addAll(stateList)
            }

        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(STATE_LIST, listGitUser)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.sv_widget).actionView as SearchView
        val actionSearchView = menu.findItem(R.id.sv_widget)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(username: String): Boolean {
                listGitUser.clear()
                getDataSearchUser(username)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })

        actionSearchView.setOnActionExpandListener(object: MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(p0: MenuItem): Boolean {
                hideRecycleList()
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem): Boolean {
                showRecycleList(currentUser)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.fu_menu -> {
                val favoriteUserIntent = Intent(this, FavoriteUserActivity::class.java)
                startActivity(favoriteUserIntent)
                return true
            }
            R.id.setting_menu -> {
                val settingsIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingsIntent)
                return true
            }
            else -> return true
        }
    }

    private fun showRecycleList(users: ArrayList<GitUser>) {

        if (users.isNotEmpty()) {

            binding.rvUsers.layoutManager = LinearLayoutManager(this)
            val gitUserAdapter =
                GitUserAdapter(users)
            binding.rvUsers.adapter = gitUserAdapter

            gitUserAdapter.setOnItemClickCallback(object : GitUserAdapter.OnItemClickCallback{
                override fun onItemClicked(data: GitUser) {
                    data.username?.let { data.avatar?.let { it1 -> showUserDetail(it, it1) } }
                }
            })

        }

    }

    private fun showUserDetail(idUsername: String, idAvatar: String){
        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${idUsername}"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                //Here the Code,if connection success

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)

                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val repository = responseObject.getString("public_repos")
                    val company = responseObject.getString("company")
                    val followers = responseObject.getString("followers")
                    val following = responseObject.getString("following")

                    val gitUser =
                        GitUser(
                            idUsername,
                            name,
                            location,
                            repository,
                            company,
                            followers,
                            following,
                            idAvatar
                        )
                    tempUser.add(gitUser)
                    showUserData(gitUser)
                }
                catch (e: Exception){
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                //Here the Code, if connection fail

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun showUserData(data: GitUser){
        val moveDataWithParcelable = Intent(this@MainActivity, GitUserDetailActivity::class.java)
        moveDataWithParcelable.putExtra(GitUserDetailActivity.EXTRA_USER, data)
        startActivity(moveDataWithParcelable)
    }

    private fun hideRecycleList() {
        binding.rvUsers.layoutManager = null
        binding.rvUsers.adapter = null
    }

    private fun getDataSearchUser(idUsername: String) {

        binding.progressBar.visibility = View.VISIBLE //Memunculkan progess bar

        //Proses get data dari API
        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=$idUsername"
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                //Here the Code, if connection success
                binding.progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")

                    listGitUser.clear()
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val username = item.getString("login")
                        val avatar = item.getString("avatar_url")

                        val gitUser =
                            GitUser(
                                username,
                                "",
                                "",
                                "",
                                "",
                                "",
                                "",
                                avatar
                            )
                        listGitUser.add(gitUser)
                    }
                    showRecycleList(listGitUser)
                }
                catch (e: Exception){
                    Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                //Here the Code, if connection fail
                binding.progressBar.visibility = View.INVISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"

                }
                Toast.makeText(this@MainActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

}
