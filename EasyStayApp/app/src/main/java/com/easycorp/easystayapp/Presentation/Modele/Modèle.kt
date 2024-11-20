package com.easycorp.easystayapp.Presentation.Modele

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon
import java.io.ByteArrayOutputStream

class Modèle private constructor() {

    @Volatile
    private var chambreChoisieId: Int? = null

    @Volatile
    private var reservationChoisieId: Int? = null

    @Volatile
    private var dateDébutChoisie: String? = null

    @Volatile
    private var dateFinChoisie: String? = null

    @Volatile
    private var cheminVersFragment: Int? = null

    @Volatile
    private var clientImage: Bitmap? = null

    @Volatile
    var sourcePage: SourcePage? = null

    enum class SourcePage {
        LISTE_RESERVATIONS,
        CHAMBRE_DETAILS
    }

    companion object {
        @Volatile
        private var instance: Modèle? = null

        fun getInstance(): Modèle {
            return instance ?: synchronized(this) {
                instance ?: Modèle().also { instance = it }
            }
        }
    }

    private val sourceDeDonnées: SourceBidon = SourceBidon()

    fun setCheminVersFragment(chemin: Int) {
        cheminVersFragment = chemin
    }
    fun getCheminVersFragmentRéserver(): Int? {
        return cheminVersFragment
    }

    // chambres
    fun obtenirChambres(): List<ChambreData> {
        return sourceDeDonnées.obtenirChambres()
    }

    fun obtenirChambreParType(type: String): List<ChambreData> {
        return sourceDeDonnées.obtenirChambreParType(type)
    }

    fun obtenirChambreParId(id: Int): ChambreData {
        return sourceDeDonnées.obtenirChambreParId(id)
    }

    fun obtenirChambresDisponibles(): List<ChambreData> {
        return sourceDeDonnées.obtenirChambresDisponibles()
    }

    fun setChambreChoisieId(id: Int) {
        chambreChoisieId = id
    }

    fun getChambreChoisieId(): Int? {
        return chambreChoisieId
    }

    // reservations
    fun obtenirToutesLesReservations(): List<ReservationData> {
        return sourceDeDonnées.obtenirToutesLesReservations()
    }

    fun setReservationChoisieId(id: Int) {
        reservationChoisieId = id
    }

    fun getReservationChoisieId(): Int? {
        return reservationChoisieId
    }

    fun obtenirClientParId(id: Int): ClientData {
        return sourceDeDonnées.obtenirClientParId(id)
    }

    fun modifierClient(client: ClientData) {
        sourceDeDonnées.modifierClient(client)
    }

    fun ajouterReservation(réservation: ReservationData) {
        sourceDeDonnées.ajouterReservation(réservation)
    }

    fun obtenirReservationsParClient(client: ClientData): List<ReservationData> {
        return sourceDeDonnées.obtenirReservationsParClient(client)
    }

    fun obtenirReservationParId(id: Int): ReservationData {
        return sourceDeDonnées.obtenirReservationParId(id)
    }

    fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData> {
        return sourceDeDonnées.obtenirReservationParChambre(chambre)
    }

    fun supprimerRéservation(réservation: ReservationData) {
        sourceDeDonnées.suppressionReservation(réservation)
    }

    fun modifierReservation(réservation: ReservationData) {
        sourceDeDonnées.modifierReservation(réservation)
    }

    fun setDates(dateDébut: String, dateFin: String) {
        dateDébutChoisie = dateDébut
        dateFinChoisie = dateFin
    }

    fun modifierClientSurname(newSurname: String) {
        sourceDeDonnées.modifierClientSurname(newSurname)
    }

    fun modifierClientName(newName: String) {
        sourceDeDonnées.modifierClientName(newName)
    }

    fun modifierClientEmail(newEmail: String) {
        sourceDeDonnées.modifierClientEmail(newEmail)
    }

    fun modifierClientImage(newImage: Bitmap, context: Context) {
        val editor = context.getSharedPreferences("client_prefs", Context.MODE_PRIVATE).edit()
        val byteArrayOutputStream = ByteArrayOutputStream()
        newImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageString = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
        editor.putString("client_image", imageString)
        editor.apply()
    }

    fun obtenirClientImage(context: Context): Bitmap? {
        val sharedPreferences = context.getSharedPreferences("client_prefs", Context.MODE_PRIVATE)
        val imageString = sharedPreferences.getString("client_image", null) ?: return null
        val imageBytes = Base64.decode(imageString, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}