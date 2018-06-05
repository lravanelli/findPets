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
import android.net.Uri
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.util.Log


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

        tvDetalheEspecie.text = pet.especie
        tvDetalheRaca.text = pet.raca

        val mapFragment = br.com.lravanelli.findpets.fragments.MapFragment()

        mapFragment.arguments = intent.extras

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.mapaFragment, mapFragment)
        fragmentTransaction.commit()
    }

    fun share() {
        val intentShare = Intent();
        intentShare.action = Intent.ACTION_SEND
        intentShare.putExtra(Intent.EXTRA_SUBJECT, "Ajude a encontrar o pet ${pet.nome}" )
        intentShare.putExtra(Intent.EXTRA_TEXT, "Este pet está perdido, ajude-nos a encontrar o caminha de casa ${pet.path_foto}")
        intentShare.type = "text/html"

        startActivity(Intent.createChooser(intentShare, "Compartilhar"));
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

                builder.setMessage("Necessária a permissao para fazer ligações")
                        .setTitle("Permissao Requerida")

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
