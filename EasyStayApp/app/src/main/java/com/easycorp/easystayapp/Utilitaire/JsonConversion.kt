package com.easycorp.easystayapp.Utilitaire

import android.util.JsonReader
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonneeAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.StringReader

class JsonConversion {
    companion object {

        var source = SourceDeDonneeAPI("https://api.easystay.com", "token")

        fun jsonAChambres(json: String): List<ChambreData> {
            val liste = mutableListOf<ChambreData>()
            val reader = JsonReader(StringReader(json))

            reader.beginArray()
            while (reader.hasNext()) {
                liste.add(jsonAChambre(reader))
            }
            reader.endArray()

            return liste
        }

        fun jsonAChambre(reader: JsonReader): ChambreData {
            var id = 0
            var typeChambre = ""
            var prixParNuit = 0.0
            var statutDisponibilite = ""
            var statutNettoyage = ""
            var note = 0
            var nombreAvis = 0
            var caracteristique = listOf<String>()
            var equipement = listOf<String>()
            var photos = listOf<String>()

            reader.beginObject()
            while (reader.hasNext()) {
                val clé = reader.nextName()
                when (clé) {
                    "id" -> id = reader.nextInt()
                    "typeChambre" -> typeChambre = reader.nextString()
                    "prixParNuit" -> prixParNuit = reader.nextDouble()
                    "statutDisponibilite" -> statutDisponibilite = reader.nextString()
                    "statutNettoyage" -> statutNettoyage = reader.nextString()
                    "note" -> note = reader.nextInt()
                    "nombreAvis" -> nombreAvis = reader.nextInt()
                    "caracteristique" -> {
                        val caracteristiqueList = mutableListOf<String>()
                        reader.beginArray()
                        while (reader.hasNext()) {
                            caracteristiqueList.add(reader.nextString())
                        }
                        reader.endArray()
                        caracteristique = caracteristiqueList
                    }
                    "equipement" -> {
                        val equipementList = mutableListOf<String>()
                        reader.beginArray()
                        while (reader.hasNext()) {
                            equipementList.add(reader.nextString())
                        }
                        reader.endArray()
                        equipement = equipementList
                    }
                    "photos" -> {
                        val photosList = mutableListOf<String>()
                        reader.beginArray()
                        while (reader.hasNext()) {
                            photosList.add(reader.nextString())
                        }
                        reader.endArray()
                        photos = photosList
                    }
                    else -> reader.skipValue()
                }
            }
            reader.endObject()

            return ChambreData(id, typeChambre, prixParNuit, statutDisponibilite, statutNettoyage, note, nombreAvis, caracteristique, equipement, photos)
        }

        suspend fun jsonAReservations(json: String): List<ReservationData> {
            val liste = mutableListOf<ReservationData>()
            val reader = JsonReader(StringReader(json))

            reader.beginArray()
            while (reader.hasNext()) {
                liste.add(jsonAReservation(reader))
            }
            reader.endArray()

            return liste
        }

        suspend fun jsonAReservation(reader: JsonReader): ReservationData {
            var id = 0
            lateinit var client: ClientData
            var chambreNumero = ""
            var dateDebut = ""
            var dateFin = ""
            var prixTotal = 0.0
            var statut = ""
            var methodePaiement = ""
            var statusPaiement = false
            var datePaiement = ""

            reader.beginObject()
            while (reader.hasNext()) {
                val clé = reader.nextName()
                when (clé) {
                    "id" -> id = reader.nextInt()
                    "client" -> {
                        client = ClientData(0, "", "", "", "")
                    }
                    "chambreNumero" -> chambreNumero = reader.nextString()
                    "dateDebut" -> dateDebut = reader.nextString()
                    "dateFin" -> dateFin = reader.nextString()
                    "prix_total" -> prixTotal = reader.nextDouble()
                    "statut" -> statut = reader.nextString()
                    "methode_paiement" -> methodePaiement = reader.nextString()
                    "status_paiement" -> statusPaiement = reader.nextBoolean()
                    "date_paiement" -> datePaiement = reader.nextString()
                    else -> reader.skipValue()
                }
            }
            reader.endObject()

            val chambre = withContext(Dispatchers.IO) {
                source.obtenirChambreParId(chambreNumero.toInt())
            }

            return ReservationData(id, client, chambreNumero, dateDebut, dateFin, prixTotal, statut, methodePaiement, statusPaiement, datePaiement, chambre!!)
        }

//        fun jsonAClient(reader: JsonReader): ClientData {
//            var id = 0
//            var courriel = ""
//            var prenom = ""
//            var nom = ""
//            var photo: String? = null
//
//            reader.beginObject()
//            while (reader.hasNext()) {
//                if (reader.peek() == JsonReader.Token.NULL) {
//                    reader.nextNull()
//                    continue
//                }
//                val clé = reader.nextName()
//                when (clé) {
//                    "id" -> id = reader.nextInt()
//                    "courriel" -> courriel = if (reader.peek() != JsonReader.Token.NULL) reader.nextString() else {
//                        reader.nextNull()
//                        ""
//                    }
//                    "prenom" -> prenom = if (reader.peek() != JsonReader.Token.NULL) reader.nextString() else {
//                        reader.nextNull()
//                        ""
//                    }
//                    "nom" -> nom = if (reader.peek() != JsonReader.Token.NULL) reader.nextString() else {
//                        reader.nextNull()
//                        ""
//                    }
//                    "photo" -> photo = if (reader.peek() != JsonReader.Token.NULL) reader.nextString() else {
//                        reader.nextNull()
//                        null
//                    }
//                    else -> reader.skipValue()
//                }
//            }
//            reader.endObject()
//
//            return ClientData(id, prenom, nom, courriel, photo)
//        }
    }
}