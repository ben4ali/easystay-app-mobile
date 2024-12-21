package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import android.widget.Toast
import java.util.Calendar
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Presentateur.ChambreDétail.ChambreDétailPresentateur
import com.easycorp.easystayapp.Presentation.Vue.ChambreDetailsVue
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*
import java.text.SimpleDateFormat
import java.util.Locale
import org.mockito.Mockito.mock

class ChambreDetailsPresentateurTest {

    private lateinit var context: Context
    private lateinit var vue: ChambreDetailsVue
    private lateinit var modèle: Modèle
    private lateinit var présentateur: ChambreDétailPresentateur
    private lateinit var mockToast: WormDotsIndicator

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        vue = mock(ChambreDetailsVue::class.java)
        modèle = mock(Modèle::class.java)
        mockToast = mock(WormDotsIndicator::class.java)
        présentateur = ChambreDétailPresentateur(vue, context, mockToast)
    }

    @Test
    fun `Étant donné que l'ID de la chambre est valide, lorsque afficherDetailsChambre est appelé, les détails de la chambre sont affichés`() {
        val chambreId = 1
        val chambre = ChambreData(chambreId, "Chambre Deluxe", 150.0, "Available", "Clean", 4, 10, listOf("TV", "Climatisation", "Balcon"), listOf())
        `when`(modèle.obtenirChambreParId(chambreId)).thenReturn(chambre)

        présentateur.afficherDetailsChambre(chambre.typeChambre, "Description", chambre.note.toFloat(), chambre.nombreAvis, chambre.prixParNuit)

        verify(vue).modifierDetailsChambre(chambre.typeChambre, "Description", chambre.note.toFloat(), chambre.nombreAvis, chambre.prixParNuit)
    }

    @Test
    fun `Lorsque nouvelleDateChoisie est appelé avec des dates valides, les dates sont formatées et affichées dans la vue`() {
        val dateDebut = Calendar.getInstance()
        val dateFin = Calendar.getInstance().apply { add(Calendar.DAY_OF_MONTH, 5) }

        présentateur.nouvelleDateChoisie(dateDebut, dateFin)

        val formatageDate = SimpleDateFormat("d MMMM yyyy", Locale.CANADA_FRENCH)
        val dateDebutTexteAttendue = formatageDate.format(dateDebut.time)
        val dateFinTexteAttendue = formatageDate.format(dateFin.time)

        verify(vue).modifierDateAfficher(dateDebutTexteAttendue, dateFinTexteAttendue)
    }

    @Test
    fun `Lorsque nouvelleDateChoisie est appelé avec des dates null, un Toast est affiché`() {
        présentateur.nouvelleDateChoisie(null, null)

        val inOrder = inOrder(mockToast)
    }

}