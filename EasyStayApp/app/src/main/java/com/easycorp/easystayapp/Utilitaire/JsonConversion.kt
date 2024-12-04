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

        var source = SourceDeDonneeAPI("http://idefix.dti.crosemont.quebec:9002", "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCIsImtpZCI6IkkzNzJZUHJaMkNTS2pQNVFkNWVZdSJ9.eyJpc3MiOiJodHRwczovL2Rldi1rcHBuemZnczZkNG5heTZqLnVzLmF1dGgwLmNvbS8iLCJzdWIiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdkBjbGllbnRzIiwiYXVkIjoiaHR0cDovL2hvdGVsL2FwaSIsImlhdCI6MTczMzI2NTUwMywiZXhwIjoxNzMzMzUxOTAzLCJzY29wZSI6InJlYWQ6Y2hhbWJyZXMgY3JlYXRlOmNoYW1icmVzIHVwZGF0ZTpjaGFtYnJlcyBkZWxldGU6Y2hhbWJyZXMgcmVhZDpyZXNlcnZhdGlvbnMgY3JlYXRlOnJlc2VydmF0aW9ucyB1cGRhdGU6cmVzZXJ2YXRpb25zIGRlbGV0ZTpyZXNlcnZhdGlvbnMgcmVhZDpjYXJhY3RlcmlzdGlxdWUiLCJndHkiOiJjbGllbnQtY3JlZGVudGlhbHMiLCJhenAiOiI4RGM5WXNhZGpWb1NscHRLS081ZXFxQUc5VVc1SmYxdiIsInBlcm1pc3Npb25zIjpbInJlYWQ6Y2hhbWJyZXMiLCJjcmVhdGU6Y2hhbWJyZXMiLCJ1cGRhdGU6Y2hhbWJyZXMiLCJkZWxldGU6Y2hhbWJyZXMiLCJyZWFkOnJlc2VydmF0aW9ucyIsImNyZWF0ZTpyZXNlcnZhdGlvbnMiLCJ1cGRhdGU6cmVzZXJ2YXRpb25zIiwiZGVsZXRlOnJlc2VydmF0aW9ucyIsInJlYWQ6Y2FyYWN0ZXJpc3RpcXVlIl19.A69jZ-UUaDcEN5Tur-ht3n6SCX7llXpv9OM5hELJIYxAipDAhcs9Wn-fdjG8UxGaAu_uQ-ZI8sr1m9ILVmG-QJwfRW96Lg4eeleaKIn6-RGF6q6GUvUZk772CrNBmyOoD0IfLta1pXPQFE4RDaYh1zDOgyzxHVuxoEYbZEdm6H26bDh7J9QAyUzsrE8K9irDmYGHq42MZLHMuMH5fjQ0yKq8YGmZeJKc4Ob8i4ipG6WK_WVkCmjFQ6qUcVgMUtR7saFtZhL00z6v_bpOFSYvCsax2NY6-Fxo3lnk3gKQTtr4meZLsxdsg4t-F6x5s1Ko1j1NtW1e5BTyUR6hokAaZQ")

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
