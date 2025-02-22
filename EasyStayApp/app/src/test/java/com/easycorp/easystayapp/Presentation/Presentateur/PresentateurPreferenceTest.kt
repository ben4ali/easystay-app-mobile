package com.easycorp.easystayapp.Presentation.Presentateur

import android.content.Context
import androidx.fragment.app.Fragment
import com.easycorp.easystayapp.Domaine.Entite.ClientData
import com.easycorp.easystayapp.Presentation.Modele.Modèle
import com.easycorp.easystayapp.Presentation.Presentateur.Préférences.PreferencePresentateur
import com.easycorp.easystayapp.Presentation.Vue.PreferenceVue
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertNull
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.*

class PresentateurPreferenceTest {
    private lateinit var présentateur: PreferencePresentateur
    private lateinit var modèle: Modèle
    private lateinit var vue: PreferenceVue
    private lateinit var context: Context

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        modèle = mock(Modèle::class.java)
        vue = mock(PreferenceVue::class.java)
        présentateur = PreferencePresentateur(
            vue,
            context
        )
    }

    @Test
    fun `Étant donné un ID de client, lorsque afficherClient est appelé, alors les données du client correctes doivent être retournées`() {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", "")

        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)


        val clientAttendu = ClientData(1, "John", "Doe", "johndoe@gmail.com", "")

        présentateur.afficherClient(clientId)

        assertEquals(clientAttendu, modèle.obtenirClientParId(clientId))
    }

    @Test
    fun `Étant donnéee un ID de client, lorsque afficherClient est appelé, alors les données du client correctes doivent être retournées`() {
        val clientId = 1
        val client = ClientData(clientId, "John", "Doe", "johndoe@gmail.com", "")

        `when`(modèle.obtenirClientParId(clientId)).thenReturn(client)

        présentateur.afficherClient(clientId)


        verify(vue).afficherClient("John", "Doe", "johndoe@gmail.com")
    }
}
