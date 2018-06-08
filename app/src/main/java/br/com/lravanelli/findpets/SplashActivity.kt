package br.com.lravanelli.findpets

import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import br.com.lravanelli.findpets.database.UserDatabase
import br.com.lravanelli.findpets.model.UserPers
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        carregar();
    }

    private fun carregar() {
        val anim = AnimationUtils.loadAnimation(this,
                R.anim.animacao_splash)
        anim.reset()

        ivSplash.clearAnimation()
        ivSplash.startAnimation(anim)

        Handler().postDelayed(object:Runnable {
            public override fun run() {

                val dao = UserDatabase.getDatabase(applicationContext)



                val user: UserPers? = GetAsyncTask(dao!!).execute().get()


                if(user == null){
                    val intent = Intent(this@SplashActivity,
                            LoginActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                    startActivity(intent)
                } else {
                    if(user.keep!!) {
                        val intent = Intent(this@SplashActivity,
                                MenuActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)

                    } else {
                        val intent = Intent(this@SplashActivity,
                                LoginActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                    }
                }

                this@SplashActivity.finish()
            }
        }, 2500)
    }

    private inner class GetAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, UserPers>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): UserPers {
            val user :UserPers = db.userDao().getUser()
            return user
        }
    }

}
