package br.com.lravanelli.findpets.service

import android.content.Intent
import android.util.Log
import br.com.lravanelli.findpets.MenuActivity
import br.com.lravanelli.findpets.R
import br.com.lravanelli.findpets.util.NotificationUtil
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    val TAG = "firebase"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "onMessageReceived")


        if (remoteMessage.notification != null) {
            val title = remoteMessage.notification?.title
            val msg = remoteMessage.notification?.body!!
            Log.d(TAG, "Message Notification Title: " + title)
            Log.d(TAG, "Message Notification Body: " + msg)

            showNotification(remoteMessage);
        }
    }


    private fun showNotification(remoteMessage: RemoteMessage) {
        // Intent para abrir a MainActivity
        val intent = Intent(this, MenuActivity::class.java)
        // Adiciona os parâmetros na intent


        // Title: O título é opcional...
        val title = remoteMessage.notification?.title ?: getString(R.string.app_name)

        // Mensagem
        val msg = remoteMessage.notification?.body!!

        // Cria uma notificação.
        NotificationUtil.create(this, 1, intent, title, msg)
    }
}
