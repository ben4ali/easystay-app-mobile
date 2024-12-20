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
import java.time.LocalDate
import java.time.LocalDateTime

class JsonConversion {
    companion object {

        var source = SourceDeDonneeAPI("http://idefix.dti.crosemont.quebec:9052", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkFqWUJxeDZ1TjFHemlYTW54d2ZGeCJ9.eyJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbS9lbWFpbCI6InJvYmVydG1heGltZTFAaG90ZWwucWMuY2EiLCJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbS9yb2xlcyI6WyJBZG1pbiJdLCJpc3MiOiJodHRwczovL2Rldi0yNGhxM3ZiNmI4cnYyZjdkLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiJhdXRoMHw2NzU1YzhhNTUxNjNiYjcxNWVlZmM5OWQiLCJhdWQiOiJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbSIsImlhdCI6MTczNDY2MTQ1OSwiZXhwIjoxNzM0NzQ3ODU5LCJndHkiOiJwYXNzd29yZCIsImF6cCI6IlBMR2FmcnJkSlN5TExsbnZiaHNnOEpiSXU0Nk5QRkkzIiwicGVybWlzc2lvbnMiOlsiY3JlYXRlOmNoYW1icmVzIiwiY3JlYXRlOmNsaWVudHMiLCJjcmVhdGU6cmVzZXJ2YXRpb25zIiwiZGVsZXRlOmNoYW1icmVzIiwiZGVsZXRlOmNsaWVudHMiLCJkZWxldGU6cmVzZXJ2YXRpb25zIiwicmVhZDpjaGFtYnJlcyIsInJlYWQ6Y2xpZW50cyIsInJlYWQ6cmVzZXJ2YXRpb25zIiwidXBkYXRlOmNoYW1icmVzIiwidXBkYXRlOmNsaWVudHMiLCJ1cGRhdGU6cmVzZXJ2YXRpb25zIl19.HtZwmMYEUTgQNnPjW-iERCfFhCzm4M5dOy9yN0mRpp5bTrbgkBflJIsZQkrYP7545_Ff-uBWlTEEvXeteXdxVrqcQ5Kf0S78Glb-e4zryLNJcXL1sd1oSgzo_mqJvli8WEIhPi6niKAhan1sERhBbIct7pG19bOe2NCSev0wHZp45BuqyMcIbVky5JFZf7QPsF7dBbz4R0ak0bqQmDFtqQBClmdImkyfyEjqTsDYN8DtsQ8dUj5SnYr1bBnh8o9eqQ9Ga2qxFdMSUPasgxt-yiT8xwks3OfiVOZunfh0tGv36QbiaOi8zSC64IgMVsoEa6PXmBkPk-M3ynKhQbTvyA")

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
                    "numero" -> id = reader.nextInt()
                    "type" -> typeChambre = reader.nextString()
                    "prix" -> prixParNuit = reader.nextDouble()
                    "statutDisponibilite" -> statutDisponibilite = reader.nextString()
                    "statutNettoyage" -> statutNettoyage = reader.nextString()
                    "note" -> note = reader.nextInt()
                    "nombreAvis" -> nombreAvis = reader.nextInt()
                    "caractéristique" -> {
                        val caracteristiqueList = mutableListOf<String>()
                        reader.beginArray()
                        while (reader.hasNext()) {
                            caracteristiqueList.add(reader.nextString())
                        }
                        reader.endArray()
                        caracteristique = caracteristiqueList
                    }
                    "équipement" -> {
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
            var dateDebut: LocalDate? = null
            var dateFin: LocalDate? = null
            var prixTotal = 0.0
            var statut = ""
            var methodePaiement = ""
            var statusPaiement = false
            var datePaiement = ""

            try {
                reader.beginObject()
                while (reader.hasNext()) {
                    val name = reader.nextName()
                    when (name) {
                        "id" -> id = reader.nextInt()
                        "client" -> {
                            reader.beginObject()
                            var clientId = 0
                            var clientNom = ""
                            var clientPrenom = ""
                            var clientCourriel = ""
                            var clientPhoto: String? = null
                            while (reader.hasNext()) {
                                when (reader.nextName()) {
                                    "id" -> clientId = reader.nextInt()
                                    "nom" -> clientNom = reader.nextString()
                                    "prenom" -> clientPrenom = reader.nextString()
                                    "courriel" -> clientCourriel = reader.nextString()
                                    "photo" -> clientPhoto = if (reader.peek() == JsonToken.NULL) {
                                        reader.nextNull()
                                        null
                                    } else {
                                        reader.nextString()
                                    }
                                }
                            }
                            reader.endObject()
                            client = ClientData(clientId, clientNom, clientPrenom, clientCourriel, clientPhoto)
                        }
                        "chambreNumero" -> chambreNumero = reader.nextString()
                        "dateDebut" -> dateDebut = if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull()
                            null
                        } else {
                            LocalDate.parse(reader.nextString())
                        }
                        "dateFin" -> dateFin = if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull()
                            null
                        } else {
                            LocalDate.parse(reader.nextString())
                        }
                        "prixTotal" -> prixTotal = reader.nextDouble()
                        "statut" -> statut = reader.nextString()
                        "methodePaiement" -> methodePaiement = reader.nextString()
                        "statusPaiement" -> statusPaiement = reader.nextBoolean()
                        "datePaiement" -> datePaiement = if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull()
                            ""
                        } else {
                            reader.nextString()
                        }
                        else -> reader.skipValue()
                    }
                }
                reader.endObject()

                val chambre = withContext(Dispatchers.IO) {
                    if (chambreNumero.isNotEmpty()) {
                        source.obtenirChambreParId(chambreNumero.toInt())
                    } else {
                        ChambreData(
                            id = 0,
                            typeChambre = "unknown",
                            prixParNuit = 0.0,
                            statutDisponibilite = "Indisponible",
                            statutNettoyage = "Inconnu",
                            note = 0,
                            nombreAvis = 0,
                            caracteristique = emptyList(),
                            equipement = emptyList(),
                            photos = emptyList()
                        )
                    }
                }

                return chambre?.let {
                    ReservationData(
                        id,
                        client ?: ClientData(0, "unknown", "unknown", "unknown", null),
                        chambreNumero,
                        dateDebut?.toString() ?: "",
                        dateFin?.toString() ?: "",
                        prixTotal,
                        statut,
                        methodePaiement,
                        statusPaiement,
                        datePaiement,
                        it
                    )
                } ?: ReservationData(
                    id,
                    client ?: ClientData(0, "unknown", "unknown", "unknown", null),
                    chambreNumero,
                    dateDebut?.toString() ?: "",
                    dateFin?.toString() ?: "",
                    prixTotal,
                    statut,
                    methodePaiement,
                    statusPaiement,
                    datePaiement,
                    ChambreData(
                        id = 0,
                        typeChambre = "unknown",
                        prixParNuit = 0.0,
                        statutDisponibilite = "Indisponible",
                        statutNettoyage = "Inconnu",
                        note = 0,
                        nombreAvis = 0,
                        caracteristique = emptyList(),
                        equipement = emptyList(),
                        photos = emptyList()
                    )
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
