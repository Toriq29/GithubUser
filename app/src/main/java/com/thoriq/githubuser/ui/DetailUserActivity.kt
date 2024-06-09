package com.thoriq.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.thoriq.githubuser.R
import com.thoriq.githubuser.data.helper.ViewModelFactory
import com.thoriq.githubuser.databinding.ActivityDetailUserBinding

class DetailUserActivity : AppCompatActivity() {

    companion object{
        const val USERNAME = "username"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.followers,
            R.string.following
        )
    }
//    private val DetailViewModel by viewModels<DetailViewModel>()

    private lateinit var binding: ActivityDetailUserBinding
    private var chekingUser = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailViewModel = obtainViewModel(this@DetailUserActivity)

        intent.getStringExtra(USERNAME)?.let { detailViewModel.findDetailUser(it) }

        intent.getStringExtra(USERNAME)?.let { username ->
            detailViewModel.getFavoriteUserByUsername(username).observe(this) { favoriteUser ->
                if (favoriteUser == null) {
                    chekingUser = false
                    binding.fabAdd.setImageResource(R.drawable.ic_favorite_border_24)
                } else {
                    chekingUser = true
                    binding.fabAdd.setImageResource(R.drawable.ic_favorite_24)
                }
            }
        }

        binding.fabAdd.setOnClickListener{
            detailViewModel.insertOrDelete(chekingUser)
        }


        detailViewModel.name.observe(this) {
            binding.name.text = it
        }

        detailViewModel.username.observe(this) {
            binding.textView.text = it
        }

        detailViewModel.followers.observe(this) {
            binding.followers.text = "${it.toString()} Followers"
        }

        detailViewModel.following.observe(this) {
            binding.following.text = "${it.toString()} Following"
        }

        detailViewModel.img.observe(this) {
            Glide.with(this)
                .load(it)
                .into(binding.imgItemPhoto)
        }

        detailViewModel.isLoadingDetail.observe(this){
            showLoading(it)
        }

        val sectionPageAdapter = SectionsPagerAdapter(this)
        sectionPageAdapter.username = "Mashokkkk"

        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionPageAdapter

        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager) {tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    private fun obtainViewModel(activity: AppCompatActivity): DetailViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DetailViewModel::class.java)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}
