package com.easycorp.easystayapp.Domaine.Service

import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOInterface

class ServiceFavoris(private val favorisDAO: FavorisDAOInterface) {

    fun ajouter(roomId: Int) {
        favorisDAO.ajouter(roomId)
    }

    fun retirer(roomId: Int) {
        favorisDAO.retirer(roomId)
    }

    fun estFavoris(roomId: Int): Boolean {
        return favorisDAO.estFavoris(roomId)
    }

    fun obtenirTous(): List<Int> {
        return favorisDAO.obtenirTous()
    }
}