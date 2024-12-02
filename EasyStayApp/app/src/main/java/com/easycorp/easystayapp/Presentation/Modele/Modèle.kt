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
import com.easycorp.easystayapp.SourceDeDonnes.SourceBidon
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonneeAPI
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonnées
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
    private val sourceDeDonnées: SourceDeDonnées = SourceDeDonneeAPI("http://idefix.dti.crosemont.quebec:9002", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkkzNzJZUHJaMkNTS2pQNVFkNWVZdSJ9.eyJpc3MiOiJodHRwczovL2Rldi1rcHBuemZnczZkNG5heTZqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdkBjbGllbnRzIiwiYXVkIjoiaHR0cDovL2hvdGVsL2FwaSIsImlhdCI6MTczMzA3NzMzMiwiZXhwIjoxNzMzMTYzNzMyLCJzY29wZSI6InJlYWQ6Y2hhbWJyZXMgY3JlYXRlOmNoYW1icmVzIHVwZGF0ZTpjaGFtYnJlcyBkZWxldGU6Y2hhbWJyZXMgcmVhZDpyZXNlcnZhdGlvbnMgY3JlYXRlOnJlc2VydmF0aW9ucyB1cGRhdGU6cmVzZXJ2YXRpb25zIGRlbGV0ZTpyZXNlcnZhdGlvbnMgcmVhZDpjYXJhY3RlcmlzdGlxdWUiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMiLCJhenAiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdiIsInBlcm1pc3Npb25zIjpbInJlYWQ6Y2hhbWJyZXMiLCJjcmVhdGU6Y2hhbWJyZXMiLCJ1cGRhdGU6Y2hhbWJyZXMiLCJkZWxldGU6Y2hhbWJyZXMiLCJyZWFkOnJlc2VydmF0aW9ucyIsImNyZWF0ZTpyZXNlcnZhdGlvbnMiLCJ1cGRhdGU6cmVzZXJ2YXRpb25zIiwiZGVsZXRlOnJlc2VydmF0aW9ucyIsInJlYWQ6Y2FyYWN0ZXJpc3RpcXVlIl19.UW6eWyCF082Xx_pQmoTScpYbYVE4qDaex_W_D5jB1No25O7cKnlshZ-0Q7HlMrLyR-c5Jn1sCWfkyc2B8eccP7tQdsGN4kDdrHuEVLR_QAxDpQPKHJfeF6RM2ic3o2g9Yf-BNzeDUGyRzcfB_IPl90hl2SvJBfFp_av9c51cpdFlzU9ES5SU-VIqgZQMZuKSV-qsVXUYu8R99nEMVzecF5xJSebmkmgeZM5TPQofIojjz_JdtHWr69H3xGWjRao0LGKbTFs7X4JJZOvtz9Lunyto2NxyOpS9b85tr-_4Nqt-qd66D7wrAJNi8chpx7vCbpBi7BntvoIUzfmQivu54A")
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

    fun modifierClientSurname(newSurname: String) {
        serviceClient.modifierClientSurname(newSurname)
    }

    fun modifierClientName(newName: String) {
        serviceClient.modifierClientName(newName)
    }

    fun modifierClientEmail(newEmail: String) {
        serviceClient.modifierClientEmail(newEmail)
    }

    fun modifierClientImage(newImage: Bitmap, context: Context) {
        val editor = context.getSharedPreferences("client_prefs", Context.MODE_PRIVATE).edit()
        val byteArrayOutputStream = ByteArrayOutputStream()
        newImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        val imageString = Base64.encodeBase64String(byteArrayOutputStream.toByteArray())
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