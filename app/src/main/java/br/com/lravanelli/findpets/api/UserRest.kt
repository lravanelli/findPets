package br.com.lravanelli.findpets.api

/**
 * Created by fernando.ravanelli on 17/05/2018.
 */

//import br.com.lravanelli.findpets.domain.Response
import br.com.lravanelli.findpets.model.User
import retrofit2.Call
import retrofit2.http.*


interface UserRest {

    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    /*@GET("user/")
    fun getUsers(): Call<List<User>>

    @POST("user/.")
    fun createUser(@Body user: User): Call<Response>

    @DELETE("user/{id}")
    fun delete(@Path("id") id: Int): Call<Response>*/


}