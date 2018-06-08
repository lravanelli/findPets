package br.com.lravanelli.findpets.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.lravanelli.findpets.R
import kotlinx.android.synthetic.main.fragment_cad.*
import android.content.Intent
import android.app.Activity
import android.os.Environment
import android.util.Base64
import java.io.File
import android.provider.MediaStore
import br.com.lravanelli.findpets.util.ImageUtils
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.widget.*
import br.com.lravanelli.findpets.MenuActivity
import br.com.lravanelli.findpets.controller.PetService
import br.com.lravanelli.findpets.database.UserDatabase
import br.com.lravanelli.findpets.model.Pet
import br.com.lravanelli.findpets.model.UserPers
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_menu.*
import kotlinx.android.synthetic.main.fragment_cad.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.FileOutputStream


class CadFragment : Fragment() {

    var file: File? = null
    var idUser: Int = 0
    var pet: Pet? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        val dao = UserDatabase.getDatabase(context.applicationContext)

        val user: UserPers? = GetAsyncTask(dao!!).execute().get()


        if(user != null){
            idUser = user.id
        }


    }


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater!!.inflate(R.layout.fragment_cad, container, false)

        val imageView: ImageView = view.findViewById(R.id.ivPetFoto)

        val btRegister: Button = view.findViewById(R.id.btResgister)

        imageView.setOnClickListener{loadPhoto()}

        btRegister.setOnClickListener { registerPet() }

        if(arguments != null) {
            pet = arguments!!.getParcelable<Pet>("pet")
            btRegister.setText(getString(R.string.update_pet) )

            view.etName.setText(pet?.nome)
            view.etBreed.setText(pet?.raca)
            view.etComments.setText(pet?.obs)
            view.etGenus.setText(pet?.especie)
            view.etPhone.setText(pet?.tel)
            view.etZip.setText(pet?.cep)
            when (pet?.sexo) {
                "F" -> view.rbFemale.isChecked = true
                "M" -> view.rbMale.isChecked = true
            }
            Picasso.with(context)
                    .load(pet?.path_foto)
                    .resize(100,100)
                    .into(view.ivPetFoto)

        }



