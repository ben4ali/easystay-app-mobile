package com.easycorp.easystayapp.SourceDeDonnes

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData

class SourceBidon : SourceDeDonnées{

    private val chambres = mutableListOf<ChambreData>()
    private val clients = mutableListOf<ClientData>()
    private val reservations = mutableListOf<ReservationData>()

    init {
        chambres.addAll(
            listOf(
                ChambreData(1, "Chambre Deluxe", 150.0, "Disponible", "Nettoyée", 5, 10, listOf("Vue sur la mer"), listOf("TV", "Climatisation", "Balcon"), listOf()),
                ChambreData(2, "Suite Junior", 50.0, "Disponible", "Nettoyée", 4, 5, listOf("Balcon privé"), listOf("TV", "Climatisation"), listOf()),
                ChambreData(3, "Chambre Standard", 100.0, "Disponible", "Nettoyée", 4, 8, listOf("Lit queen-size"), listOf("TV", "Climatisation", "Balcon"), listOf()),
                ChambreData(4, "Chambre Deluxe", 150.0, "Disponible", "Nettoyée", 5, 10, listOf("Vue sur la mer"), listOf("TV", "Climatisation", "Balcon"), listOf())
            )
        )

        val client1 = ClientData(1,"John", "Doe", "johndoe@gmail.com", "daad")
        clients.add(client1)

        reservations.addAll(
            listOf(
                ReservationData(1, client1, "1", "24-11-2024", "26-11-2024", 300.0, "Confirmée", "Carte de crédit", true, "23-11-2024", chambres[0]),
                ReservationData(2, client1, "2", "27-11-2024", "01-12-2024", 400.0, "Confirmée", "PayPal", true, "26-11-2024", chambres[1]),
                ReservationData(3, client1, "3", "02-12-2024", "05-12-2024", 500.0, "Confirmée", "Espèces", true, "01-12-2024", chambres[2]),
                ReservationData(4, client1, "4", "10-12-2024", "15-12-2024", 600.0, "Confirmée", "Carte de crédit", true, "09-12-2024", chambres[3]),
                ReservationData(5, client1, "1", "20-12-2024", "25-12-2024", 700.0, "Confirmée", "PayPal", true, "19-12-2024", chambres[0]),
                ReservationData(6, client1, "2", "27-12-2024", "31-12-2024", 800.0, "Confirmée", "Espèces", true, "26-12-2024", chambres[1]),
            )
        )
    }

    override fun obtenirChambres(): List<ChambreData> {
        return chambres
    }

    override fun obtenirChambreParType(typeChambre: String): List<ChambreData> {
        return chambres.filter { it.typeChambre == typeChambre }
    }

    override fun obtenirChambreParId(id: Int): ChambreData {
        return chambres.find { it.id == id }!!
    }

    override fun obtenirChambresDisponibles(): List<ChambreData> {
        return chambres
    }

    override fun ajouterClient(client: ClientData) {
        clients.add(client)
    }

    override fun obtenirClientParId(id: Int): ClientData {
        return clients.find { it.id == id }!!
    }

    override fun modifierClient(client: ClientData){
        val index = clients.indexOfFirst { it.id == client.id }
        clients[index] = client
    }

    override fun obtenirToutesLesReservations(): List<ReservationData> {
        return reservations
    }

    override fun ajouterReservation(reservation: ReservationData) {
        reservations.add(reservation)
    }

    override suspend fun obtenirReservationsParClient(client: ClientData): List<ReservationData> {
        return reservations.filter { it.client == client }
    }

    override fun obtenirReservationParId(id: Int): ReservationData {
        return reservations.find { it.id == id }!!
    }

    override suspend fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData> {
        return reservations.filter { it.chambre == chambre }
    }

    override fun suppressionReservation(reservation: ReservationData) {
        reservations.remove(reservation)
    }

    override fun modifierReservation(reservation: ReservationData) {
        val index = reservations.indexOfFirst { it.id == reservation.id }
        reservations[index] = reservation
    }

    override fun modifierClientName(newName: String) {
        clients[0].nom = newName
    }

    override fun modifierClientSurname(newSurname: String) {
        clients[0].prénom = newSurname
    }

    override fun modifierClientEmail(newEmail: String) {
        clients[0].courriel = newEmail
    }

    override fun modifierClientImage(newImage: String) {
        clients[0].photo = newImage
    }


}