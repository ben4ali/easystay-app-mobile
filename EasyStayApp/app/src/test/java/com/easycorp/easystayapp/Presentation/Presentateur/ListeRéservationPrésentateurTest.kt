package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Presentateur.ListeRéservation.ListeRéservationPrésentateur
import com.easycorp.easystayapp.Presentation.Vue.ListeReservationsVue
import com.easycorp.easystayapp.Utilitaire.RéservationAdapter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import kotlinx.coroutines.runBlocking

class ListeRéservationPrésentateurTest {

    private lateinit var context: Context
    private lateinit var listView: ListView
    private lateinit var vue: ListeReservationsVue
    private lateinit var modèle: Modèle
    private lateinit var présentateur: ListeRéservationPrésentateur

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        listView = mock(ListView::class.java)
        vue = mock(ListeReservationsVue::class.java)
        modèle = mock(Modèle::class.java)
        présentateur = ListeRéservationPrésentateur(context, listView, vue)
    }

    @Test
    fun `Étant donné que le client a des réservations, lorsque chargerReservations est appelé, on obtient une liste de réservations`() = runBlocking {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", "0")
        val reservations = listOf(
            ReservationData(1, client, "1", "2024-11-16", "2024-11-20", 150.0, "Confirmed", "Credit Card", true, "2024-11-20", ChambreData(1, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf())),
            ReservationData(2, client, "2", "2024-11-27", "2024-12-01", 150.0, "Confirmed", "Credit Card", true, "2024-12-01", ChambreData(2, "Chambre Double", 150.0, "Available", "Clean", 4, 8, listOf("TV", "Balcon"), listOf()))
        )
        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)
        `when`(runBlocking { modèle.obtenirReservationsParClient(client) }).thenReturn(reservations)

        présentateur.chargerReservations(clientId)

        verify(listView).adapter = any(RéservationAdapter::class.java)
    }

    @Test
    fun `Étant donné qu'une réservation est supprimée, lorsque supprimerReservation est appelé, la liste de réservations est mise à jour`() = runBlocking {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", "0")
        val reservation = ReservationData(1, client, "1", "2024-11-16", "2024-11-20", 150.0, "Confirmed", "Credit Card", true, "2024-11-20", ChambreData(1, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf()))
        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)
        `when`(runBlocking { modèle.obtenirReservationsParClient(client) }).thenReturn(emptyList())

        présentateur.supprimerReservation(reservation)

        assert(runBlocking { modèle.obtenirReservationsParClient(client) }.isEmpty())
    }
}