import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*

class ModeleTest {

    private lateinit var modele: Modèle
    private lateinit var mockSourceDeDonnees: SourceBidon

    @BeforeEach
    fun setUp() {
        mockSourceDeDonnees = mock(SourceBidon::class.java)
        modele = Modèle.getInstance()
    }

    @Test
    fun obtenirChambres_returnsListOfRooms() {
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0),
            ChambreData(2, "Suite Junior", "Balcon privé", 3.5f, 5, listOf("TV", "Climatisation"), 50.0)
        )
        `when`(mockSourceDeDonnees.obtenirChambres()).thenReturn(chambres)

        val result = modele.obtenirChambres()

        assertEquals(chambres, result)
        verify(mockSourceDeDonnees).obtenirChambres()
    }

    @Test
    fun obtenirChambreParType_returnsRoomsOfType() {
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0)
        )
        `when`(mockSourceDeDonnees.obtenirChambreParType("Deluxe")).thenReturn(chambres)

        val result = modele.obtenirChambreParType("Deluxe")

        assertEquals(chambres, result)
        verify(mockSourceDeDonnees).obtenirChambreParType("Deluxe")
    }

    @Test
    fun obtenirChambreParId_returnsRoomById() {
        val chambre = ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0)
        `when`(mockSourceDeDonnees.obtenirChambreParId(1)).thenReturn(chambre)

        val result = modele.obtenirChambreParId(1)

        assertEquals(chambre, result)
        verify(mockSourceDeDonnees).obtenirChambreParId(1)
    }

    @Test
    fun setChambreChoisieId_setsChosenRoomId() {
        modele.setChambreChoisieId(2)
        assertEquals(2, modele.getChambreChoisieId())
    }

    @Test
    fun ajouterReservation_addsReservation() {
        val reservation = ReservationData(
            1,
            ClientData(1, "John Doe", "johndoe@gmail.com", "514-555-1234"),
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV"), 150.0),
            "2024-11-15",
            "2024-11-20"
        )
        doNothing().`when`(mockSourceDeDonnees).ajouterReservation(reservation)

        modele.ajouterReservation(reservation)

        verify(mockSourceDeDonnees).ajouterReservation(reservation)
    }

    @Test
    fun obtenirReservationsParClient_returnsReservationsForClient() {
        val client = ClientData(1, "John Doe", "johndoe@gmail.com", "514-555-1234")
        val reservations = listOf(
            ReservationData(
                1,
                client,
                ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV"), 150.0),
                "2024-11-15",
                "2024-11-20"
            )
        )
        `when`(mockSourceDeDonnees.obtenirReservationsParClient(client)).thenReturn(reservations)

        val result = modele.obtenirReservationsParClient(client)

        assertEquals(reservations, result)
        verify(mockSourceDeDonnees).obtenirReservationsParClient(client)
    }

    @Test
    fun supprimerReservation_removesReservation() {
        val reservation = ReservationData(
            1,
            ClientData(1, "John Doe", "johndoe@gmail.com", "514-555-1234"),
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV"), 150.0),
            "2024-11-15",
            "2024-11-20"
        )
        doNothing().`when`(mockSourceDeDonnees).suppressionReservation(reservation)

        modele.supprimerRéservation(reservation)

        verify(mockSourceDeDonnees).suppressionReservation(reservation)
    }

    @Test
    fun modifierClient_updatesClientInfo() {
        val client = ClientData(1, "Jane Doe", "janedoe@gmail.com", "514-555-6789")
        doNothing().`when`(mockSourceDeDonnees).modifierClient(client)

        modele.modifierClient(client)

        verify(mockSourceDeDonnees).modifierClient(client)
    }
}
