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

class JsonConversion {
    companion object {

        var source = SourceDeDonneeAPI("http://idefix.dti.crosemont.quebec:9022", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkFqWUJxeDZ1TjFHemlYTW54d2ZGeCJ9.eyJodHRwOi8vaXNtYWlsZWxhc3Jhb3VpLmNvbS9lbWFpbCI6InJvYmVydG1heGltZUBob3RlbC5xYy5jYSIsImh0dHA6Ly9pc21haWxlbGFzcmFvdWkuY29tL3JvbGVzIjpbIkFkbWluIl0sImlzcyI6Imh0dHBzOi8vZGV2LTI0aHEzdmI2YjhydjJmN2QudXMuYXV0aDAuY29tLyIsInN1YiI6ImF1dGgwfDY3NTRiMWEwMTk1MjYyYjFjZTZmYmMzZiIsImF1ZCI6Imh0dHA6Ly9pc21haWxlbGFzcmFvdWkuY29tIiwiaWF0IjoxNzM0NjcxNDUwLCJleHAiOjE3MzQ3NTc4NTAsImd0eSI6InBhc3N3b3JkIiwiYXpwIjoiUExHYWZycmRKU3lMTGxudmJoc2c4SmJJdTQ2TlBGSTMiLCJwZXJtaXNzaW9ucyI6WyJjcmVhdGU6Y2hhbWJyZXMiLCJjcmVhdGU6Y2xpZW50cyIsImNyZWF0ZTpyZXNlcnZhdGlvbnMiLCJkZWxldGU6Y2hhbWJyZXMiLCJkZWxldGU6Y2xpZW50cyIsImRlbGV0ZTpyZXNlcnZhdGlvbnMiLCJyZWFkOmNoYW1icmVzIiwicmVhZDpjbGllbnRzIiwicmVhZDpyZXNlcnZhdGlvbnMiLCJ1cGRhdGU6Y2hhbWJyZXMiLCJ1cGRhdGU6Y2xpZW50cyIsInVwZGF0ZTpyZXNlcnZhdGlvbnMiXX0.DORF7BJtTIu0W2ofeNvPDORwZwtYEjfJRB-OdTGX3praMf1ka6AHSbNAA78nFELqJ7Cd-cbG_-LGHlyQPMqqd_fIDgbKZFcLWiUbjN1Mf5EBOdIqUX_GoobrNsc23cZm_JyDOWE6O4ylt07PrqrnxSBmZFyikRr-hYjMuOqgJwMlyCdduPOEzwprGzrOIRsbSo3trAEkxNORYsfbCm04GsSVH4zfjALbpFgx5zhWHs3jro1pNV4f7Pc1yQE6ni56T2vIrk8gpKvz9_ZdvXlWPBBH1CHwbI_HicytLbmWDq1w4xNAeGohqsloTtqXJsCALy43PwofwT9ujvaFmhSF7g")

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
                            val dateArriver: String = (reader.nextString()).substring(0, 10)
                            LocalDate.parse(dateArriver)
                        }
                        "dateFin" -> dateFin = if (reader.peek() == JsonToken.NULL) {
                            reader.nextNull()
                            null
                        } else {
                            val datePartir: String = (reader.nextString()).substring(0, 10)
                            LocalDate.parse(datePartir)
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
