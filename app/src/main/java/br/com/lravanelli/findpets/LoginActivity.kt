package br.com.lravanelli.findpets

import android.annotation.SuppressLint
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.lravanelli.findpets.controller.UserService
import br.com.lravanelli.findpets.database.UserDatabase
import br.com.lravanelli.findpets.model.User
import br.com.lravanelli.findpets.model.UserPers
import br.com.lravanelli.findpets.util.Util
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.firebase.iid.FirebaseInstanceId



class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btSignup.setOnClickListener { signUp() }
        btSignin.setOnClickListener { signIn() }

    }

    fun signUp() {

        if (etEmail.text.isEmpty()) {
            Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show()
        } else if (etPassword.text.isEmpty()) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show()
        } else if (!Util.isNetworkAvailable(this))
        {
            Toast.makeText(this, R.string.conection_avaiable, Toast.LENGTH_SHORT).show()
        } else {
            val refreshedToken = FirebaseInstanceId.getInstance().token
            val user: User = User(0, etEmail.text.toString(), etPassword.text.toString(), refreshedToken ?: "")


            UserService.service.createUser(user).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {
                    val userResponse = response.body()?.copy()
                    if(userResponse != null ) {
                        if (userResponse?.id == -1) {
                            Toast.makeText(applicationContext, R.string.user_exist, Toast.LENGTH_LONG).show()
                        } else if (userResponse?.id != 0) {

                            val userP: UserPers = UserPers(userResponse!!.id, etEmail.text.toString(), cbRemember.isChecked, refreshedToken ?: "")

                            val dao = UserDatabase.getDatabase(applicationContext)

                            DeleteAsyncTask(dao!!).execute()

                            InsertAsyncTask(dao!!).execute(userP)

                            Toast.makeText(applicationContext, R.string.user_registred, Toast.LENGTH_LONG).show()

                            val intent = Intent(this@LoginActivity,
                                    MenuActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                            this@LoginActivity.finish()

                        }
                    } else {
                        Toast.makeText(applicationContext, R.string.message_error, Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Toast.makeText(applicationContext, R.string.message_error, Toast.LENGTH_LONG).show()

                }
            })

        }
    }

    fun signIn() {
        if (etEmail.text.isEmpty()) {
            Toast.makeText(this, R.string.enter_email, Toast.LENGTH_SHORT).show()
        } else if (etPassword.text.isEmpty()) {
            Toast.makeText(this, R.string.enter_password, Toast.LENGTH_SHORT).show()
        } else if (!Util.isNetworkAvailable(this))
        {
            Toast.makeText(this, R.string.conection_avaiable, Toast.LENGTH_SHORT).show()
        } else {

            val refreshedToken = FirebaseInstanceId.getInstance().token
            val user: User = User(0, etEmail.text.toString(), etPassword.text.toString(), refreshedToken ?: "")


            UserService.service.veriUser(user).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    Log.d("user", response.body()?.toString())
                    val userResponse = response.body()?.copy()
                    if(userResponse != null) {
                        if (userResponse?.id == -2) {
                            Toast.makeText(applicationContext, R.string.incorrect_email, Toast.LENGTH_LONG).show()
                        } else if (userResponse?.id == -1) {
                            Toast.makeText(applicationContext, R.string.incorrect_password, Toast.LENGTH_LONG).show()
                        } else {

                            val userP: UserPers = UserPers(userResponse!!.id, etEmail.text.toString(), cbRemember.isChecked, refreshedToken ?: "")

                            val dao = UserDatabase.getDatabase(applicationContext)

                            DeleteAsyncTask(dao!!).execute()

                            InsertAsyncTask(dao!!).execute(userP)

                            val intent = Intent(this@LoginActivity,
                                    MenuActivity::class.java)
                            intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                            startActivity(intent)
                            this@LoginActivity.finish()
                        }
                    } else {
                        Toast.makeText(applicationContext, R.string.message_error, Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Toast.makeText(applicationContext, R.string.login_error, Toast.LENGTH_LONG).show()

                }
            })

        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class InsertAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<UserPers, Void, String>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: UserPers): String {
            db.userDao().insertUser(params[0])
            return ""
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class DeleteAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, String>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): String {
            db.userDao().deleteUser()
            return ""
        }
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, UserPers>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): UserPers {
            val user :UserPers = db.userDao().getUser()
            return user
        }
    }

}
