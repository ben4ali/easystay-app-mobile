package com.easycorp.easystayapp.Domaine.Service

import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonnées

class ServiceChambre(private val sourceDeDonnées: SourceDeDonnées) {

    fun obtenirChambres(): List<ChambreData>? {
        return sourceDeDonnées.obtenirChambres()
    }

    fun obtenirChambreParType(type: String): List<ChambreData>? {
        return sourceDeDonnées.obtenirChambreParType(type)
    }

    fun obtenirChambreParId(id: Int): ChambreData? {
        return sourceDeDonnées.obtenirChambreParId(id)
    }

    fun obtenirChambresDisponibles(): List<ChambreData>? {
        return sourceDeDonnées.obtenirChambresDisponibles()
    }
}