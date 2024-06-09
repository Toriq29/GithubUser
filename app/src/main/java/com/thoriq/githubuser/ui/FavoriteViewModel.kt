package com.thoriq.githubuser.ui

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.thoriq.githubuser.data.GithubUserRepository
import com.thoriq.githubuser.data.database.FavoriteUser

class FavoriteViewModel(application: Application) : ViewModel() {

    private val mGithubRepository: GithubUserRepository = GithubUserRepository(application)

    fun getAllFav(): LiveData<List<FavoriteUser>> {
        return mGithubRepository.getAllFav()
    }
}