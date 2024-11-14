package com.easycorp.easystayapp.Presentation.Presentateur

import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Vue.PreferenceVue
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Presentation.Vue.PreferencePresentateurInterface


class PreferencePresentateur(
    val vue: PreferencePresentateurInterface
) {
    fun afficherClient(clientId: Int) {
        val client = Modèle.obtenirClientParId(clientId)
        vue.afficherClient(
            prénom = client.prénom,
            nom = client.nom,
            email = client.email,
            photoResId = client.photo ?: R.drawable.profil_icon
        )
    }
}