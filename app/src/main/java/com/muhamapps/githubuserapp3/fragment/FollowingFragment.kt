package com.muhamapps.githubuserapp3.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.muhamapps.githubuserapp3.BuildConfig
import com.muhamapps.githubuserapp3.R
import com.muhamapps.githubuserapp3.activity.GitUserDetailActivity
import com.muhamapps.githubuserapp3.adapter.GitUserAdapter
import com.muhamapps.githubuserapp3.entity.GitUser
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class FollowingFragment : Fragment() {

    private val listUserFollowing = ArrayList<GitUser>()
    private val listFollowingDetail = ArrayList<GitUser>()

    companion object {

        private val TAG = FollowingFragment::class.java.simpleName
        private const val ARG_USERNAME = "username"

        fun newInstance(username: String): FollowingFragment {
            val fragment =
                FollowingFragment()
            val bundle = Bundle()
            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(ARG_USERNAME)
        showUserFollowing(username)
    }

    private fun showUserFollowing(idUsername: String?) {

        progressBar.visibility = View.VISIBLE //Memunculkan progess bar

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$idUsername/following"
        client.addHeader("Authorization", "token ${BuildConfig.URL}")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                //Here the Code, Jika Koneksi Berhasil
                progressBar.visibility = View.INVISIBLE

                val result = String(responseBody)
                Log.d(TAG, result)
                try {
                    val jsonArray = JSONArray(result)

                    listUserFollowing.clear()
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
                        listUserFollowing.add(gitUser)
                    }
                    showRecycleList(listUserFollowing)
                }
                catch (e: Exception){
                    Toast.makeText(activity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable
            ) {
                //Here the Code, Jika Koneksi Gagal
                progressBar.visibility = View.INVISIBLE

                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"

                }
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun showRecycleList(users: ArrayList<GitUser>) {

        if (users.isNotEmpty()) {

            rv_user_following.layoutManager = LinearLayoutManager(activity)
            val gitUserAdapter =
                GitUserAdapter(users)
            rv_user_following.adapter = gitUserAdapter

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
        client.addHeader("Authorization", "token ${BuildConfig.URL}")
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
                    listFollowingDetail.clear()
                    listFollowingDetail.add(gitUser)
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
                Toast.makeText(activity, errorMessage, Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun showUserData(data: GitUser){
        val moveDataWithParcelable = Intent(activity, GitUserDetailActivity::class.java)
        moveDataWithParcelable.putExtra(GitUserDetailActivity.EXTRA_USER, data)
        startActivity(moveDataWithParcelable)
    }

}