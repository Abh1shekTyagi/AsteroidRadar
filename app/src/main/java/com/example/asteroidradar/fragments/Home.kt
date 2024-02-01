package com.example.asteroidradar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.asteroidradar.R
import com.example.asteroidradar.adapter.AsteroidClickListener
import com.example.asteroidradar.adapter.AsteroidListAdapter
import com.example.asteroidradar.databinding.FragmentHomeBinding
import com.example.asteroidradar.viewmodels.HomeViewModel

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var adapter: AsteroidListAdapter
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        adapter = AsteroidListAdapter(clickListener = AsteroidClickListener {
            findNavController().navigate(HomeDirections.actionHomeToAsteroidDetails(it))
        })
        val activity = requireNotNull(this.activity)
        viewModel = ViewModelProvider(
            viewModelStore,
            HomeViewModel.Factory(activity.application)
        )[HomeViewModel::class.java]
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()
        initListeners()
        initObserver()
    }

    private fun initObserver() {
        viewModel.picturesByDay.observe(viewLifecycleOwner){
            it?.let {
                adapter.addHeader(it)
            }
        }
        viewModel.asteroidDataList.observe(viewLifecycleOwner) {
            adapter.addList(it)
        }

        viewModel.filteredAsteroids.observe(viewLifecycleOwner){
            adapter.addList(it)
        }
    }

    private fun initListeners() {
        binding.rvAsteroidList.adapter = adapter
    }
    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.filter_asteroid_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.hazardous_asteroids -> {
                        viewModel.getHazardousAsteroids()
                    }
                    R.id.not_hazardous_asteroids -> {
                        viewModel.getNotHazardousAsteroids()
                    }
                    R.id.today_asteroids -> {
                        viewModel.getTodayAsteroids()
                    }
                    R.id.week_asteroids -> {
                        viewModel.getWeekAsteroids()
                    }
                    R.id.all_asteroids -> {
                        viewModel.getAllAsteroids()
                    }
                }
                return true
            }
        }, viewLifecycleOwner)
    }
}