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
                ChambreData(1,"Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0),
                ChambreData(2,"Suite Junior", "Balcon privé", 3.5f, 5, listOf("TV", "Climatisation"), 50.0),
                ChambreData(3,"Chambre Standard", "Lit queen-size", 4.0f, 8, listOf("TV", "Climatisation", "Balcon"), 100.0),
                ChambreData(4,"Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0)
            )
        )

        val client1 = ClientData(1,"John", "Doe", "johndoe@gmail.com")
        clients.add(client1)

        reservations.addAll(
            listOf(
                ReservationData(1, client1, chambres[0], "2024-11-16", "2021-11-20"),
                ReservationData(2, client1, chambres[1], "2024-11-27", "2021-12-01"),
                ReservationData(3, client1, chambres[2], "2024-12-02", "2021-12-05"),
                ReservationData(4, client1, chambres[3], "2024-12-10", "2021-12-15")
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

    override fun ajouterReservation(reservation: ReservationData) {
        reservations.add(reservation)
    }

    override fun obtenirReservationsParClient(client: ClientData): List<ReservationData> {
        return reservations.filter { it.client == client }
    }

    override fun obtenirReservationParId(id: Int): ReservationData {
        return reservations.find { it.id == id }!!
    }

    override fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData> {
        return reservations.filter { it.chambre == chambre }
    }

    override fun suppressionReservation(reservation: ReservationData) {
        reservations.remove(reservation)
    }

    override fun modifierReservation(reservation: ReservationData) {
        val index = reservations.indexOfFirst { it.id == reservation.id }
        reservations[index] = reservation
    }


}