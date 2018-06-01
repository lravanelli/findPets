package br.com.lravanelli.findpets.adapter

import br.com.lravanelli.findpets.model.Pet
import br.com.lravanelli.findpets.R
import android.content.Context
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.pet_item.view.*


class PetAdapter(val context: Context,
                 val pets: List<Pet>,
                 val listener: (Pet) -> Unit,
                 val listenerDelete: (Pet) -> Unit) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    override fun getItemCount(): Int {

        return pets.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PetViewHolder {

        val itemView = LayoutInflater.from(context).inflate(R.layout.pet_item, parent, false)
        return PetViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PetViewHolder?, position: Int) {

        val pet = pets[position]

        holder?.let {
            holder.bindView(pet, listener, listenerDelete)
        }
    }

    class  PetViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

        fun bindView(pet: Pet, listener: (Pet) -> Unit, listenerDelete: (Pet) -> Unit) = with(itemView) {

            tvNome.text = pet.nome
            tvEspecie.text = pet.especie
            tvRaca.text = pet.raca
            Picasso.with(context)
                    .load(pet.path_foto)
                    .resize(100,100)
                    .into(ivFoto)

            ivDelete.setOnClickListener{
                listenerDelete(pet)
            }

            setOnClickListener { listener(pet) }
        }
    }
}