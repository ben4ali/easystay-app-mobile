package com.easycorp.easystayapp.Presentation.Modele

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

import org.junit.Assert.*

class ModèleTest {

    private lateinit var modèle: Modèle
    private lateinit var sourceDeDonnées: SourceBidon

    @Before
    fun setUp() {
        sourceDeDonnées = mock(SourceBidon::class.java)
        modèle = Modèle.getInstance()
        val field = Modèle::class.java.getDeclaredField("sourceDeDonnées")
        field.isAccessible = true
        field.set(modèle, sourceDeDonnées)
    }

    @Test
    fun `Étant donné que des chambres sont disponibles, lorsque obtenirChambres est appelé, on obtient une liste de toutes les chambres`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0),
            ChambreData(2, "Chambre Double", "Vue sur mer", 4.5f, 8, listOf("TV", "Balcon"), 150.0)
        )
        `when`(sourceDeDonnées.obtenirChambres()).thenReturn(chambres)

        val result = modèle.obtenirChambres()

        assertEquals(chambres, result)
        verify(sourceDeDonnées).obtenirChambres()
    }

    @Test
    fun `Étant donné un type de chambre spécifique, lorsque obtenirChambreParType est appelé, on obtient les chambres correspondantes`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        )
        `when`(sourceDeDonnées.obtenirChambreParType("Chambre Simple")).thenReturn(chambres)

        val result = modèle.obtenirChambreParType("Chambre Simple")

        assertEquals(chambres, result)
        verify(sourceDeDonnées).obtenirChambreParType("Chambre Simple")
    }

    @Test
    fun `Étant donné l'ID d'une chambre, lorsque obtenirChambreParId est appelé, on obtient la chambre correspondante`() {
        val chambre = ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        `when`(sourceDeDonnées.obtenirChambreParId(1)).thenReturn(chambre)

        val result = modèle.obtenirChambreParId(1)

        assertEquals(chambre, result)
        verify(sourceDeDonnées).obtenirChambreParId(1)
    }

    @Test
    fun `Étant donné un ID de chambre choisi, lorsque setChambreChoisieId est appelé, alors getChambreChoisieId retourne cet ID`() {
        modèle.setChambreChoisieId(2)
        val id = modèle.getChambreChoisieId()

        assertEquals(2, id)
    }

    @Test
    fun `Étant donné une réservation valide, lorsque ajouterReservation est appelé, la réservation est ajoutée`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)
        val chambre = ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        val reservation = ReservationData(1, client, chambre, "2024-11-16", "2024-11-20")

        modèle.ajouterReservation(reservation)

        verify(sourceDeDonnées).ajouterReservation(reservation)
    }

    @Test
    fun `Étant donné un client existant, lorsque obtenirReservationsParClient est appelé, on obtient les réservations de ce client`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)
        val reservations = listOf(
            ReservationData(1, client, ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0), "2024-11-16", "2024-11-20")
        )
        `when`(sourceDeDonnées.obtenirReservationsParClient(client)).thenReturn(reservations)

        val result = modèle.obtenirReservationsParClient(client)

        assertEquals(reservations, result)
        verify(sourceDeDonnées).obtenirReservationsParClient(client)
    }

    @Test
    fun `Étant donné une réservation existante, lorsque supprimerRéservation est appelé, la réservation est supprimée`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)
        val chambre = ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        val reservation = ReservationData(1, client, chambre, "2024-11-16", "2024-11-20")

        modèle.supprimerRéservation(reservation)

        verify(sourceDeDonnées).suppressionReservation(reservation)
    }
}
