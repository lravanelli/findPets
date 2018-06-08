package br.com.lravanelli.findpets

import android.Manifest
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.lravanelli.findpets.model.Pet
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_pet.*
import kotlinx.android.synthetic.main.content_pet.*
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log
import br.com.lravanelli.findpets.util.Util


class PetActivity : AppCompatActivity() {

    lateinit var pet: Pet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet)
        setSupportActionBar(toolbar)
        btPhone.setOnClickListener { view ->
            call()
        }

        btShare.setOnClickListener { view ->
            share()

        }


        pet = intent.getParcelableExtra<Pet>("pet")

        toolbar_layout.title = pet.nome

        Picasso.with(this)
                .load(pet.path_foto)
                .resize(100,100)
                .into(ivBanner)

        when (pet.sexo){
            "F" -> tvDetailGender.text = "${getString(R.string.pet_gender)}: ${getString(R.string.pet_female)}"
            "M" -> tvDetailGender.text = "${getString(R.string.pet_gender)}: ${getString(R.string.pet_male)}"
        }
        tvDetailGenus.text =  "${getString(R.string.pet_genus)}: ${pet.especie}"
        tvDetailBreed.text = "${getString(R.string.pet_breed)}: ${pet.raca}"
        tvDetailZipCode.text = "${getString(R.string.pet_zip_code)}: ${pet.cep}"
        tvDetailPhone.text = "${getString(R.string.pet_phone)}: ${pet.tel}"
        etDetailComments.setText(pet.obs)

        val mapFragment = br.com.lravanelli.findpets.fragments.MapFragment()

        mapFragment.arguments = intent.extras

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mapaFragment, mapFragment)
        fragmentTransaction.commit()

        if(Util.isNetworkAvailable(this)) {
            val geoCoder = Geocoder(this)
            var address: List<Address>?

            address = geoCoder.getFromLocationName(pet.cep, 1)

            if (address.isNotEmpty()) {
                val location = address[0]
                if (address.size == 0) {
                    tvLastLocation.text = getString(R.string.zip_not_found)
                }
            } else {
                tvLastLocation.text = getString(R.string.zip_not_found)
            }
        } else {
            tvLastLocation.text = getString(R.string.conection_avaiable)
        }


    }

    fun share() {
        val intentShare = Intent();
        intentShare.action = Intent.ACTION_SEND
        intentShare.putExtra(Intent.EXTRA_SUBJECT, "${getString(R.string.pet_help_find)} ${pet.nome}" )
        intentShare.putExtra(Intent.EXTRA_TEXT, "${getString(R.string.pet_message)} ${pet.path_foto}")
        intentShare.type = "text/html"

        startActivity(Intent.createChooser(intentShare, getString(R.string.share)));
    }

    fun call() {
        try {
            if(checkPermission()) {
                //ACTION_DIAL
                //ACTION_CALL
                val uri = Uri.parse("tel:" + pet.tel)

                val intentCall = Intent(Intent.ACTION_CALL, uri);

                startActivity(intentCall);
            }
        } catch(act: ActivityNotFoundException) {
            Log.e("Exemplo de chamada", "falha", act);
        }


    }

    private fun checkPermission() : Boolean {
        var ret: Boolean = false
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CALL_PHONE)) {

                val builder = AlertDialog.Builder(this)

                builder.setMessage(getString(R.string.message_permission))
                        .setTitle(getString(R.string.permission_title))

                builder.setPositiveButton("OK") {
                    dialog, id ->
                    requestPermission()
                }

                val dialog = builder.create()
                dialog.show()

            } else {
                requestPermission()
            }
        } else {
            ret = true
        }
        return ret
    }

    protected fun requestPermission() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CALL_PHONE), 0)
    }



}
