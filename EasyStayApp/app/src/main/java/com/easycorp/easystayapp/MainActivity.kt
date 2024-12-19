package com.easycorp.easystayapp

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonneeAPI
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {

            var estPrete = true

            setKeepOnScreenCondition {
                CoroutineScope(Dispatchers.Main).launch {
                    delay(1000)
                    estPrete = false
                }

                estPrete
            }

            setOnExitAnimationListener {screen ->
                val zoomX = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_X,
                    0.4f,
                    0.0f
                )
                zoomX.interpolator = OvershootInterpolator()
                zoomX.duration = 500L
                zoomX.doOnEnd { screen.remove() }

                val zoomY = ObjectAnimator.ofFloat(
                    screen.iconView,
                    View.SCALE_Y,
                    0.4f,
                    0.0f
                )
                zoomY.interpolator = OvershootInterpolator()
                zoomY.duration = 500L
                zoomY.doOnEnd { screen.remove() }

                zoomX.start()
                zoomY.start()
            }
        }

        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.chambreDetailsFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                R.id.reserverFragment -> {
                    bottomNavigationView.visibility = View.GONE
                }
                else -> {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        }


        println("**********************************************************--------------------TEST API------------------------")

        val urlSource = "http://idefix.dti.crosemont.quebec:9017"
        val bearer = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkFqWUJxeDZ1TjFHemlYTW54d2ZGeCJ9.eyJpc3MiOiJodHRwczovL2Rldi0yNGhxM3ZiNmI4cnYyZjdkLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw2NzU0YjFhMDE5NTI2MmIxY2U2ZmJjM2YiLCJhdWQiOiJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbSIsImlhdCI6MTczMzY4MDc3OCwiZXhwIjoxNzMzNzY3MTc4LCJndHkiOiJwYXNzd29yZCIsImF6cCI6IlBMR2FmcnJkSlN5TExsbnZiaHNnOEpiSXU0Nk5QRkkzIiwicGVybWlzc2lvbnMiOlsiY3JlYXRlOmNoYW1icmVzIiwiY3JlYXRlOmNsaWVudHMiLCJjcmVhdGU6cmVzZXJ2YXRpb25zIiwiZGVsZXRlOmNoYW1icmVzIiwiZGVsZXRlOmNsaWVudHMiLCJkZWxldGU6cmVzZXJ2YXRpb25zIiwicmVhZDpjaGFtYnJlcyIsInJlYWQ6Y2xpZW50cyIsInJlYWQ6cmVzZXJ2YXRpb25zIiwidXBkYXRlOmNoYW1icmVzIiwidXBkYXRlOmNsaWVudHMiLCJ1cGRhdGU6cmVzZXJ2YXRpb25zIl19.NyT42YHvORp_t1W9OwriNHQdmw2d-B87py-G0sw7jv7UfHQHckWZNwYuQ-LkWGNmy40JuqZVz6oAaPTAtea3UEZHrl9laJKelCEtlZjJ0x3JmNBFD4Dp5jdvMLOtQXOgYiDn9m-z97YDwRnT0b5T6_g3n3L3FdbkIIjNbRVCBPUQst2Y_nkcfYipTp2u_lxxlmhv5tjztEtlKLdt88sIveSiyY4-hUQsaSijifOefipr21Vbrph_DUhBA9lWbzEeWC4Rz5pfBR7sh6hFCJnmaE8puQgOMFY2JSn9FpgG4eYdV1sGE6zN_q5v2wQknkOtkmWEihBaqqimvcP8pFlfhA"
        val source = SourceDeDonneeAPI(urlSource, bearer)

        CoroutineScope(Dispatchers.IO).launch {
            val chambres = source.obtenirChambres()
            val reservations = source.obtenirReservations()
            val reservationDeClient = source.obtenirReservationsParClient(ClientData(1, "DupontJ@gmail.com", "Jean", "Dupont", "daad"))
            val reservationParChambre = source.obtenirReservationParChambre(ChambreData(1, "Chambre deluxe", 0.0, "adawd", "adawd", 1, 1, listOf(), listOf(), listOf()))
            val client = source.obtenirClientParId(1)
            withContext(Dispatchers.Main) {
                println("--------------------CHAMBRES------------------------")
                println(chambres)
                println("--------------------RESERVATIONS------------------------")
                println(reservations)
                println("--------------------RESERVATIONS DE CLIENT------------------------")
                println(reservationDeClient)
                println("--------------------RESERVATIONS PAR CHAMBRE ------------------------")
                println(reservationParChambre)
                println("--------------------CLIENT ------------------------")
                println(client)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)?.navController
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}
