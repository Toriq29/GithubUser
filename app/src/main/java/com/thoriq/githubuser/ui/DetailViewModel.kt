package com.thoriq.githubuser.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.thoriq.githubuser.data.GithubUserRepository
import com.thoriq.githubuser.data.database.FavoriteUser
import com.thoriq.githubuser.data.remote.response.DetailUserResponse
import com.thoriq.githubuser.data.remote.response.ItemsItem
import com.thoriq.githubuser.data.remote.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    companion object{
        private const val TAG = "DetailViewModel"
    }

    private val _username = MutableLiveData<String>()
    val username: LiveData<String> = _username

    private val _name = MutableLiveData<String>()
    val name: LiveData<String> = _name

    private val _img = MutableLiveData<String>()
    val img: LiveData<String> = _img

    private val _followers = MutableLiveData<Int>()
    val followers: LiveData<Int> = _followers

    private val _following = MutableLiveData<Int>()
    val following: LiveData<Int> = _following

    private val _isLoadingDetail = MutableLiveData<Boolean>()
    val isLoadingDetail: LiveData<Boolean> = _isLoadingDetail

    private val _isLoadingFollow = MutableLiveData<Boolean>()
    val isLoadingFollow: LiveData<Boolean> = _isLoadingFollow

    private val _listFollowers = MutableLiveData<List<ItemsItem>>()
    val listFollowers: LiveData<List<ItemsItem>> = _listFollowers

    private val _listFollowing = MutableLiveData<List<ItemsItem>>()
    val listFollowing: LiveData<List<ItemsItem>> = _listFollowing


    private val mGithubRepository: GithubUserRepository = GithubUserRepository(application)

    fun insert() {
        val usernameValue = username.value
        val avatarUrlValue = img.value

        println(usernameValue)

        if (usernameValue != null && avatarUrlValue != null) {

            val favUser = FavoriteUser()
            favUser.let {
                favUser.username = usernameValue
                favUser.avatarUrl = avatarUrlValue
            }
            mGithubRepository.insert(favUser)
        }

    }

    fun delete() {
        val usernameValue = username.value
        val avatarUrlValue = img.value

        println(usernameValue)

        if (usernameValue != null && avatarUrlValue != null) {
            val favUser = FavoriteUser()
            favUser.let {
                favUser.username = usernameValue
                favUser.avatarUrl = avatarUrlValue
            }
            mGithubRepository.delete(favUser)
        }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> {
        return mGithubRepository.getFavoriteUserByUsername(username)!!
    }

    fun insertOrDelete(check: Boolean){
        if (check == false) {
            insert()
        } else {
            delete()
        }
    }




    fun findDetailUser(username: String){
        _isLoadingDetail.value = true
        _isLoadingFollow.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _isLoadingDetail.value = false
                if (response.isSuccessful) {
                    _name.value = response.body()?.name
                    _username.value = response.body()?.login
                    _img.value = response.body()?.avatarUrl
                    _followers.value = response.body()?.followers
                    _following.value = response.body()?.following
                } else {
                    Log.e(DetailViewModel.TAG, "onFailure getDetailUser: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoadingDetail.value = false
                Log.e(DetailViewModel.TAG, "onFailure getDetailUser: ${t.message.toString()}")
            }
        })

        val client1 = ApiConfig.getApiService().getFollowers(username)
        client1.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                if (response.isSuccessful) {
                    _listFollowers.value = response.body()
                    println(response.body()?.size)
                } else {
                    Log.e(DetailViewModel.TAG, "onFailure getFollowers: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(DetailViewModel.TAG, "onFailure getFollowers: ${t.message.toString()}")
            }
        })

        val client2 = ApiConfig.getApiService().getFollowing(username)
        client2.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(call: Call<List<ItemsItem>>, response: Response<List<ItemsItem>>) {
                _isLoadingFollow.value = false
                if (response.isSuccessful) {
                    _listFollowing.value = response.body()
                    println(response.body()?.size)
                } else {
                    Log.e(DetailViewModel.TAG, "onFailure getFollowing: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                _isLoadingFollow.value = false
                Log.e(DetailViewModel.TAG, "onFailure getFollowing: ${t.message.toString()}")
            }
        })
    }
}