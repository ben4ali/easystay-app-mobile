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
    fun `Étant donné que le client a des réservations, lorsque chargerReservations est appelé, on obtient une liste de réservations`() {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", 0)
        val reservations = listOf(
            ReservationData(1, client, ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0), "2024-11-16", "2024-11-20"),
            ReservationData(2, client, ChambreData(2, "Chambre Double", "Vue sur mer", 4.5f, 8, listOf("TV", "Balcon"), 150.0), "2024-11-27", "2024-12-01")
        )
        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)
        `when`(modèle.obtenirReservationsParClient(client)).thenReturn(reservations)

        présentateur.chargerReservations(clientId)

        verify(listView).adapter = any(RéservationAdapter::class.java)
    }

    @Test
    fun `Étant donné qu'une réservation est supprimée, lorsque supprimerReservation est appelé, la liste de réservations est mise à jour`() {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", 0)
        val reservation = ReservationData(1, client, ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0), "2024-11-16", "2024-11-20")
        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)
        `when`(modèle.obtenirReservationsParClient(client)).thenReturn(emptyList())

        présentateur.supprimerReservation(reservation)

        assert(modèle.obtenirReservationsParClient(client).isEmpty())
    }
}