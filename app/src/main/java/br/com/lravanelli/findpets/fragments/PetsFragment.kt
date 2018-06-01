package br.com.lravanelli.findpets.fragments

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.lravanelli.findpets.R
import br.com.lravanelli.findpets.adapter.PetAdapter
import br.com.lravanelli.findpets.model.Pet
import kotlinx.android.synthetic.main.fragment_pets.*
import android.support.v7.widget.RecyclerView
import android.util.Log
import br.com.lravanelli.findpets.PetActivity
import br.com.lravanelli.findpets.controller.PetService
import br.com.lravanelli.findpets.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PetsFragment : Fragment() {


    lateinit var list: List<Pet>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_pets, container, false)

        getPets()

        return view
    }


    fun loadList() {
        val recyclerView: RecyclerView = rvPets


        recyclerView.adapter = PetAdapter(context, list, {

            val intentDetalhe = Intent(context, PetActivity::class.java)

            intentDetalhe.putExtra("pet", it)

            startActivity(intentDetalhe)

        }, {
            Toast.makeText(context, "Delete ${it.nome}", Toast.LENGTH_LONG).show()
        })
        recyclerView.layoutManager = LinearLayoutManager(context)

    }

    fun getPets() {

//        return listOf(Pet(
//                1,
//                "nome",
//                "especie",
//                "raca",
//                "tel",
//                "cep",
//                "obs",
//                1,
//                //Date(),
//                "http://www.gmdlogistica.com.br:8085/findapi/img/8/persona.png")
//        )


        if (!Util.isNetworkAvailable(context))
        {
            Toast.makeText(context, R.string.conection_avaiable, Toast.LENGTH_SHORT).show()
        } else {
            PetService.service.getPets().enqueue(object : Callback<List<Pet>> {

                override fun onResponse(call: Call<List<Pet>>, response: Response<List<Pet>>) {
                    val petResponse = response.body()
                    if(petResponse != null) {
                        list = response.body()?.toList()!!
                        loadList()
                    }
                }
                override fun onFailure(call: Call<List<Pet>>?, t: Throwable?) {
                    Log.d("ERRO", t?.message)

                }
            })

        }
    }

}
