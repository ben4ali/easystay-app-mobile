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
    private val sourceDeDonnées: SourceDeDonnées = SourceDeDonneeAPI("http://idefix.dti.crosemont.quebec:9052", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkFqWUJxeDZ1TjFHemlYTW54d2ZGeCJ9.eyJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbS9lbWFpbCI6InJvYmVydG1heGltZTFAaG90ZWwucWMuY2EiLCJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbS9yb2xlcyI6WyJBZG1pbiJdLCJpc3MiOiJodHRwczovL2Rldi0yNGhxM3ZiNmI4cnYyZjdkLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw2NzU1YzhhNTUxNjNiYjcxNWVlZmM5OWQiLCJhdWQiOiJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbSIsImlhdCI6MTczNDY2MTQ1OSwiZXhwIjoxNzM0NzQ3ODU5LCJndHkiOiJwYXNzd29yZCIsImF6cCI6IlBMR2FmcnJkSlN5TExsbnZiaHNnOEpiSXU0Nk5QRkkzIiwicGVybWlzc2lvbnMiOlsiY3JlYXRlOmNoYW1icmVzIiwiY3JlYXRlOmNsaWVudHMiLCJjcmVhdGU6cmVzZXJ2YXRpb25zIiwiZGVsZXRlOmNoYW1icmVzIiwiZGVsZXRlOmNsaWVudHMiLCJkZWxldGU6cmVzZXJ2YXRpb25zIiwicmVhZDpjaGFtYnJlcyIsInJlYWQ6Y2xpZW50cyIsInJlYWQ6cmVzZXJ2YXRpb25zIiwidXBkYXRlOmNoYW1icmVzIiwidXBkYXRlOmNsaWVudHMiLCJ1cGRhdGU6cmVzZXJ2YXRpb25zIl19.HtZwmMYEUTgQNnPjW-iERCfFhCzm4M5dOy9yN0mRpp5bTrbgkBflJIsZQkrYP7545_Ff-uBWlTEEvXeteXdxVrqcQ5Kf0S78Glb-e4zryLNJcXL1sd1oSgzo_mqJvli8WEIhPi6niKAhan1sERhBbIct7pG19bOe2NCSev0wHZp45BuqyMcIbVky5JFZf7QPsF7dBbz4R0ak0bqQmDFtqQBClmdImkyfyEjqTsDYN8DtsQ8dUj5SnYr1bBnh8o9eqQ9Ga2qxFdMSUPasgxt-yiT8xwks3OfiVOZunfh0tGv36QbiaOi8zSC64IgMVsoEa6PXmBkPk-M3ynKhQbTvyA")
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