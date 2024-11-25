package com.easycorp.easystayapp.Utilitaire

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.R
import com.easycorp.easystayapp.SourceDeDonnes.FavorisDAOImpl

class ChambreAdapter(
    context: Context,
    private val chambres: List<ChambreData>,
    private val onImageClick: (ChambreData) -> Unit
) : ArrayAdapter<ChambreData>(context, 0, chambres) {

    private val favorisDAO = FavorisDAOImpl(context)
    private val animatedPositions = mutableSetOf<Int>()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_chambre, parent, false)
        val chambre = getItem(position) ?: return view

        val imageRoom = view.findViewById<ImageButton>(R.id.image_Chambre)
        val textRating = view.findViewById<TextView>(R.id.text_rating)
        val textRoomType = view.findViewById<TextView>(R.id.text_Chambre_type)
        val textRoomPrice = view.findViewById<TextView>(R.id.text_Chambre_price)
        val textRoomDescription = view.findViewById<TextView>(R.id.text_Chambre_description)
        val btnFavoris = view.findViewById<ImageView>(R.id.btnFavoris)

        if (favorisDAO.estFavoris(chambre.id)) {
            btnFavoris.setImageResource(R.drawable.bookmark_fill)
        } else {
            btnFavoris.setImageResource(R.drawable.bookmark)
        }

        textRoomType.text = chambre.typeChambre
        textRoomDescription.text = chambre.description
        textRating.text = "${chambre.note} (${chambre.nombreAvis} avis)"
        textRoomPrice.text = "$${chambre.prixParNuit} CAD nuit"

        imageRoom.setImageResource(R.drawable.chambre_exemple1)

        imageRoom.setOnClickListener {
            onImageClick(chambre)
        }

        btnFavoris.setOnClickListener {
            if (favorisDAO.estFavoris(chambre.id)) {
                favorisDAO.retirer(chambre.id)
                btnFavoris.setImageResource(R.drawable.bookmark)
            } else {
                favorisDAO.ajouter(chambre.id)
                btnFavoris.setImageResource(R.drawable.bookmark_fill)
            }
        }

        if (!animatedPositions.contains(position)) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
            animation.startOffset = (position * 100).toLong()
            view.startAnimation(animation)
            animatedPositions.add(position)
        }

        return view
    }
}