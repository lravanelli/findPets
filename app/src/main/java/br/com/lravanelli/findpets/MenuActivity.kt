package br.com.lravanelli.findpets

import android.support.v4.app.Fragment;
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import br.com.lravanelli.findpets.fragments.PetsFragment
import br.com.lravanelli.findpets.fragments.SobreFragment
import kotlinx.android.synthetic.main.activity_menu.*


class MenuActivity : AppCompatActivity() {


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_pets -> {
                changeFragment(PetsFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_about -> {
                changeFragment(SobreFragment())
                return@OnNavigationItemSelectedListener true
            }

        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        var fragment = savedInstanceState?.getString("fragment") ?: "nulo"


        if( fragment == "nulo" ) {
            changeFragment(PetsFragment())
        }

    }

    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main, fragment)
        fragmentTransaction.commit()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("fragment", "passou")
        super.onSaveInstanceState( outState )
    }

    override fun onResume() {
        super.onResume()
    }


}
