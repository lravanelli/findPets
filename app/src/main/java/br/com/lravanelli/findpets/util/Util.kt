package br.com.lravanelli.findpets.util

import android.content.Context
import android.net.ConnectivityManager


object Util {
    fun isNetworkAvailable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cm.activeNetworkInfo
        return (netInfo != null && netInfo.isConnected)
    }
}