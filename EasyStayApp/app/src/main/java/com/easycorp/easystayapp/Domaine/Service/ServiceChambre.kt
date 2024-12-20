package com.easycorp.easystayapp.Domaine.Service

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonnées
import java.io.IOException

class ServiceChambre(private val sourceDeDonnées: SourceDeDonnées) {

    fun obtenirChambres(): List<ChambreData>? {
        return sourceDeDonnées.obtenirChambres()
    }

    fun obtenirChambreParType(type: String): List<ChambreData>? {
        return sourceDeDonnées.obtenirChambreParType(type)
    }

    fun filtrerChambres(type: String?, prix: Int?): List<ChambreData>? {
        return try {
            sourceDeDonnées.filtrerChambres(type, prix)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    fun rechercherChambresParMotCle(keyword: String): List<ChambreData>? {
        return sourceDeDonnées.rechercherChambresParMotCle(keyword)
    }

    fun obtenirChambreParId(id: Int): ChambreData? {
        return sourceDeDonnées.obtenirChambreParId(id)
    }

    fun obtenirChambresDisponibles(): List<ChambreData>? {
        return sourceDeDonnées.obtenirChambresDisponibles()
    }
}