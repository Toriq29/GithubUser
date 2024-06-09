package com.thoriq.githubuser.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.thoriq.githubuser.data.remote.response.ItemsItem
import com.thoriq.githubuser.databinding.FragmentFollowBinding

class FollowFragment : Fragment() {

    companion object{
        const val ARG_POSITION = "arg_position"
        const val ARG_USERNAME = "arg_username"
    }

    private lateinit var binding: FragmentFollowBinding
    private var position: Int = 1
    private var username: String? = null

    val viewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireContext(), layoutManager.orientation)
        binding.rvFollow.addItemDecoration(itemDecoration)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }
        if (position == 1){
            viewModel.listFollowers.observe(viewLifecycleOwner){
                setFollowData(it)
            }

        } else {
            viewModel.listFollowing.observe(viewLifecycleOwner){
                setFollowData(it)
                println(it)
            }
        }

        viewModel.isLoadingFollow.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setFollowData(githubUsers: List<ItemsItem>) {
        val adapter = FollowAdapter()
        adapter.submitList(githubUsers)
        binding.rvFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}