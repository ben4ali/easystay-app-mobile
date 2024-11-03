package com.easycorp.easystayapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {

    private lateinit var btnHome: Button
    private lateinit var btnProfile: Button
    private lateinit var btnMainHotel: Button
    private lateinit var btnSettings: Button
    private lateinit var btnLogout: Button
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialiser les boutons
        toolbar = findViewById(R.id.toolbar)
        btnHome = findViewById(R.id.buttonHome)
        btnProfile = findViewById(R.id.buttonProfile)
        btnMainHotel = findViewById(R.id.buttonMainHotel)
        btnSettings = findViewById(R.id.buttonSettings)
        btnLogout = findViewById(R.id.buttonLogout)

        // Configurer la barre d'outils
        setSupportActionBar(toolbar)

        // Configuration du NavHostFragment et NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navFragment) as? NavHostFragment
        navHostFragment?.let { navHost ->
            val navController = navHost.navController
            NavigationUI.setupActionBarWithNavController(this, navController)

            // Actions de navigation pour chaque bouton avec vérification du fragment actuel
            btnHome.setOnClickListener {
                if (navController.currentDestination?.id != R.id.fragment_accueil) {
                    navController.navigate(R.id.fragment_accueil)
                }
            }

            btnProfile.setOnClickListener {
                if (navController.currentDestination?.id != R.id.fragment_chambres) {
                    navController.navigate(R.id.fragment_chambres)
                }
            }

            btnMainHotel.setOnClickListener {
                if (navController.currentDestination?.id != R.id.chambreDetailsFragment) {
                    navController.navigate(R.id.chambreDetailsFragment)
                }
            }

            btnSettings.setOnClickListener {
                if (navController.currentDestination?.id != R.id.fragment_preference3) {
                    navController.navigate(R.id.fragment_preference3)
                }
            }

            btnLogout.setOnClickListener {
                if (navController.currentDestination?.id != R.id.reserverFragment) {
                    navController.navigate(R.id.reserverFragment)
                }
            }
        } ?: throw IllegalStateException("NavHostFragment introuvable dans la mise en page")
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.navFragment) as? NavHostFragment)?.navController
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}
