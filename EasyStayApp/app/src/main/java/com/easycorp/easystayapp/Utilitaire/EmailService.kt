package com.easycorp.easystayapp.Utilitaire

import android.util.Log
import java.util.Properties
import javax.mail.Authenticator
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService {

    fun envoyerEmail(destinataire: String, sujet: String, contenu: String, callback: (Boolean) -> Unit) {
        Thread {
            try {
                val props = Properties().apply {
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.ssl.trust", "smtp.gmail.com")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication("gmail@gmail.com", "motDePasse_d'application")
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress("gmail@gmail.com"))
                    setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(destinataire))
                    subject = sujet
                    setText(contenu)
                }

                Transport.send(message)
                callback(true)
            } catch (e: Exception) {
                callback(false)
            }
        }.start()
    }
}
