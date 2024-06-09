package com.thoriq.githubuser.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.thoriq.githubuser.data.database.FavoriteUser
import com.thoriq.githubuser.data.database.GithubDao
import com.thoriq.githubuser.data.database.GithubRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class GithubUserRepository(application: Application) {

    private val mGithubDao: GithubDao

    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = GithubRoomDatabase.getDatabase(application)
        mGithubDao = db.GithubDao()
    }

    fun insert(favUser: FavoriteUser) {
        executorService.execute { mGithubDao.insert(favUser) }
    }

    fun delete(favUser: FavoriteUser) {
        executorService.execute { mGithubDao.delete(favUser) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser>? {
        return mGithubDao.getFavoriteUserByUsername(username)
    }

    fun getAllFav(): LiveData<List<FavoriteUser>>{
        return mGithubDao.getAllFav()
    }


}