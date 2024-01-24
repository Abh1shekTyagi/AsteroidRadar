package com.example.asteroidradar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.asteroidradar.databinding.FragmentAsteroidDetailsBinding

class AsteroidDetails : Fragment() {

    private lateinit var binding: FragmentAsteroidDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentAsteroidDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }
}