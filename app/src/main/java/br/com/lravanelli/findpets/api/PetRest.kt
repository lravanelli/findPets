package br.com.lravanelli.findpets.api

import br.com.lravanelli.findpets.model.Pet
import retrofit2.Call
import retrofit2.http.*

interface PetRest {

    @GET("pets/")
    fun getPets() : Call<List<Pet>>

    @POST("pets/")
    fun createPet(@Body pet: Pet) : Call<Pet>

    @PUT("pets/")
    fun updatePet(@Body pet: Pet) : Call<Pet>

    @DELETE("pets/{id}")
    fun deletePet(@Path("id") id: Int): Call<Pet>
}