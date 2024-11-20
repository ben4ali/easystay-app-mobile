package com.easycorp.easystayapp.Presentation.Modele

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
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
    fun `Étant donné que les données sont chargées, vérifier que le singleton est bien instancié`() {
        val instance = Modèle.getInstance()
        assertNotNull(instance)
    }

    @Test
    fun `Étant donné que des chambres sont disponibles, vérifier que obtenirChambres retourne toutes les chambres`() {
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
    fun `Étant donné un type de chambre, vérifier que obtenirChambreParType retourne les chambres correspondantes`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        )
        `when`(sourceDeDonnées.obtenirChambreParType("Chambre Simple")).thenReturn(chambres)

        val result = modèle.obtenirChambreParType("Chambre Simple")

        assertEquals(chambres, result)
        verify(sourceDeDonnées).obtenirChambreParType("Chambre Simple")
    }

    @Test
    fun `Étant donné un ID de chambre, vérifier que obtenirChambreParId retourne la chambre correspondante`() {
        val chambre = ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        `when`(sourceDeDonnées.obtenirChambreParId(1)).thenReturn(chambre)

        val result = modèle.obtenirChambreParId(1)

        assertEquals(chambre, result)
        verify(sourceDeDonnées).obtenirChambreParId(1)
    }

    @Test
    fun `Étant donné des chambres disponibles, vérifier que obtenirChambresDisponibles retourne les chambres disponibles`() {
        val chambresDisponibles = listOf(
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "WiFi"), 150.0)
        )
        `when`(sourceDeDonnées.obtenirChambresDisponibles()).thenReturn(chambresDisponibles)

        val result = modèle.obtenirChambresDisponibles()

        assertEquals(chambresDisponibles, result)
        verify(sourceDeDonnées).obtenirChambresDisponibles()
    }

    @Test
    fun `Étant donné un ID de chambre choisi, vérifier que setChambreChoisieId le stocke et getChambreChoisieId le retourne`() {
        modèle.setChambreChoisieId(3)

        val id = modèle.getChambreChoisieId()

        assertEquals(3, id)
    }

    @Test
    fun `Étant donné un ID de réservation choisi, vérifier que setReservationChoisieId le stocke et getReservationChoisieId le retourne`() {
        modèle.setReservationChoisieId(5)

        val id = modèle.getReservationChoisieId()

        assertEquals(5, id)
    }

    @Test
    fun `Étant donné un ID de client, vérifier que obtenirClientParId retourne le client correspondant`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)
        `when`(sourceDeDonnées.obtenirClientParId(1)).thenReturn(client)

        val result = modèle.obtenirClientParId(1)

        assertEquals(client, result)
        verify(sourceDeDonnées).obtenirClientParId(1)
    }

    @Test
    fun `Étant donné un client, vérifier que modifierClient met à jour ses données`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)

        modèle.modifierClient(client)

        verify(sourceDeDonnées).modifierClient(client)
    }

    @Test
    fun `Étant donné une réservation, vérifier que ajouterReservation ajoute la réservation`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)
        val chambre = ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        val reservation = ReservationData(1, client, chambre, "2024-11-16", "2024-11-20")

        modèle.ajouterReservation(reservation)

        verify(sourceDeDonnées).ajouterReservation(reservation)
    }

    @Test
    fun `Étant donné un client, vérifier que obtenirReservationsParClient retourne ses réservations`() {
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
    fun `Étant donné une réservation, vérifier que supprimerRéservation la supprime`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)
        val chambre = ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        val reservation = ReservationData(1, client, chambre, "2024-11-16", "2024-11-20")

        modèle.supprimerRéservation(reservation)

        verify(sourceDeDonnées).suppressionReservation(reservation)
    }

    @Test
    fun `Étant donné une réservation, vérifier que modifierReservation met à jour ses données`() {
        val client = ClientData(1, "John", "Doe", "johndoe@gmail.com", 0)
        val chambre = ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0)
        val reservation = ReservationData(1, client, chambre, "2024-11-16", "2024-11-20")

        modèle.modifierReservation(reservation)

        verify(sourceDeDonnées).modifierReservation(reservation)
    }

    @Test
    fun `Étant donné une image Bitmap et un contexte, vérifier que modifierClientImage stocke l'image encodée en Base64 dans les SharedPreferences`() {
        modèle = Modèle.getInstance()
        val mockContext = mock(Context::class.java)
        val mockEditor = mock(SharedPreferences.Editor::class.java)
        val mockPreferences = mock(SharedPreferences::class.java)
        val mockBitmap = mock(Bitmap::class.java)

        `when`(mockContext.getSharedPreferences("client_prefs", Context.MODE_PRIVATE)).thenReturn(mockPreferences)
        `when`(mockPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)

        modèle.modifierClientImage(mockBitmap,mockContext)

        verify(mockEditor).putString(eq("client_image"), anyString())
        verify(mockEditor).apply()
    }

    @Test
    fun `Étant donné que des réservations existent, vérifier que obtenirToutesLesReservations retourne toutes les réservations`() {
        val reservations = listOf(
            ReservationData(1, ClientData(1, "John", "Doe", "johndoe@gmail.com", 0), ChambreData(1, "Chambre Simple", "Vue sur jardin", 4.0f, 10, listOf("WiFi", "Climatisation"), 100.0), "2024-11-16", "2024-11-20"),
            ReservationData(2, ClientData(2, "Jane", "Doe", "janedoe@gmail.com", 0), ChambreData(2, "Chambre Double", "Vue sur mer", 4.5f, 8, listOf("TV", "Balcon"), 150.0), "2024-12-01", "2024-12-05")
        )
        `when`(sourceDeDonnées.obtenirToutesLesReservations()).thenReturn(reservations)

        val result = modèle.obtenirToutesLesReservations()

        assertEquals(reservations, result)
        verify(sourceDeDonnées).obtenirToutesLesReservations()
    }


}
