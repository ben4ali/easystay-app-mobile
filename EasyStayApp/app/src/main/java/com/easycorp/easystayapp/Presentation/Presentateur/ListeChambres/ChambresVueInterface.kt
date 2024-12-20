package com.easycorp.easystayapp.Presentation.Presentateur.ListeChambres

import com.easycorp.easystayapp.Domaine.Entite.ChambreData

interface ChambresVueInterface {
    fun afficherChambres(chambres: List<ChambreData>)
    fun afficherMessageErreur(message: String)
    fun afficherFiltreDialog()
    fun ouvrirDetailsChambre(actionId: Int)
}