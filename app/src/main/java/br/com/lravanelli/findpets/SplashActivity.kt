package br.com.lravanelli.findpets

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.ImageView

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

        val iv: ImageView = findViewById<ImageView>(R.id.ivSplash)
        if (iv != null)
        {
            iv.clearAnimation()
            iv.startAnimation(anim)
        }
        Handler().postDelayed(object:Runnable {
            public override fun run() {

                val intent = Intent(this@SplashActivity,
                        MenuActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                this@SplashActivity.finish()
            }
        }, 3500)
    }

}
