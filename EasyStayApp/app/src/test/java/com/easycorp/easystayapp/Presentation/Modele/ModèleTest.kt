package com.easycorp.easystayapp.Presentation.Modele

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import org.junit.Assert.*
import org.junit.jupiter.api.Assertions.assertThrows

class ModèleTest {

    private lateinit var modèle: Modèle
    private lateinit var sourceDeDonnées: SourceBidon
    private lateinit var mockContext: Context

    @Before
    fun setUp() {
        sourceDeDonnées = mock(SourceBidon::class.java)
        mockContext = mock(Context::class.java)
        modèle = Modèle.getInstance(mockContext)
        val field = Modèle::class.java.getDeclaredField("sourceDeDonnées")
        field.isAccessible = true
        field.set(modèle, sourceDeDonnées)
    }

    @Test
    fun `Étant donné que les données sont chargées, vérifier que le singleton est bien instancié`() {
        val instance = Modèle.getInstance(mockContext)
        assertNotNull(instance)
    }

    @Test
    fun `Étant donné que des chambres sont disponibles, vérifier que obtenirChambres retourne toutes les chambres`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf()),
            ChambreData(2, "Chambre Double", 150.0, "Disponible", "Nettoyée", 4, 8, listOf("TV", "Balcon"), listOf())
        )
        `when`(sourceDeDonnées.obtenirChambres()).thenReturn(chambres)

        val result = modèle.obtenirChambres()

        assertEquals(chambres, result)
        verify(sourceDeDonnées).obtenirChambres()
    }

    @Test
    fun `Étant donné un type de chambre, vérifier que obtenirChambreParType retourne les chambres correspondantes`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())
        )
        `when`(sourceDeDonnées.obtenirChambreParType("Chambre Simple")).thenReturn(chambres)

        val result = modèle.obtenirChambreParType("Chambre Simple")

        assertEquals(chambres, result)
        verify(sourceDeDonnées).obtenirChambreParType("Chambre Simple")
    }

    @Test
    fun `Étant donné un ID de chambre, vérifier que obtenirChambreParId retourne la chambre correspondante`() {
        val chambre = ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())
        `when`(sourceDeDonnées.obtenirChambreParId(1)).thenReturn(chambre)

        val result = modèle.obtenirChambreParId(1)

        assertEquals(chambre, result)
        verify(sourceDeDonnées).obtenirChambreParId(1)
    }

    @Test
    fun `Étant donné des chambres disponibles, vérifier que obtenirChambresDisponibles retourne les chambres disponibles`() {
        val chambresDisponibles = listOf(
            ChambreData(1, "Chambre Deluxe", 150.0, "Disponible", "Nettoyée", 4, 10, listOf("TV", "WiFi"), listOf())
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
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)
        `when`(sourceDeDonnées.obtenirClientParId(1)).thenReturn(client)

        val result = modèle.obtenirClientParId(1)

        assertEquals(client, result)
        verify(sourceDeDonnées).obtenirClientParId(1)
    }

    @Test
    fun `Étant donné un client, vérifier que modifierClient met à jour ses données`() {
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)

        modèle.modifierClient(client)

        verify(sourceDeDonnées).modifierClient(client)
    }

    @Test
    fun `Étant donné une réservation, vérifier que ajouterReservation ajoute la réservation`() = runBlocking {
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)
        val chambre = ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())
        val reservation = ReservationData(1, client, "1", "2024-11-16", "2024-11-20", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", chambre)

        modèle.ajouterReservation(reservation, client, chambre)

        verify(sourceDeDonnées).ajouterReservation(reservation, client, chambre)
    }

    @Test
    fun `Étant donné un client, vérifier que obtenirReservationsParClient retourne ses réservations`(): Unit = runBlocking {
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)
        val reservations = listOf(
            ReservationData(1, client, "1", "2024-11-16", "2024-11-20", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf()))
        )
        `when`(sourceDeDonnées.obtenirReservationsParClient(client)).thenReturn(reservations)

        val result = modèle.obtenirReservationsParClient(client)

        assertEquals(reservations, result)
        verify(sourceDeDonnées).obtenirReservationsParClient(client)
    }

    @Test
    fun `Étant donné une réservation, vérifier que supprimerRéservation la supprime`() {
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)
        val chambre = ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())
        val reservation = ReservationData(1, client, "1", "2024-11-16", "2024-11-20", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", chambre)

        modèle.supprimerRéservation(reservation)

        verify(sourceDeDonnées).suppressionReservation(reservation)
    }

    @Test
    fun `Étant donné une réservation, vérifier que modifierReservation met à jour ses données`() {
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)
        val chambre = ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())
        val reservation = ReservationData(1, client, "1", "2024-11-16", "2024-11-20", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", chambre)

        modèle.modifierReservation(reservation)

        verify(sourceDeDonnées).modifierReservation(reservation)
    }

    @Test
    fun `Étant donné une image Bitmap et un contexte, vérifier que modifierClientImage stocke l'image encodée en Base64 dans les SharedPreferences`() = runBlocking {
        val mockEditor = mock(SharedPreferences.Editor::class.java)
        val mockPreferences = mock(SharedPreferences::class.java)
        val mockBitmap = mock(Bitmap::class.java)

        `when`(mockContext.getSharedPreferences("client_prefs", Context.MODE_PRIVATE)).thenReturn(mockPreferences)
        `when`(mockPreferences.edit()).thenReturn(mockEditor)
        `when`(mockEditor.putString(anyString(), anyString())).thenReturn(mockEditor)

        modèle.modifierClientImage(mockBitmap, mockContext, 1)

        verify(mockEditor).putString(eq("client_image"), anyString())
        verify(mockEditor).apply()
    }

    @Test
    fun `Étant donné que des réservations existent, vérifier que obtenirToutesLesReservations retourne toutes les réservations`(): Unit = runBlocking {
        val reservations = listOf(
            ReservationData(1, ClientData(1, "johndoe@gmail.com", "John", "Doe", null), "1", "2024-11-16", "2024-11-20", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())),
            ReservationData(2, ClientData(2, "janedoe@gmail.com", "Jane", "Doe", null), "2", "2024-12-01", "2024-12-05", 500.0, "Confirmée", "PayPal", true, "2024-11-30", ChambreData(2, "Chambre Double", 150.0, "Disponible", "Nettoyée", 4, 8, listOf("TV", "Balcon"), listOf()))
        )
        `when`(sourceDeDonnées.obtenirToutesLesReservations()).thenReturn(reservations)

        val result = modèle.obtenirToutesLesReservations()

        assertEquals(reservations, result)
        verify(sourceDeDonnées).obtenirToutesLesReservations()
    }

    @Test
    fun `Étant donné un ID de réservation, vérifier que obtenirReservationParId retourne la réservation correspondante`(): Unit = runBlocking {
        val reservation = ReservationData(1, ClientData(1, "johndoe@gmail.com", "John", "Doe", null), "1", "2024-11-16", "2024-11-20", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf()))
        `when`(sourceDeDonnées.obtenirReservationParId(1)).thenReturn(reservation)

        val result = modèle.obtenirReservationParId(1)

        assertEquals(reservation, result)
        verify(sourceDeDonnées).obtenirReservationParId(1)
    }

    @Test
    fun `Étant donné une date de début et de fin, vérifier que setDates les stocke correctement`() {
        modèle.setDates("2024-11-01", "2024-11-10")

        assertEquals("2024-11-01", modèle.getDateDébutChoisie())
        assertEquals("2024-11-10", modèle.getDateFinChoisie())
    }

    @Test
    fun `Étant donné un nouveau nom de famille, vérifier que modifierClientNom le met à jour`() {
        val newNom = "Smith"

        modèle.modifierClientNom(1, newNom)

        verify(sourceDeDonnées).modifierClientNom(1, newNom)
    }

    @Test
    fun `Étant donné un nouveau prénom, vérifier que modifierClientPrenom le met à jour`() {
        val newPrenom = "Michael"

        modèle.modifierClientPrenom(1, newPrenom)

        verify(sourceDeDonnées).modifierClientPrenom(1, newPrenom)
    }

    @Test
    fun `Étant donné un nouvel email, vérifier que modifierClientCourriel le met à jour`() {
        val newCourriel = "michael.smith@gmail.com"

        modèle.modifierClientCourriel(1, newCourriel)

        verify(sourceDeDonnées).modifierClientCourriel(1, newCourriel)
    }

    @Test
    fun `Étant donné un type de chambre inexistant, vérifier que obtenirChambreParType retourne une liste vide`() {
        `when`(sourceDeDonnées.obtenirChambreParType("Chambre Inexistante")).thenReturn(emptyList())

        val result = modèle.obtenirChambreParType("Chambre Inexistante")

        if (result != null) {
            assertTrue(result.isEmpty())
        }
        verify(sourceDeDonnées).obtenirChambreParType("Chambre Inexistante")
    }

    @Test
    fun `Étant donné un ID de réservation inexistant, vérifier que obtenirReservationParId retourne null`(): Unit = runBlocking {
        `when`(sourceDeDonnées.obtenirReservationParId(999)).thenReturn(null)

        val result = modèle.obtenirReservationParId(999)

        assertNull(result)
        verify(sourceDeDonnées).obtenirReservationParId(999)
    }

    @Test
    fun `Étant donné des dates invalides, vérifier que ajouterReservation lance une exception`(): Unit = runBlocking {
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)
        val chambre = ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())
        val reservation = ReservationData(1, client, "1", "2024-11-20", "2024-11-16", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", chambre)

        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                modèle.ajouterReservation(reservation, client, chambre)
            }
        }
    }
    @Test
    fun `Étant donné aucune chambre disponible, vérifier que obtenirChambres retourne une liste vide`() {
        `when`(sourceDeDonnées.obtenirChambres()).thenReturn(emptyList())

        val result = modèle.obtenirChambres()

        if (result != null) {
            assertTrue(result.isEmpty())
        }
        verify(sourceDeDonnées).obtenirChambres()
    }

    @Test
    fun `Étant donné un ID de chambre négatif, vérifier que obtenirChambreParId retourne null`() {
        `when`(sourceDeDonnées.obtenirChambreParId(-1)).thenReturn(null)

        val result = modèle.obtenirChambreParId(-1)

        assertNull(result)
        verify(sourceDeDonnées).obtenirChambreParId(-1)
    }

    @Test
    fun `Étant donné un client vide, vérifier que ajouterReservation lance une exception`(): Unit = runBlocking {
        val chambre = ChambreData(1, "Chambre Simple", 100.0, "Disponible", "Nettoyée", 4, 10, listOf("WiFi", "Climatisation"), listOf())
        val reservation = ReservationData(1, ClientData(1, "", "", "", null), "1", "2024-11-16", "2024-11-20", 400.0, "Confirmée", "Carte de crédit", true, "2024-11-15", chambre)

        assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                modèle.ajouterReservation(reservation, ClientData(1, "", "", "", null), chambre)
            }
        }
    }

    @Test
    fun `Étant donné un client vide, vérifier que modifierClient lance une exception`() {
        assertThrows(IllegalArgumentException::class.java) {
            modèle.modifierClient(ClientData(1, "", "", "", null))
        }
    }

    @Test
    fun `Étant donné un client sans réservations, vérifier que obtenirReservationsParClient retourne une liste vide`(): Unit = runBlocking {
        val client = ClientData(1, "johndoe@gmail.com", "John", "Doe", null)
        `when`(sourceDeDonnées.obtenirReservationsParClient(client)).thenReturn(emptyList())

        val result = modèle.obtenirReservationsParClient(client)

        assertTrue(result.isEmpty())
        verify(sourceDeDonnées).obtenirReservationsParClient(client)
    }

}