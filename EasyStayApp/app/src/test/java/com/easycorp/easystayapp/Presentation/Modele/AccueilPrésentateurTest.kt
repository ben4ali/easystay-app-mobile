package com.easycorp.easystayapp.Presentation.Modele

import android.content.Context
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Presentateur.Accueil.AccueilPrésentateur
import com.easycorp.easystayapp.Presentation.Vue.AccueilVue
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import com.easycorp.easystayapp.Utilitaire.RéservationCourtAdapter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class AccueilPrésentateurTest {

    private lateinit var context: Context
    private lateinit var listViewReservations: ListView
    private lateinit var listViewChambres: ListView
    private lateinit var vue: AccueilVue
    private lateinit var modèle: Modèle
    private lateinit var favorisDAO: FavorisDAOImpl
    private lateinit var présentateur: AccueilPrésentateur

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        listViewReservations = mock(ListView::class.java)
        listViewChambres = mock(ListView::class.java)
        vue = mock(AccueilVue::class.java)
        modèle = mock(Modèle::class.java)
        favorisDAO = mock(FavorisDAOImpl::class.java)
        présentateur = AccueilPrésentateur(context, listViewReservations, listViewChambres, vue)
    }

    @Test
    fun `Étant donné que le client a des réservations courtes, lorsque chargerReservationsCourte est appelé, on obtient une liste de réservations courtes`() {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", 0)
        val reservations = listOf(
            ReservationData(1, client, ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0), "2024-11-16", "2024-11-20"),
            ReservationData(2, client, ChambreData(2, "Suite Junior", "Balcon privé", 3.5f, 5, listOf("TV", "Climatisation"), 50.0), "2024-11-27", "2024-12-01")
        )
        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)
        `when`(modèle.obtenirReservationsParClient(client)).thenReturn(reservations)

        présentateur.chargerReservationsCourte(clientId)

        verify(listViewReservations).adapter = any(RéservationCourtAdapter::class.java)
    }

    @Test
    fun `Étant donné que des chambres sont disponibles, lorsque chargerChambres est appelé, on obtient une liste de chambres`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0),
            ChambreData(2, "Suite Junior", "Balcon privé", 3.5f, 5, listOf("TV", "Climatisation"), 50.0),
            ChambreData(3, "Chambre Standard", "Lit queen-size", 4.0f, 8, listOf("TV", "Climatisation", "Balcon"), 100.0),
            ChambreData(4, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0)
        )
        `when`(modèle.obtenirChambres()).thenReturn(chambres)

        présentateur.chargerChambres()

        verify(listViewChambres).adapter = any(ChambreAdapter::class.java)
    }

    @Test
    fun `Étant donné que des chambres favorites sont disponibles, lorsque chargerChambresFavoris est appelé, on obtient une liste de chambres favorites`() {
        val favoriteRoomIds = listOf(1, 2)
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0),
            ChambreData(2, "Suite Junior", "Balcon privé", 3.5f, 5, listOf("TV", "Climatisation"), 50.0),
            ChambreData(3, "Chambre Standard", "Lit queen-size", 4.0f, 8, listOf("TV", "Climatisation", "Balcon"), 100.0),
            ChambreData(4, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0)
        )
        var mockFavorisDAO = mock(FavorisDAOImpl::class.java)
        `when`(mockFavorisDAO.obtenirTous()).thenReturn(favoriteRoomIds)
        `when`(modèle.obtenirChambres()).thenReturn(chambres)

        présentateur = AccueilPrésentateur(context, listViewReservations, listViewChambres, vue)
        présentateur.favorisDAO = mockFavorisDAO
        présentateur.chargerChambresFavoris()

        verify(listViewChambres).adapter = any(ChambreAdapter::class.java)
    }
}