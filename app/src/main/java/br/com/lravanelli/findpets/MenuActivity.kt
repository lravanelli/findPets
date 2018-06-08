package br.com.lravanelli.findpets

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.support.v4.app.Fragment;
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import br.com.lravanelli.findpets.database.UserDatabase
import br.com.lravanelli.findpets.fragments.CadFragment
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
            R.id.navigation_register -> {
                changeFragment(CadFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_about -> {
                changeFragment(SobreFragment())
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_exit -> {
                exit()
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
            navigation.menu.getItem(0).setChecked(true)

        }

    }


    private fun changeFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.content_main, fragment)
        fragmentTransaction.commit()
    }

    private fun exit() {
        val dao = UserDatabase.getDatabase(applicationContext)

        DeleteAsyncTask(dao!!).execute()

        finish();
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("fragment", "passou")
        super.onSaveInstanceState( outState )
    }

    override fun onResume() {
        super.onResume()
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DeleteAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, String>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): String {
            db.userDao().deleteUser()
            return ""
        }
    }




}
