package br.com.lravanelli.findpets

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import br.com.lravanelli.findpets.controller.UserService
import br.com.lravanelli.findpets.model.User
import kotlinx.android.synthetic.main.activity_menu.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        button.setOnClickListener { chamar() }

    }

    fun chamar() {
        var user: User? = null

        UserService.service.getUser(2).enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                user = response.body()?.copy()
                Log.d("user", user?.email)
                tvNome.text = user?.email
            }
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.d("ERRO", t?.message)
                tvNome.text = t?.message
            }
        })


    }


}
