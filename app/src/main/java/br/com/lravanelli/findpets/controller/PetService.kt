package br.com.lravanelli.findpets.controller

import br.com.lravanelli.findpets.api.PetRest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object PetService {
    private val BASE_URL = "http://www.gmdlogistica.com.br:8085/findapi/"
    public var service: PetRest

    init {
        val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        service = retrofit.create(PetRest::class.java)
    }
}
