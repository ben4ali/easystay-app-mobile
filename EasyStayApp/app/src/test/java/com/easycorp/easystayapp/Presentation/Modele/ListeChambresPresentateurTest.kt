package com.easycorp.easystayapp.Presentation.Modele

import android.content.Context
import android.widget.ListView
import com.easycorp.easystayapp.Domaine.Entite.ChambreData
import com.easycorp.easystayapp.Presentation.Presentateur.ListeChambres.ListeChambresPresentateur
import com.easycorp.easystayapp.Presentation.Vue.ChambresVue
import com.easycorp.easystayapp.Utilitaire.ChambreAdapter
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class ListeChambresPresentateurTest {

    private lateinit var context: Context
    private lateinit var listViewChambres: ListView
    private lateinit var vue: ChambresVue
    private lateinit var présentateur: ListeChambresPresentateur
    private lateinit var modèle: Modèle

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        listViewChambres = mock(ListView::class.java)
        vue = mock(ChambresVue::class.java)
        modèle =  mock(Modèle::class.java)

        `when`(vue.listViewChambres).thenReturn(listViewChambres)

        présentateur = ListeChambresPresentateur(vue, context)
    }

    @Test
    fun `Étant donné que des chambres sont disponibles dans le modèle, lorsque chargerChambres est appelé, on affiche la liste de toutes les chambres`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0),
            ChambreData(2, "Suite Junior", "Balcon privé", 3.5f, 5, listOf("TV", "Climatisation"), 50.0),
            ChambreData(3, "Chambre Standard", "Lit queen-size", 4.0f, 8, listOf("TV", "Climatisation", "Balcon"), 100.0)
        )

        modèle.obtenirChambres().toMutableList().apply {
            clear()
            addAll(chambres)
        }

        présentateur.chargerChambres()

        verify(listViewChambres).adapter = any(ChambreAdapter::class.java)
    }

    @Test
    fun `Étant donné un texte de recherche, lorsque filtrerChambres est appelé, on affiche les chambres correspondantes`() {
        val chambres = listOf(
            ChambreData(1, "Chambre Deluxe", "Vue sur la mer", 4.5f, 10, listOf("TV", "Climatisation", "Balcon"), 150.0),
            ChambreData(2, "Suite Junior", "Balcon privé", 3.5f, 5, listOf("TV", "Climatisation"), 50.0),
            ChambreData(3, "Chambre Standard", "Lit queen-size", 4.0f, 8, listOf("TV", "Climatisation", "Balcon"), 100.0)
        )

        modèle.obtenirChambres().toMutableList().apply {
            clear()
            addAll(chambres)
        }

        présentateur.filtrerChambres("Deluxe")

        verify(listViewChambres).adapter = any(ChambreAdapter::class.java)
    }
}
