package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.ListAdapter
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Domaine.Service.ServiceFavoris
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Presentateur.Accueil.AccueilPrésentateur
import com.easycorp.easystayapp.Presentation.Vue.AccueilVue
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import com.easycorp.easystayapp.Utilitaire.RéservationCourtAdapter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import kotlinx.coroutines.runBlocking

class AccueilPrésentateurTest {

    private lateinit var context: Context
    private lateinit var listViewReservations: ListView
    private lateinit var listViewChambres: ListView
    private lateinit var vue: AccueilVue
    private lateinit var modèle: Modèle
    private lateinit var serviceFavoris: ServiceFavoris
    private lateinit var présentateur: AccueilPrésentateur

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        listViewReservations = mock(ListView::class.java)
        listViewChambres = mock(ListView::class.java)
        vue = mock(AccueilVue::class.java)
        modèle = mock(Modèle::class.java)
        serviceFavoris = mock(ServiceFavoris::class.java)
    }

    @Test
    fun `Étant donné que le client a des réservations courtes, lorsque chargerReservationsCourte est appelé, on obtient une liste de réservations courtes`() = runBlocking {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", "")
        val reservations = listOf(
            ReservationData(1, client, "1", "2024-11-16", "2024-11-20", 150.0, "Confirmed", "Credit Card", true, "2024-11-20", ChambreData(1, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf("Télévision", "Wi-fi", "Climatisation"))),
            ReservationData(2, client, "2", "2024-11-27", "2024-12-01", 50.0, "Confirmed", "Credit Card", true, "2024-12-01", ChambreData(2, "Suite Junior", 50.0, "Available", "Clean", 3, 5, listOf("TV", "Climatisation"), listOf("Télévision", "Wi-fi", "Climatisation")))
        )
        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)
        `when`(modèle.obtenirReservationsParClient(client)).thenReturn(reservations)

        présentateur.chargerReservationsCourte(
            clientId,
            viewPager = TODO()
        )

        verify(listViewReservations).adapter = any(ListAdapter::class.java)
    }

    @Test
    fun `Étant donné que des chambres sont disponibles, lorsque chargerChambres est appelé, on obtient une liste de chambres`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf("Télévision", "Wi-fi", "Climatisation")),
            ChambreData(2, "Suite Junior", 50.0, "Available", "Clean", 3, 5, listOf("TV", "Climatisation"), listOf("Télévision", "Wi-fi", "Climatisation")),
            ChambreData(3, "Chambre Standard", 100.0, "Available", "Clean", 4, 8, listOf("TV", "Climatisation", "Balcon"), listOf("Télévision", "Wi-fi", "Climatisation")),
            ChambreData(4, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf("Télévision", "Wi-fi", "Climatisation"))
        )
        `when`(modèle.obtenirChambres()).thenReturn(chambres)

        présentateur.chargerChambres()

        verify(listViewChambres).adapter = any(ChambreAdapter::class.java)
    }

    @Test
    fun `Étant donné que des chambres favorites sont disponibles, lorsque chargerChambresFavoris est appelé, on obtient une liste de chambres favorites`() {
        val favoriteRoomIds = listOf(1, 2)
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf("Télévision", "Wi-fi", "Climatisation")),
            ChambreData(2, "Suite Junior", 50.0, "Available", "Clean", 3, 5, listOf("TV", "Climatisation"), listOf("Télévision", "Wi-fi", "Climatisation")),
            ChambreData(3, "Chambre Standard", 100.0, "Available", "Clean", 4, 8, listOf("TV", "Climatisation", "Balcon"), listOf("Télévision", "Wi-fi", "Climatisation")),
            ChambreData(4, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf("Télévision", "Wi-fi", "Climatisation"))
        )
        var mockFavorisDAO = mock(FavorisDAOImpl::class.java)
        `when`(mockFavorisDAO.obtenirTous()).thenReturn(favoriteRoomIds)
        `when`(modèle.obtenirChambres()).thenReturn(chambres)

        serviceFavoris = ServiceFavoris(mockFavorisDAO)
        présentateur.chargerChambresFavoris()

        verify(listViewChambres).adapter = any(ChambreAdapter::class.java)
    }
}