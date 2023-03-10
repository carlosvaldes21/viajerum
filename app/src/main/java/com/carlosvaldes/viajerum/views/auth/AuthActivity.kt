package com.carlosvaldes.viajerum.views.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupActionBarWithNavController
import com.carlosvaldes.viajerum.MainActivity
import com.carlosvaldes.viajerum.R
import com.carlosvaldes.viajerum.databinding.ActivityAuthBinding
import com.carlosvaldes.viajerum.helpers.AuthHelpers
import com.carlosvaldes.viajerum.views.auth.fragments.AuthFragment
import com.carlosvaldes.viajerum.views.auth.fragments.LoginFragment

class AuthActivity : AppCompatActivity() {

    private lateinit var binding:ActivityAuthBinding
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //Seteamos el nav controller para los fragments
        navController = findNavController(R.id.container)
        //setupActionBarWithNavController(navController, null)
        val tokenExists = AuthHelpers.tokenExist(this@AuthActivity)

    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, null)

    }


}