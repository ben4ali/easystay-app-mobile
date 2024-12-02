package com.easycorp.easystayapp.SourceDeDonnes

import android.icu.text.SimpleDateFormat
import android.util.JsonReader
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Utilitaire.JsonConversion
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException
import java.io.StringReader
import java.util.Locale

class SourceDeDonneeAPI(val url_api: String, val bearerToken: String) : SourceDeDonnées {
    private val client = OkHttpClient()

    override fun obtenirChambres(): List<ChambreData>? {
        val request = Request.Builder()
            .url("$url_api/chambres")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { json ->
                    return JsonConversion.jsonAChambres(json)
                }
            }
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun obtenirChambreParType(typeChambre: String): List<ChambreData>? {
        val request = Request.Builder()
            .url("$url_api/chambres?type=$typeChambre")
            .addHeader("Authorization","Bearer $bearerToken")
            .build()
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { json ->
                    return JsonConversion.jsonAChambres(json)
                }
            }
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }
    override fun rechercherChambresParMotCle(keyword: String): List<ChambreData>? {
        val request = Request.Builder()
            .url("$url_api/chambres?keyword=$keyword")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { json ->
                    return JsonConversion.jsonAChambres(json)
                }
            }
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun filtrerChambres(type: String?, prix: Int?): List<ChambreData>? {
        val urlBuilder = StringBuilder("$url_api/chambres?")
        type?.let { urlBuilder.append("type=$it&") }
        prix?.let { urlBuilder.append("prix=$it&") }
        val url = urlBuilder.toString().removeSuffix("&")

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { json ->
                    JsonConversion.jsonAChambres(json)
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }


    suspend fun obtenirReservations(): List<ReservationData>? {
        val request = Request.Builder()
            .url("$url_api/reservations")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { json ->
                    return JsonConversion.jsonAReservations(json)
                }
            }
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun obtenirChambreParId(numéro: Int): ChambreData? {
        val request = Request.Builder()
            .url("$url_api/chambres/$numéro")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { json ->
                    return JsonConversion.jsonAChambre(JsonReader(StringReader(json)))
                }
            }
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    override fun obtenirChambresDisponibles(): List<ChambreData>? {
        val request = Request.Builder()
            .url("$url_api/chambres?statutDisponibilite=Disponible")
            .addHeader("Authorization","Bearer $bearerToken")
            .build()
        return try {
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                response.body?.string()?.let { json ->
                    return JsonConversion.jsonAChambres(json)
                }
            }
            null
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    //PAS BESOIN
    override fun ajouterClient(client: ClientData) {
        TODO("Not yet implemented")
    }

    override fun obtenirClientParId(id: Int): ClientData {
        return ClientData(1, "DupontJ@gmail.com", "Jean", "Dupont", "dadadad")
    }

    override fun modifierClient(clientData: ClientData) {
        val json =
                "{" +
                    "\"nom\": \"${clientData.nom}\"," +
                    "\"prénom\": \"${clientData.prénom}\"," +
                    "\"courriel\": \"${clientData.courriel}\"," +
                    "\"photo\": \"${clientData.photo}\"" +
                "}"
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$url_api/clients/${clientData.id}")
            .put(body)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }

    //PAS BESOIN
    override suspend fun obtenirToutesLesReservations(): List<ReservationData>? {
        //reservations
        val request = Request.Builder()
            .url("$url_api/reservations")
            .addHeader("Authorization","Bearer $bearerToken")
            .build()
        return client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            response.body?.string()?.let { json ->
                return JsonConversion.jsonAReservations(json)
            } ?: emptyList()
        }
    }


    override suspend fun ajouterReservation(reservationData: ReservationData, clientData: ClientData, chambre: ChambreData) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.CANADA_FRENCH)

        val dateDebutFormatted = dateFormatter.format(SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).parse(reservationData.dateDébut))
        val dateFinFormatted = dateFormatter.format(SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).parse(reservationData.dateFin))
        val datePaiementFormatted = dateFormatter.format(SimpleDateFormat("dd-MM-yyyy", Locale.CANADA_FRENCH).parse(reservationData.datePaiement))

        val json = """
            {
                "client": {
                    "id": ${clientData.id},
                    "courriel": "${clientData.courriel}",
                    "prenom": "${clientData.prénom}",
                    "nom": "${clientData.nom}",
                    "photo": "${clientData.photo}"
                },
                "chambreNumero": ${chambre.id},
                "dateDebut": "${dateDebutFormatted}",
                "dateFin": "${dateFinFormatted}",
                "prix_total": ${reservationData.prixTotal},
                "statut": "${reservationData.statut}",
                "methode_paiement": "${reservationData.methodePaiement}",
                "status_paiement": ${reservationData.statusPaiement},
                "date_paiement": "${datePaiementFormatted}"
            }
        """.trimIndent()

        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        println("MIGHT FAIL HERE---------------------------------------------$!@%*(!&@*#%(!@&%!@%!@%!@%!@%!@%")
        val request = Request.Builder()
            .url("$url_api/reservations")
            .post(body)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }

    override suspend fun obtenirReservationsParClient(clientChoisie: ClientData): List<ReservationData> {
        val request = Request.Builder()
            .url("$url_api/reservations?clientId=${clientChoisie.id}")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return client.newCall(request).execute().use { response ->
            if (response.code == 404) return emptyList()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            response.body?.string()?.let { json ->
                return JsonConversion.jsonAReservations(json)
            } ?: emptyList()
        }
    }

    override suspend fun obtenirReservationParId(id: Int): ReservationData? {
        val request = Request.Builder()
            .url("$url_api/reservations/$id")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return client.newCall(request).execute().use { response ->
            if (response.code == 404) return null
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            response.body?.string()?.let { json ->
                return JsonConversion.jsonAReservation(JsonReader(StringReader(json)))
            }
        }
    }

    override suspend fun obtenirReservationParChambre(chambre: ChambreData): List<ReservationData> {
        val request = Request.Builder()
            .url("$url_api/reservations?chambreNumero=${chambre.id}")
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        return client.newCall(request).execute().use { response ->
            if (response.code == 404) return emptyList()
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            response.body?.string()?.let { json ->
                return JsonConversion.jsonAReservations(json)
            } ?: emptyList()
        }
    }

    override fun suppressionReservation(reservation: ReservationData) {
        val request = Request.Builder()
            .url("$url_api/reservations/${reservation.id}")
            .delete()
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }

    override fun modifierReservation(reservationData: ReservationData) {
        val json = ""
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$url_api/reservations/${reservationData.id}")
            .put(body)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }

    override fun modifierClientName(newName: String) {
        val json = ""
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$url_api/clients/name")
            .put(body)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }

    override fun modifierClientSurname(newSurname: String) {
        val json = ""
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$url_api/clients/surname")
            .put(body)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }

    override fun modifierClientEmail(newEmail: String) {
        val json = ""
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$url_api/clients/email")
            .put(body)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }

    override fun modifierClientImage(newImage: String) {
        val json = ""
        val body: RequestBody = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val request = Request.Builder()
            .url("$url_api/clients/image")
            .put(body)
            .addHeader("Authorization", "Bearer $bearerToken")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }
}