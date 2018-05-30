package br.com.lravanelli.findpets

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import br.com.lravanelli.findpets.controller.UserService
import br.com.lravanelli.findpets.model.User
import br.com.lravanelli.findpets.util.Util
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            val user: User = User(0, etEmail.text.toString(), etPassword.text.toString())


            UserService.service.createUser(user).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    Log.d("user", response.body()?.toString())
                }
                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Log.d("ERRO", t?.message)

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
            val user: User = User(0, etEmail.text.toString(), etPassword.text.toString())


            UserService.service.veriUser(user).enqueue(object : Callback<User> {

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    Log.d("user", response.body()?.toString())
                    val userResponse = response.body()?.copy()
                    if (userResponse?.id == -2){
                        Toast.makeText(applicationContext, R.string.incorrect_email, Toast.LENGTH_LONG).show()
                    } else if (userResponse?.id == -1){
                        Toast.makeText(applicationContext, R.string.incorrect_password, Toast.LENGTH_LONG).show()
                    } else {
                        val intent = Intent(this@LoginActivity,
                                MenuActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                        startActivity(intent)
                        this@LoginActivity.finish()
                    }
                }
                override fun onFailure(call: Call<User>?, t: Throwable?) {
                    Toast.makeText(applicationContext, R.string.login_error, Toast.LENGTH_LONG).show()

                }
            })

        }
    }



}
