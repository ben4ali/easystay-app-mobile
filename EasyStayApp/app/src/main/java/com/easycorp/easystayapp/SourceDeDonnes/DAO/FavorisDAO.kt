package com.easycorp.easystayapp.SourceDeDonnes

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

class FavorisDAO(context: Context) : FavorisDAOInterface {

    private val dbHelper = DatabaseHelper(context)

    override fun ajouterFavoris(roomId: Int) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_ROOM_ID, roomId)
        }
        db.insert(DatabaseHelper.TABLE_FAVORITES, null, values)
    }

    override fun retirerFavoris(roomId: Int) {
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_FAVORITES, "${DatabaseHelper.COLUMN_ROOM_ID} = ?", arrayOf(roomId.toString()))
    }

    override fun estFavoris(roomId: Int): Boolean {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_FAVORITES,
            arrayOf(DatabaseHelper.COLUMN_ID),
            "${DatabaseHelper.COLUMN_ROOM_ID} = ?",
            arrayOf(roomId.toString()),
            null, null, null
        )
        val isFavorite = cursor.count > 0
        cursor.close()
        return isFavorite
    }

    override fun obtenirTousLesFavoris(): List<Int> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DatabaseHelper.TABLE_FAVORITES, arrayOf(DatabaseHelper.COLUMN_ROOM_ID), null, null, null, null, null)
        val favorites = mutableListOf<Int>()
        while (cursor.moveToNext()) {
            favorites.add(cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ROOM_ID)))
        }
        cursor.close()
        return favorites
    }
}