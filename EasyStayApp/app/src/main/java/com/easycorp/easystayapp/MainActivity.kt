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
        val bearer = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkkzNzJZUHJaMkNTS2pQNVFkNWVZdSJ9.eyJpc3MiOiJodHRwczovL2Rldi1rcHBuemZnczZkNG5heTZqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdkBjbGllbnRzIiwiYXVkIjoiaHR0cDovL2hvdGVsL2FwaSIsImlhdCI6MTczMzI2NTUwMywiZXhwIjoxNzMzMzUxOTAzLCJzY29wZSI6InJlYWQ6Y2hhbWJyZXMgY3JlYXRlOmNoYW1icmVzIHVwZGF0ZTpjaGFtYnJlcyBkZWxldGU6Y2hhbWJyZXMgcmVhZDpyZXNlcnZhdGlvbnMgY3JlYXRlOnJlc2VydmF0aW9ucyB1cGRhdGU6cmVzZXJ2YXRpb25zIGRlbGV0ZTpyZXNlcnZhdGlvbnMgcmVhZDpjYXJhY3RlcmlzdGlxdWUiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMiLCJhenAiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdiIsInBlcm1pc3Npb25zIjpbInJlYWQ6Y2hhbWJyZXMiLCJjcmVhdGU6Y2hhbWJyZXMiLCJ1cGRhdGU6Y2hhbWJyZXMiLCJkZWxldGU6Y2hhbWJyZXMiLCJyZWFkOnJlc2VydmF0aW9ucyIsImNyZWF0ZTpyZXNlcnZhdGlvbnMiLCJ1cGRhdGU6cmVzZXJ2YXRpb25zIiwiZGVsZXRlOnJlc2VydmF0aW9ucyIsInJlYWQ6Y2FyYWN0ZXJpc3RpcXVlIl19.A69jZ-UUaDcEN5Tur-ht3n6SCX7llXpv9OM5hELJIYxAipDAhcs9Wn-fdjG8UxGaAu_uQ-ZI8sr1m9ILVmG-QJwfRW96Lg4eeleaKIn6-RGF6q6GUvUZk772CrNBmyOoD0IfLta1pXPQFE4RDaYh1zDOgyzxHVuxoEYbZEdm6H26bDh7J9QAyUzsrE8K9irDmYGHq42MZLHMuMH5fjQ0yKq8YGmZeJKc4Ob8i4ipG6WK_WVkCmjFQ6qUcVgMUtR7saFtZhL00z6v_bpOFSYvCsax2NY6-Fxo3lnk3gKQTtr4meZLsxdsg4t-F6x5s1Ko1j1NtW1e5BTyUR6hokAaZQ"
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
