package com.easycorp.easystayapp.Presentation.Presentateur.Accueil

import android.content.Context
import android.widget.ListView
import androidx.navigation.fragment.findNavController
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.AccueilVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import com.easycorp.easystayapp.Utilitaire.RéservationCourtAdapter

class AccueilPrésentateur(
    private val context: Context,
    private val listViewReservations: ListView,
    private val listViewChambres: ListView,
    private val vue: AccueilVue,
) : AccueilPrésentateurInterface {
    var favorisDAO = FavorisDAOImpl(context)
    private val modèle = Modèle.getInstance()

    override fun chargerReservationsCourte(clientId: Int) {
        val reservations = modèle.obtenirReservationsParClient(modèle.obtenirClientParId(clientId))
        val filteredReservations = reservations.filter { it.obtenirNombreDeJours() <= 20 && it.obtenirNombreDeJours() >= 0 }
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
        modèle.setCheminVersChambreDetails(R.id.action_chambreDetailsFragment_to_fragment_accueil)
        modèle.setCheminVersReservation(R.id.action_reserverFragment_to_chambreDetailsFragment)
        vue.findNavController().navigate(R.id.action_fragment_accueil_to_chambreDetailsFragment)
    }

    override fun chargerChambresFavoris() {
        val favoriteRoomIds = favorisDAO.obtenirTous()
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