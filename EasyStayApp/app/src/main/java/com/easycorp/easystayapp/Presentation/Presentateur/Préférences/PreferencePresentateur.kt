package com.easycorp.easystayapp.Presentation.Presentateur.Préférences

import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.R


class PreferencePresentateur(
    val vue: PreferencePresentateurInterface
) {
    var modèle = Modèle.getInstance()
    fun afficherClient(clientId: Int) {
        val client = modèle.obtenirClientParId(clientId)
        vue.afficherClient(
            prénom = client.prénom,
            nom = client.nom,
            email = client.email,
            photoResId = client.photo ?: R.drawable.profil_icon
        )
    }
}