package com.thoriq.githubuser.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thoriq.githubuser.R
import com.thoriq.githubuser.data.helper.SettingPreferences
import com.thoriq.githubuser.data.helper.ViewModelDarkModeVactory
import com.thoriq.githubuser.data.helper.ViewModelFactory
import com.thoriq.githubuser.data.helper.dataStore
import com.thoriq.githubuser.data.remote.response.ItemsItem
import com.thoriq.githubuser.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelDarkModeVactory(pref)).get(
            MainViewModel::class.java
        )

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        setSupportActionBar(binding.searchBar)


        val layoutManager = LinearLayoutManager(this)
        binding.rvGithub.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvGithub.addItemDecoration(itemDecoration)

        mainViewModel.listUsers.observe(this) {
            setGithubData(it)
        }

        mainViewModel.isLoading.observe(this){
            showLoading(it)
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { textView, actionId, event ->
                    searchBar.setText(searchView.text)
                    searchView.hide()
//                    Toast.makeText(this@MainActivity, searchView.text, Toast.LENGTH_SHORT).show()
                    mainViewModel.fundUsers(searchView.text.toString())
                    false
                }
        }
    }

    private fun setGithubData(githubUsers: List<ItemsItem>) {
        val adapter = GithubAdapter()
        adapter.submitList(githubUsers)
        binding.rvGithub.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)

        val favoriteMenuItem = menu?.findItem(R.id.menu1)

        val themeMode = AppCompatDelegate.getDefaultNightMode()
        if (themeMode == AppCompatDelegate.MODE_NIGHT_YES) {
            favoriteMenuItem?.setIcon(R.drawable.baseline_favorite_light_24)
        } else {
            favoriteMenuItem?.setIcon(R.drawable.ic_favorite_24)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu1 -> {
                val favoriteIntent = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(favoriteIntent)
                return true
            }
            R.id.menu2 -> {
                val settingIntent = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(settingIntent)
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}