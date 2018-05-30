package br.com.lravanelli.findpets.controller

import android.util.Log
import android.widget.TextView
import br.com.lravanelli.findpets.R
import br.com.lravanelli.findpets.api.UserRest
//import br.com.lravanelli.findpets.domain.Response
import br.com.lravanelli.findpets.model.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

object UserService {
    private val BASE_URL = "http://www.gmdlogistica.com.br:8085/findapi/"
    public var service: UserRest

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(UserRest::class.java)
    }

    /*fun getUser(id: Int): User? {
        val call = service.getUser(id)
        val user = call.execute().body()
        return user
    }

    fun getUsers(): List<User>? {
        val call = service.getUsers()
        val user = call.execute().body()
        return user
    }

    fun createUser(user: User): Response? {
        val call = service.createUser(user)
        val response = call.execute().body()
        return response
    }

    fun delete(user: User): Response? {
        val call = service.delete(user.id)
        val response = call.execute().body()
        return response
    }*/



    /*fun getUser(id: Int) : User? {
        var user: User? = null

        service.getUser(2).enqueue(object : Callback<User> {

            override fun onResponse(call: Call<User>, response: Response<User>) {

                user = response.body()?.copy()
                Log.d("user", user?.email)


            }
            override fun onFailure(call: Call<User>?, t: Throwable?) {
                Log.d("ERRO", t?.message)
            }
        })
        return user
    }*/
}