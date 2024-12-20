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

        val urlSource = "http://idefix.dti.crosemont.quebec:9022"
        val bearer = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkFqWUJxeDZ1TjFHemlYTW54d2ZGeCJ9.eyJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbS9lbWFpbCI6InJvYmVydG1heGltZUBob3RlbC5xYy5jYSIsImh0dHA6Ly9pc21haWxlbGFzcmFvdWkuY29tL3JvbGVzIjpbIkFkbWluIl0sImlzcyI6Imh0dHBzOi8vZGV2LTI0aHEzdmI2YjhydjJmN2QudXMuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDY3NTRiMWEwMTk1MjYyYjFjZTZmYmMzZiIsImF1ZCI6Imh0dHA6Ly9pc21haWxlbGFzcmFvdWkuY29tIiwiaWF0IjoxNzM0NjcxNDUwLCJleHAiOjE3MzQ3NTc4NTAsImd0eSI6InBhc3N3b3JkIiwiYXpwIjoiUExHYWZycmRKU3lMTGxudmJoc2c4SmJJdTQ2TlBGSTMiLCJwZXJtaXNzaW9ucyI6WyJjcmVhdGU6Y2hhbWJyZXMiLCJjcmVhdGU6Y2xpZW50cyIsImNyZWF0ZTpyZXNlcnZhdGlvbnMiLCJkZWxldGU6Y2hhbWJyZXMiLCJkZWxldGU6Y2xpZW50cyIsImRlbGV0ZTpyZXNlcnZhdGlvbnMiLCJyZWFkOmNoYW1icmVzIiwicmVhZDpjbGllbnRzIiwicmVhZDpyZXNlcnZhdGlvbnMiLCJ1cGRhdGU6Y2hhbWJyZXMiLCJ1cGRhdGU6Y2xpZW50cyIsInVwZGF0ZTpyZXNlcnZhdGlvbnMiXX0.DORF7BJtTIu0W2ofeNvPDORwZwtYEjfJRB-OdTGX3praMf1ka6AHSbNAA78nFELqJ7Cd-cbG_-LGHlyQPMqqd_fIDgbKZFcLWiUbjN1Mf5EBOdIqUX_GoobrNsc23cZm_JyDOWE6O4ylt07PrqrnxSBmZFyikRr-hYjMuOqgJwMlyCdduPOEzwprGzrOIRsbSo3trAEkxNORYsfbCm04GsSVH4zfjALbpFgx5zhWHs3jro1pNV4f7Pc1yQE6ni56T2vIrk8gpKvz9_ZdvXlWPBBH1CHwbI_HicytLbmWDq1w4xNAeGohqsloTtqXJsCALy43PwofwT9ujvaFmhSF7g"
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