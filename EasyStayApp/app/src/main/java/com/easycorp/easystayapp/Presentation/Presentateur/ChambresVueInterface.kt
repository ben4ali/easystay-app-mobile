package com.easycorp.easystayapp.Presentation.Vue

import android.os.Bundle
import com.easycorp.easystayapp.Domaine.Entite.ChambreData

interface ChambresVueInterface {
    fun afficherChambres(chambres: List<ChambreData>)
    fun afficherMessageErreur(message: String)
    fun afficherFiltreDialog()
    fun ouvrirDetailsChambre(actionId: Int, bundle: Bundle)
}