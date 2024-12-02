package com.easycorp.easystayapp.Presentation.Presentateur.Accueil

import android.content.Context
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.AccueilVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import com.easycorp.easystayapp.Utilitaire.RéservationCourtAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccueilPrésentateur(
    private val context: Context,
    private val listeReservations : ViewPager2,
    private val listViewChambres: ListView,
    private val vue: AccueilVue,
    private val dotsIndicator: DotsIndicator
) : AccueilPrésentateurInterface {
    private val modèle = Modèle.getInstance(context)

    override suspend fun chargerReservationsCourte(clientId: Int, viewPager: ViewPager2) {
        CoroutineScope(Dispatchers.IO).launch {
            val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
            CoroutineScope(Dispatchers.Main).launch {
                val filteredReservations = reservations.filter { it.obtenirNombreDeJours() <= 20 && it.obtenirNombreDeJours() >= 0 }
                val adapter = RéservationCourtAdapter(viewPager.context, filteredReservations)
                viewPager.adapter = adapter
                dotsIndicator.attachTo(viewPager)
            }

        }
    }


    override fun chargerChambres() {
        CoroutineScope(Dispatchers.IO).launch {
            val chambres = modèle.obtenirChambres()
            CoroutineScope(Dispatchers.Main).launch {
                val adapter = chambres?.let {
                    ChambreAdapter(context, it) { chambre ->
                        ouvrirDetailsChambre(chambre)
                    }
                }
                listViewChambres.adapter = adapter
                val animation = AnimationUtils.loadAnimation(context, R.anim.slide_down)
                animation.startOffset= 300
                vue.heroImage.startAnimation(animation)
            }

        }

    }

    override fun ouvrirDetailsChambre(chambre: ChambreData) {
        modèle.setChambreChoisieId(chambre.id)
        modèle.setCheminVersChambreDetails(R.id.action_chambreDetailsFragment_to_fragment_accueil)
        modèle.setCheminVersReservation(R.id.action_reserverFragment_to_chambreDetailsFragment)
        vue.findNavController().navigate(R.id.action_fragment_accueil_to_chambreDetailsFragment)
    }

    override fun chargerChambresFavoris() {
        CoroutineScope(Dispatchers.IO).launch {
            val favoriteRoomIds = modèle.obtenirTousLesFavoris()
            val favoriteChambres = modèle.obtenirChambres()?.filter { it.id in favoriteRoomIds }
            CoroutineScope(Dispatchers.Main).launch {
                if (favoriteChambres != null) {
                    if (favoriteChambres.isEmpty()) {
                        vue.textFavoris.layoutParams.height = 0
                    }
                }
                val adapter = favoriteChambres?.let {
                    ChambreAdapter(context, it) { chambre ->
                        ouvrirDetailsChambre(chambre)
                    }
                }
                listViewChambres.adapter = adapter

                setListViewHeightBasedOnItems(listViewChambres)
            }

        }
    }

    private fun setListViewHeightBasedOnItems(listView: ListView) {
        val listAdapter = listView.adapter ?: return

        val density = listView.context.resources.displayMetrics.density
        val itemHeightInPx = (380 * density).toInt()

        val totalHeight = itemHeightInPx * listAdapter.count

        val params = listView.layoutParams
        params.height = totalHeight + (listView.dividerHeight * (listAdapter.count - 1))
        listView.layoutParams = params
        listView.requestLayout()
    }

}