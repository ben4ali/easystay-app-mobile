package com.easycorp.easystayapp.SourceDeDonnes

import com.easycorp.easystayapp.SourceDeDonnes.DAO.DAOInterface

interface FavorisDAOInterface : DAOInterface<Int> {
    override fun ajouter(roomId: Int)
    override fun retirer(roomId: Int)
    override fun obtenirTous(): List<Int>
    fun estFavoris(roomId: Int): Boolean
    override fun modifier(roomId: Int)
}