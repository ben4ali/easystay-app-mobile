package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.AccueilVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAO
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import com.easycorp.easystayapp.Utilitaire.RéservationCourtAdapter

class AccueilPrésentateur(
    private val context: Context,
    private val listViewReservations: ListView,
    private val listViewChambres: ListView,
    private val vue: AccueilVue,
) : AccueilPrésentateurInterface {
    private val favorisDAO = FavorisDAO(context)
    private val modèle = Modèle.getInstance()

    override fun chargerReservationsCourte(clientId: Int) {
        val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
        val filteredReservations = reservations.filter { it.obtenirNombreDeJours() <= 20 }
        val adapter = RéservationCourtAdapter(context, filteredReservations)
        listViewReservations.adapter = adapter
    }

    override fun chargerChambres() {
        val chambres = modèle.obtenirChambres()
        val adapter = ChambreAdapter(context, chambres) { chambre ->
            ouvrirDetailsChambre(chambre)
        }
        listViewChambres.adapter = adapter
    }

    override fun ouvrirDetailsChambre(chambre: ChambreData) {
        modèle.setChambreChoisieId(chambre.id)
        vue.findNavController().navigate(R.id.action_fragment_accueil_to_chambreDetailsFragment)
    }

    override fun chargerChambresFavoris() {
        val favoriteRoomIds = favorisDAO.obtenirTousLesFavoris()
        val favoriteChambres = modèle.obtenirChambres().filter { it.id in favoriteRoomIds }
        if (favoriteChambres.isEmpty()) {
            vue.textFavoris.layoutParams.height = 0
        }
        val adapter = ChambreAdapter(context, favoriteChambres) { chambre ->
            ouvrirDetailsChambre(chambre)
        }
        listViewChambres.adapter = adapter
    }
}