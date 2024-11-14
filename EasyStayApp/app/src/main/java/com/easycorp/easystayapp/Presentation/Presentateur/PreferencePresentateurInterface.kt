package com.easycorp.easystayapp.Presentation.Vue

import com.easycorp.easystayapp.Domaine.Entite.ClientData

interface PreferencePresentateurInterface {
    fun afficherClient(prénom: String, nom: String, email: String, photoResId: Int)
}