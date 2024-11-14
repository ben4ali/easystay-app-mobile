// ChambreAdapter.kt
package com.easycorp.easystayapp.Utilitaire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.easycorp.easystayapp.Presentation.Modele.ChambreData
import com.easycorp.easystayapp.R

class ChambreAdapter(
    context: Context,
    private val chambres: List<ChambreData>,
    private val onImageClick: (ChambreData) -> Unit
) : ArrayAdapter<ChambreData>(context, 0, chambres) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_chambre, parent, false)
        val chambre = getItem(position) ?: return view

        val imageRoom = view.findViewById<ImageButton>(R.id.image_Chambre) // Utilisation d'ImageButton
        val textRating = view.findViewById<TextView>(R.id.text_rating)
        val textRoomType = view.findViewById<TextView>(R.id.text_Chambre_type)
        val textRoomPrice = view.findViewById<TextView>(R.id.text_Chambre_price)
        val textRoomDescription = view.findViewById<TextView>(R.id.text_Chambre_description)

        textRoomType.text = chambre.typeChambre
        textRoomDescription.text = chambre.description
        textRating.text = "${chambre.note} (${chambre.nombreAvis} avis)"
        textRoomPrice.text = "${chambre.prixTotal()}$/nuit"

        imageRoom.setImageResource(R.drawable.chambre_exemple1)

        imageRoom.setOnClickListener {
            onImageClick(chambre)
        }

        return view
    }
}
