package br.com.lravanelli.findpets.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import br.com.lravanelli.findpets.R

object NotificationUtil {

    fun create(context: Context, id: Int, intent: Intent, title: String, text: String) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Intent para disparar o broadcast
        val p = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Cria a notification
        val builder = NotificationCompat.Builder(context,"1")
                .setContentIntent(p)
                .setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)

        // Dispara a notification
        val n = builder.build()
        manager.notify(id, n)
    }
}