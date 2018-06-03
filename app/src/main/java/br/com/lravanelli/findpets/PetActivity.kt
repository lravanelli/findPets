package br.com.lravanelli.findpets

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import br.com.lravanelli.findpets.model.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.content_pet.*

class PetActivity : AppCompatActivity() {

    lateinit var pet: Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)
        setSupportActionBar(toolbar)
        fab.setOnClickListener { view ->
            val intentMaps = Intent(this, MapsActivity::class.java)

            intentMaps.putExtra("pet", pet)

            startActivity(intentMaps)
        }


        pet = intent.getParcelableExtra<Pet>("pet")

        toolbar_layout.title = pet.nome

        Picasso.with(this)
                .load(pet.path_foto)
                .resize(100,100)
                .into(ivBanner)

        tvDetalheNome.text = pet.nome
        tvDetalheEspecie.text = pet.especie
        tvDetalheRaca.text = pet.raca

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mapaFragment, br.com.lravanelli.findpets.fragments.MapFragment())
        fragmentTransaction.commit()
    }



}
