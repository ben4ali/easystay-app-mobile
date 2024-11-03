package com.easycorp.easystayapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    private lateinit var btnHome: Button
    private lateinit var btnUser: Button
    private lateinit var btnHotel: Button
    private lateinit var btnSetting: Button
    private lateinit var btnLogout: Button
    private lateinit var toolbar : androidx.appcompat.widget.Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnHome = findViewById(R.id.buttonHome)
        btnUser = findViewById(R.id.buttonProfile)
        btnHotel = findViewById(R.id.buttonMainHotel)
        btnSetting = findViewById(R.id.buttonSettings)
        btnLogout = findViewById(R.id.buttonLogout)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as? NavHostFragment
        if (navHostFragment != null) {
            val navController = navHostFragment.navController

            NavigationUI.setupActionBarWithNavController(this, navController)

            btnHome.setOnClickListener {
                navController.navigate(R.id.action_fragment_chambres_to_fragment_accueil)
            }

            btnUser.setOnClickListener {
                navController.navigate(R.id.action_fragment_accueil_to_chambreDetailsFragment)
            }

            btnHotel.setOnClickListener {
                navController.navigate(R.id.action_fragment_accueil_to_fragment_chambres)
            }

            btnSetting.setOnClickListener {
                navController.navigate(R.id.action_fragment_accueil_to_fragment_preference3)
            }

            btnLogout.setOnClickListener {
                navController.navigate(R.id.action_fragment_accueil_to_fragment_preference3)
            }
        } else {
            throw IllegalStateException("NavHostFragment not found in layout")
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.fragment_nav) as? NavHostFragment)?.navController
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}
