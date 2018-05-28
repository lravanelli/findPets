package br.com.lravanelli.findpets.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


object Util {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return if (netInfo != null && netInfo.isConnected)
            true
        else
            false
    }
}