package com.easycorp.easystayapp.Presentation.Modele

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Domaine.Service.ServiceChambre
import com.easycorp.easystayapp.Domaine.Service.ServiceClient
import com.easycorp.easystayapp.Domaine.Service.ServiceReservation
import com.easycorp.easystayapp.Domaine.Service.ServiceFavoris
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonneeAPI
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonnées
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream

class Modèle private constructor(context: Context) {

    @Volatile
    private var chambreChoisieId: Int? = null

    @Volatile
    private var reservationChoisieId: Int? = null

    @Volatile
    private var dateDébutChoisie: String? = null

    @Volatile
    private var dateFinChoisie: String? = null

    @Volatile
    private var cheminVersReservation: Int? = null

    @Volatile
    private var cheminVersChambreDetails: Int? = null

    @Volatile
    private var clientImage: Bitmap? = null

    fun getDateDébutChoisie(): String? {
        return dateDébutChoisie
    }

    fun getDateFinChoisie(): String? {
        return dateFinChoisie
    }

    companion object {
        @Volatile
        private var instance: Modèle? = null

        fun getInstance(context: Context): Modèle {
            return instance ?: synchronized(this) {
                instance ?: Modèle(context).also { instance = it }
            }
        }
    }

//    private val sourceDeDonnées: SourceBidon = SourceBidon()
    private val sourceDeDonnées: SourceDeDonnées = SourceDeDonneeAPI("http://idefix.dti.crosemont.quebec:9022", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkFqWUJxeDZ1TjFHemlYTW54d2ZGeCJ9.eyJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbS9lbWFpbCI6InJvYmVydG1heGltZUBob3RlbC5xYy5jYSIsImh0dHA6Ly9pc21haWxlbGFzcmFvdWkuY29tL3JvbGVzIjpbIkFkbWluIl0sImlzcyI6Imh0dHBzOi8vZGV2LTI0aHEzdmI2YjhydjJmN2QudXMuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDY3NTRiMWEwMTk1MjYyYjFjZTZmYmMzZiIsImF1ZCI6Imh0dHA6Ly9pc21haWxlbGFzcmFvdWkuY29tIiwiaWF0IjoxNzM0NjcxNDUwLCJleHAiOjE3MzQ3NTc4NTAsImd0eSI6InBhc3N3b3JkIiwiYXpwIjoiUExHYWZycmRKU3lMTGxudmJoc2c4SmJJdTQ2TlBGSTMiLCJwZXJtaXNzaW9ucyI6WyJjcmVhdGU6Y2hhbWJyZXMiLCJjcmVhdGU6Y2xpZW50cyIsImNyZWF0ZTpyZXNlcnZhdGlvbnMiLCJkZWxldGU6Y2hhbWJyZXMiLCJkZWxldGU6Y2xpZW50cyIsImRlbGV0ZTpyZXNlcnZhdGlvbnMiLCJyZWFkOmNoYW1icmVzIiwicmVhZDpjbGllbnRzIiwicmVhZDpyZXNlcnZhdGlvbnMiLCJ1cGRhdGU6Y2hhbWJyZXMiLCJ1cGRhdGU6Y2xpZW50cyIsInVwZGF0ZTpyZXNlcnZhdGlvbnMiXX0.DORF7BJtTIu0W2ofeNvPDORwZwtYEjfJRB-OdTGX3praMf1ka6AHSbNAA78nFELqJ7Cd-cbG_-LGHlyQPMqqd_fIDgbKZFcLWiUbjN1Mf5EBOdIqUX_GoobrNsc23cZm_JyDOWE6O4ylt07PrqrnxSBmZFyikRr-hYjMuOqgJwMlyCdduPOEzwprGzrOIRsbSo3trAEkxNORYsfbCm04GsSVH4zfjALbpFgx5zhWHs3jro1pNV4f7Pc1yQE6ni56T2vIrk8gpKvz9_ZdvXlWPBBH1CHwbI_HicytLbmWDq1w4xNAeGohqsloTtqXJsCALy43PwofwT9ujvaFmhSF7g")
    private val serviceChambre = ServiceChambre(sourceDeDonnées)
    private val serviceClient = ServiceClient(sourceDeDonnées)
    private val serviceReservation = ServiceReservation(sourceDeDonnées)
    private val serviceFavoris = ServiceFavoris(FavorisDAOImpl(context))

    fun setCheminVersReservation(chemin: Int) {
        cheminVersReservation = chemin
    }
    fun getCheminVersReservation(): Int? {
        return cheminVersReservation
    }

    fun setCheminVersChambreDetails(chemin: Int) {
        cheminVersChambreDetails = chemin
    }
    fun getCheminVersChambreDetails(): Int? {
        return cheminVersChambreDetails
    }

    // chambres
    fun obtenirChambres(): List<ChambreData>? {
        return serviceChambre.obtenirChambres()
    }

    fun obtenirChambreParType(type: String): List<ChambreData>? {
        return serviceChambre.obtenirChambreParType(type)
    }

    fun obtenirChambreParId(id: Int): ChambreData? {
        return serviceChambre.obtenirChambreParId(id)
    }

    fun filtrerChambres(type: String?, prix: Int?): List<ChambreData>? {
        return serviceChambre.filtrerChambres(type, prix)
    }

    fun rechercherChambresParMotCle(keyword: String): List<ChambreData>? {
        return serviceChambre.rechercherChambresParMotCle(keyword)
    }

    fun obtenirChambresDisponibles(): List<ChambreData>? {
        return serviceChambre.obtenirChambresDisponibles()
    }

    fun setChambreChoisieId(id: Int) {
        chambreChoisieId = id
    }

    fun getChambreChoisieId(): Int? {
        return chambreChoisieId
    }

    // reservations
    suspend fun obtenirToutesLesReservations(): List<ReservationData>? {
        return serviceReservation.obtenirToutesLesReservations()
    }

    fun setReservationChoisieId(id: Int) {
        reservationChoisieId = id
    }

    fun getReservationChoisieId(): Int? {
        return reservationChoisieId
    }

    fun obtenirClientParId(id: Int): ClientData {
        return serviceClient.obtenirClientParId(id)
    }

    fun modifierClient(client: ClientData) {
        serviceClient.modifierClient(client)
    }

    suspend fun ajouterReservation(réservation: ReservationData, clientData: ClientData, chambre: ChambreData) {
        serviceReservation.ajouterReservation(réservation, clientData, chambre)
    }

    suspend fun obtenirReservationsParClient(client: ClientData): List<ReservationData> {
        return serviceReservation.obtenirReservationsParClient(client)
    }

    suspend fun obtenirReservationParId(id: Int): ReservationData? {
        return serviceReservation.obtenirReservationParId(id)
    }

    suspend fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData> {
        return serviceReservation.obtenirReservationParChambre(chambre)
    }

    fun supprimerRéservation(réservation: ReservationData) {
        serviceReservation.supprimerRéservation(réservation)
    }

    fun modifierReservation(réservation: ReservationData) {
        serviceReservation.modifierReservation(réservation)
    }

    fun setDates(dateDébut: String, dateFin: String) {
        dateDébutChoisie = dateDébut
        dateFinChoisie = dateFin
    }

    fun modifierClientPrenom(clientId: Int,newPrenom: String) {
        serviceClient.modifierClientPrenom(clientId,newPrenom)
    }

    fun modifierClientCourriel(clientId: Int,newCourriel: String) {
        serviceClient.modifierClientCourriel(clientId,newCourriel)
    }

    fun modifierClientNom(clientId: Int,newNom: String) {
        serviceClient.modifierClientNom(clientId,newNom)
    }

    suspend fun modifierClientImage(newImage: Bitmap, context: Context, clientId: Int) {
        val byteArrayOutputStream = ByteArrayOutputStream()
        newImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageString = Base64.encodeBase64String(byteArrayOutputStream.toByteArray())
        CoroutineScope(Dispatchers.IO).launch {
            sourceDeDonnées.modifierClientImage(imageString, clientId)
        }
        println("l'image a été envoyé a l'api")
        val editor = context.getSharedPreferences("client_prefs", Context.MODE_PRIVATE).edit()
        editor.putString("client_image", imageString)
        editor.apply()
    }

    fun obtenirClientImage(context: Context): Bitmap? {
        val sharedPreferences = context.getSharedPreferences("client_prefs", Context.MODE_PRIVATE)
        val imageString = sharedPreferences.getString("client_image", null) ?: return null
        val imageBytes = Base64.decodeBase64(imageString)
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    // favoris
    fun ajouterFavori(roomId: Int) {
        serviceFavoris.ajouter(roomId)
    }

    fun retirerFavori(roomId: Int) {
        serviceFavoris.retirer(roomId)
    }

    fun estFavori(roomId: Int): Boolean {
        return serviceFavoris.estFavoris(roomId)
    }

    fun obtenirTousLesFavoris(): List<Int> {
        return serviceFavoris.obtenirTous()
    }
}