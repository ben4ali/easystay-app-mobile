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


        println("--------------------TEST API------------------------")

        val urlSource = "http://idefix.dti.crosemont.quebec:9002"
        val bearer = "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkkzNzJZUHJaMkNTS2pQNVFkNWVZdSJ9.eyJpc3MiOiJodHRwczovL2Rldi1rcHBuemZnczZkNG5heTZqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdkBjbGllbnRzIiwiYXVkIjoiaHR0cDovL2hvdGVsL2FwaSIsImlhdCI6MTczMzA3NzMzMiwiZXhwIjoxNzMzMTYzNzMyLCJzY29wZSI6InJlYWQ6Y2hhbWJyZXMgY3JlYXRlOmNoYW1icmVzIHVwZGF0ZTpjaGFtYnJlcyBkZWxldGU6Y2hhbWJyZXMgcmVhZDpyZXNlcnZhdGlvbnMgY3JlYXRlOnJlc2VydmF0aW9ucyB1cGRhdGU6cmVzZXJ2YXRpb25zIGRlbGV0ZTpyZXNlcnZhdGlvbnMgcmVhZDpjYXJhY3RlcmlzdGlxdWUiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMiLCJhenAiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdiIsInBlcm1pc3Npb25zIjpbInJlYWQ6Y2hhbWJyZXMiLCJjcmVhdGU6Y2hhbWJyZXMiLCJ1cGRhdGU6Y2hhbWJyZXMiLCJkZWxldGU6Y2hhbWJyZXMiLCJyZWFkOnJlc2VydmF0aW9ucyIsImNyZWF0ZTpyZXNlcnZhdGlvbnMiLCJ1cGRhdGU6cmVzZXJ2YXRpb25zIiwiZGVsZXRlOnJlc2VydmF0aW9ucyIsInJlYWQ6Y2FyYWN0ZXJpc3RpcXVlIl19.UW6eWyCF082Xx_pQmoTScpYbYVE4qDaex_W_D5jB1No25O7cKnlshZ-0Q7HlMrLyR-c5Jn1sCWfkyc2B8eccP7tQdsGN4kDdrHuEVLR_QAxDpQPKHJfeF6RM2ic3o2g9Yf-BNzeDUGyRzcfB_IPl90hl2SvJBfFp_av9c51cpdFlzU9ES5SU-VIqgZQMZuKSV-qsVXUYu8R99nEMVzecF5xJSebmkmgeZM5TPQofIojjz_JdtHWr69H3xGWjRao0LGKbTFs7X4JJZOvtz9Lunyto2NxyOpS9b85tr-_4Nqt-qd66D7wrAJNi8chpx7vCbpBi7BntvoIUzfmQivu54A"
        val source = SourceDeDonneeAPI(urlSource, bearer)

        CoroutineScope(Dispatchers.IO).launch {
            val chambres = source.obtenirChambres()
            val reservations = source.obtenirReservations()
            withContext(Dispatchers.Main) {
                println(chambres)
                println(reservations)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment)?.navController
        return navController?.navigateUp() ?: super.onSupportNavigateUp()
    }
}
