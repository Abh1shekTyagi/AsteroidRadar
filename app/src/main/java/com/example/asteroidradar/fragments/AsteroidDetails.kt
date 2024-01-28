package com.example.asteroidradar.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.asteroidradar.R
import com.example.asteroidradar.databinding.FragmentAsteroidDetailsBinding

class AsteroidDetails : Fragment() {

    private lateinit var binding: FragmentAsteroidDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAsteroidDetailsBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.asteroid = AsteroidDetailsArgs.fromBundle(requireArguments()).asteroid
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
    }

    private fun initListener() {
        binding.helpButton.setOnClickListener {
            showAlertDialog()
        }
    }
    private fun showAlertDialog() {
        val alertDialogBuilder = context?.let { AlertDialog.Builder(it) }

        // Set the title and message for the dialog
        alertDialogBuilder?.setMessage(R.string.astronomica_unit_explanation)

        // Set positive button and its click listener
        alertDialogBuilder?.setPositiveButton("Understood") { dialog, which ->
            // Do something when the OK button is clicked
            // You can leave this empty if you just want to close the dialog
        }
        // Create and show the alert dialog
        val alertDialog: AlertDialog? = alertDialogBuilder?.create()
        alertDialog?.show()
    }
}