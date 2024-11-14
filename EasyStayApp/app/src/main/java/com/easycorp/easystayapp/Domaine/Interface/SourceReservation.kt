package com.easycorp.easystayapp.Domaine.Interface

import com.easycorp.easystayapp.Domaine.Entite.ReservationData

interface SourceReservation {
    var listeReservation: ArrayList<ReservationData>

    fun getListeReservation(): List<ReservationData>

    fun addReservation(reservation: ReservationData)

    fun updateReservation(reservation: ReservationData)

    fun findRerservationById(id: Int): ReservationData?

    fun deleteReservationById(id: Int)

}