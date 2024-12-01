package com.easycorp.easystayapp.Utilitaire

import android.util.JsonReader
import android.util.JsonToken
import android.util.Log
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

            return ChambreData(
                id,
                typeChambre,
                prixParNuit,
                statutDisponibilite,
                statutNettoyage,
                note,
                nombreAvis,
                caracteristique,
                equipement,
                photos
            )
        }

        suspend fun jsonAReservations(json: String): List<ReservationData> {
            val liste = mutableListOf<ReservationData?>()
            val reader = JsonReader(StringReader(json))

            reader.beginArray()
            while (reader.hasNext()) {
                val reservation = try {
                    jsonAReservation(reader)
                } catch (e: Exception) {
                    Log.e("jsonAReservations", "Erreur lors du traitement d'une réservation : ${e.message}", e)
                    null
                }
                liste.add(reservation)
            }
            reader.endArray()

            return liste.filterNotNull()
        }

        suspend fun jsonAReservation(reader: JsonReader): ReservationData {
            var id = 0
            var client: ClientData? = null
            var chambreNumero = ""
            var dateDebut = ""
            var dateFin = ""
            var prixTotal = 0.0
            var statut = ""
            var methodePaiement = ""
            var statusPaiement = false
            var datePaiement = ""

            try {
                reader.beginObject()
                while (reader.hasNext()) {
                    val clé = reader.nextName()
                    when (clé) {
                        "id" -> id = reader.nextInt()
                        "client" -> {
                            client = if (reader.peek() == JsonToken.NULL) {
                                reader.nextNull()
                                ClientData(0, "unknown", "unknown", "unknown", null)
                            } else {
                                jsonAClient(reader)
                            }
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
                    if (chambreNumero.isNotEmpty()) {
                        try {
                            source.obtenirChambreParId(chambreNumero.toInt())
                        } catch (e: Exception) {
                            Log.e("obtenirChambreParId", "Erreur lors de l'obtention de la chambre : ${e.message}", e)
                            null
                        }
                    } else {
                        null
                    }
                } ?: ChambreData(
                    id = -1,
                    typeChambre = "Inconnu",
                    prixParNuit = 0.0,
                    statutDisponibilite = "Indisponible",
                    statutNettoyage = "Inconnu",
                    note = 0,
                    nombreAvis = 0,
                    caracteristique = emptyList(),
                    equipement = emptyList(),
                    photos = emptyList()
                )

                return ReservationData(
                    id,
                    client ?: ClientData(0, "unknown", "unknown", "unknown", null),
                    chambreNumero,
                    dateDebut,
                    dateFin,
                    prixTotal,
                    statut,
                    methodePaiement,
                    statusPaiement,
                    datePaiement,
                    chambre
                )
            } catch (e: Exception) {
                Log.e("jsonAReservation", "Erreur lors du traitement : ${e.message}", e)
                throw e
            }
        }

        fun jsonAClient(reader: JsonReader): ClientData {
            var id = 0
            var courriel = ""
            var prenom = ""
            var nom = ""
            var photo: String? = null

            reader.beginObject()
            while (reader.hasNext()) {
                val clé = reader.nextName()
                when (clé) {
                    "id" -> id = reader.nextInt()
                    "courriel" -> courriel = reader.nextString()
                    "prenom" -> prenom = reader.nextString()
                    "nom" -> nom = reader.nextString()
                    "photo" -> photo = if (reader.peek() == JsonToken.NULL) {
                        reader.nextNull()
                        null
                    } else {
                        reader.nextString()
                    }
                    else -> reader.skipValue()
                }
            }
            reader.endObject()

            return ClientData(id, courriel, prenom, nom, photo)
        }
    }
}