//        if(savedInstanceState != null) {
//            val arq = savedInstanceState.getString("arq")
//            if(arq == "com") {
//                file = savedInstanceState.getSerializable("file") as File
//                val options = BitmapFactory.Options()
//                val bitmap = BitmapFactory.decodeFile(file!!.absolutePath, options)
//                imageView.setImageBitmap(bitmap)
//            }
//
//        }

        if (file != null) {
            val options = BitmapFactory.Options()
                val bitmap = BitmapFactory.decodeFile(file!!.absolutePath, options)
            imageView.setImageBitmap(bitmap)
        }


        return view
    }


    private fun loadPhoto() {

        //val uri = FileProvider.getUriForFile(context, context.applicationContext.packageName + ".provider", f)

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_PICK
        startActivityForResult(Intent.createChooser(intent, "Select File"), 123)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 123) {

                val f = getSdCardFile("pet.jpg")
                file = f;

                val bitmap: Bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), data!!.getData());

                val fos: FileOutputStream
                fos = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)


                showImage(file)
            }
        }

    }

    private fun registerPet() {
        if(validateFields()) {
            if (pet == null) {
                val bytes = file!!.readBytes()
                val imagemBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)

                var gender: String = ""
                when (rgGender.checkedRadioButtonId) {
                    0 -> gender = "F"
                    1 -> gender = "M"
                }


                val pet: Pet = Pet(0, etName.text.toString(), etGenus.text.toString(), etBreed.text.toString(), etPhone.text.toString(), etZip.text.toString(), etComments.text.toString(), idUser, imagemBase64, "", gender)


                PetService.service.createPet(pet).enqueue(object : Callback<Pet> {

                    override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                        val userResponse = response.body()?.copy()

                        if (userResponse?.id != 0) {

                            Toast.makeText(context, "pet cadastrado com sucesso", Toast.LENGTH_LONG).show()

                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.content_main, PetsFragment())
                            fragmentTransaction.commit()

                        }
                    }

                    override fun onFailure(call: Call<Pet>?, t: Throwable?) {
                        Log.d("ERRO", t?.message)

                    }
                })
            } else {
                var imagemBase64: String = ""
                if (file != null) {
                    val bytes = file!!.readBytes()
                    imagemBase64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                }

                var gender: String = ""
                when (rgGender.checkedRadioButtonId) {
                    0 -> gender = "F"
                    1 -> gender = "M"
                }

                val pet: Pet = Pet(pet!!.id, etName.text.toString(), etGenus.text.toString(), etBreed.text.toString(), etPhone.text.toString(), etZip.text.toString(), etComments.text.toString(), idUser, imagemBase64, "", gender)

                PetService.service.updatePet(pet).enqueue(object : Callback<Pet> {

                    override fun onResponse(call: Call<Pet>, response: Response<Pet>) {
                        val userResponse = response.body()?.copy()

                        if (userResponse?.id != -1) {

                            Toast.makeText(context, "pet atualizado com sucesso", Toast.LENGTH_LONG).show()

                            val fragmentTransaction = fragmentManager.beginTransaction()
                            fragmentTransaction.replace(R.id.content_main, PetsFragment())
                            fragmentTransaction.commit()

                        }
                    }

                    override fun onFailure(call: Call<Pet>?, t: Throwable?) {
                        Log.d("ERRO", pet.nome)

                    }
                })
            }
        }

    }

    private fun validateFields() :Boolean {
        var nError: Boolean = false
        if ((file == null) && (pet == null)) {
            Toast.makeText(context, getString(R.string.photo) + getString(R.string.required), Toast.LENGTH_LONG).show()
            nError = false
        } else if(etName.text.isBlank()) {
            Toast.makeText(context, getString(R.string.pet_name) + getString(R.string.required), Toast.LENGTH_LONG).show()
            nError = false
        } else if(etGenus.text.isBlank()) {
            Toast.makeText(context, getString(R.string.pet_genus) + getString(R.string.required), Toast.LENGTH_LONG).show()
            nError = false
        } else if(etBreed.text.isBlank()) {
            Toast.makeText(context, getString(R.string.pet_breed) + getString(R.string.required), Toast.LENGTH_LONG).show()
            nError = false
        } else if(etPhone.text.isBlank()) {
            Toast.makeText(context, getString(R.string.pet_phone) + getString(R.string.required), Toast.LENGTH_LONG).show()
            nError = false
        } else if(etZip.text.isBlank()) {
            Toast.makeText(context, getString(R.string.pet_zip_code) + getString(R.string.required), Toast.LENGTH_LONG).show()
            nError = false
        } else if(etComments.text.isBlank()) {
            Toast.makeText(context, getString(R.string.pet_comments) + getString(R.string.required), Toast.LENGTH_LONG).show()
            nError = false
        } else {
            nError = true
        }
        return nError
    }

    private fun showImage(file: File?) {
        if (file != null && file.exists()) {
            val w = ivPetFoto.width
            val h = ivPetFoto.height
            val bitmap = ImageUtils.resize(file, w, h)
            ivPetFoto.setImageBitmap(bitmap)
        }
    }

    fun getSdCardFile(fileName: String): File {
        val sdCardDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        if (!sdCardDir.exists()) {
            sdCardDir.mkdir()
        }
        val file = File(sdCardDir, fileName)
        return file
    }

    @SuppressLint("StaticFieldLeak")
    private inner class GetAsyncTask internal constructor(appDatabase: UserDatabase) : AsyncTask<Void, Void, UserPers>() {
        private val db: UserDatabase = appDatabase

        override fun doInBackground(vararg params: Void): UserPers {
            val user :UserPers = db.userDao().getUser()
            return user
        }
    }

//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        if(file != null) {
//            outState.putString("arq", "com")
//            outState.putSerializable("file", file)
//        } else {outState.putString("arq", "sem")}
//    }


}
