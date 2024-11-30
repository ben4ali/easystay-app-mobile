package com.easycorp.easystayapp.Domaine.Service

import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.SourceDeDonnes.SourceDeDonnées

class ServiceClient(private val sourceDeDonnées: SourceDeDonnées) {

    fun ajouterClient(client: ClientData) {
        sourceDeDonnées.ajouterClient(client)
    }

    fun obtenirClientParId(id: Int): ClientData {
        return sourceDeDonnées.obtenirClientParId(id)
    }

    fun modifierClient(client: ClientData) {
        sourceDeDonnées.modifierClient(client)
    }

    fun modifierClientName(newName: String) {
        sourceDeDonnées.modifierClientName(newName)
    }

    fun modifierClientSurname(newSurname: String) {
        sourceDeDonnées.modifierClientSurname(newSurname)
    }

    fun modifierClientEmail(newEmail: String) {
        sourceDeDonnées.modifierClientEmail(newEmail)
    }

    fun modifierClientImage(newImage: Int) {
        sourceDeDonnées.modifierClientImage(newImage)
    }
}