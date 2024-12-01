package com.easycorp.easystayapp.SourceDeDonnes

import android.util.JsonReader
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Domaine.Entite.ReservationData
import com.easycorp.easystayapp.Utilitaire.JsonConversion
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.io.StringReader

class SourceDeDonneeAPI(val url_api: String, val bearerToken: String) {
    private val client = OkHttpClient()

    fun obtenirChambres(): List<ChambreData>? {
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

    fun obtenirChambreParId(id: Int): ChambreData? {
        val request = Request.Builder()
            .url("$url_api/chambres/$id")
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
}