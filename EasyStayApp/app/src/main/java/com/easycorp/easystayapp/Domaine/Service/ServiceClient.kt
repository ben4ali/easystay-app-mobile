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

    fun modifierClientPrenom(clientId: Int,newPrenom: String) {
        sourceDeDonnées.modifierClientPrenom(clientId,newPrenom)
    }

    fun modifierClientCourriel(clientId: Int, newCourriel: String) {
        sourceDeDonnées.modifierClientCourriel(clientId,newCourriel)
    }

    fun modifierClientNom(clientId: Int, newNom: String) {
        sourceDeDonnées.modifierClientNom(clientId,newNom)
    }

    fun modifierClientImage(newImage: String) {
        sourceDeDonnées.modifierClientImage(newImage)
    }
}