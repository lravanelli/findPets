package br.com.lravanelli.findpets.fragments

import android.content.Intent
import android.os.AsyncTask
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
import br.com.lravanelli.findpets.database.UserDatabase
import br.com.lravanelli.findpets.model.UserPers
import br.com.lravanelli.findpets.util.Util
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PetsFragment : Fragment() {


    lateinit var list: List<Pet>
    var idUser: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = UserDatabase.getDatabase(context.applicationContext)

        val user: UserPers? = GetAsyncTask(dao!!).execute().get()


        if(user != null){
            idUser = user.id
        }

    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_pets, container, false)

        getPets()

        return view
    }


    fun loadList() {
        rvPets.Recycler()
        val recyclerView: RecyclerView = rvPets


        recyclerView.adapter = PetAdapter(context, list, {



            val intentDetalhe = Intent(context, PetActivity::class.java)

            intentDetalhe.putExtra("pet", it)

            startActivity(intentDetalhe)

        }, {
            deletePet(it)
        }, {
            updatePet(it)



        })
        recyclerView.layoutManager = LinearLayoutManager(context)

    }

    fun getPets() {


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

    private inner class GetAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, UserPers>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): UserPers {
            val user :UserPers = db.userDao().getUser()
            return user
        }
    }

    fun deletePet(petDel: Pet){
        if (!Util.isNetworkAvailable(context)) {
            Toast.makeText(context, R.string.conection_avaiable, Toast.LENGTH_SHORT).show()
        } else if(petDel.id_user != idUser){
            Toast.makeText(context, getString(R.string.not_owner_pet), Toast.LENGTH_SHORT).show()
        } else {
            PetService.service.deletePet(petDel.id).enqueue(object : Callback<Pet> {

                override fun onResponse(call: Call<Pet>, response: Response<Pet>) {

                    Log.d("user", response.body()?.toString())
                    val petResponse = response.body()?.copy()
                    if (petResponse != null) {
                        if (petResponse?.id == -1) {
                            Toast.makeText(context, getString(R.string.message_error), Toast.LENGTH_LONG).show()
                        } else {
                            Toast.makeText(context, getString(R.string.pet_deleted), Toast.LENGTH_LONG).show()
                            getPets()

                        }
                    } else {
                        Toast.makeText(context, getString(R.string.message_error), Toast.LENGTH_LONG).show()
                    }
                }
                override fun onFailure(call: Call<Pet>?, t: Throwable?) {
                    Toast.makeText(context, R.string.login_error, Toast.LENGTH_LONG).show()

                }
            })
        }
    }

    fun updatePet(petUp: Pet){
        if (petUp.id_user != idUser){
            Toast.makeText(context, getString(R.string.not_owner_pet), Toast.LENGTH_SHORT).show()
        } else {

            val cadFragment = CadFragment()
            val bundle: Bundle = Bundle()
            bundle.putParcelable("pet", petUp)

            cadFragment.arguments = bundle

            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.content_main, cadFragment)
            fragmentTransaction.commit()

        }

    }

}
