package br.edu.scl.ifsp.ads.contatospdm.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import br.edu.scl.ifsp.ads.contatospdm.R
import br.edu.scl.ifsp.ads.contatospdm.databinding.TileContactBinding
import br.edu.scl.ifsp.ads.contatospdm.model.Contact

class ContactAdapter(context: Context,private val contactList: MutableList<Contact>):

    ArrayAdapter<Contact>(context, R.layout.tile_contact, contactList){
        private lateinit var tcb: TileContactBinding
        override fun getView(position: Int, convertView: View?, parent: android.view.ViewGroup): View {
            val contact: Contact = contactList[position]
           var tileContactView =convertView
            if(tileContactView == null) {
                tcb = TileContactBinding.inflate(
                    context.getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater,
                    parent, false
                )
                tileContactView = tcb.root

                //criando um holder e guardando as referencias dos componentes
                val tcvh = TileContactViewHolder(
                    tileContactView.findViewById(R.id.nameTv),
                    tileContactView.findViewById(R.id.emailTv)

                )
                //armazenando viewHolder na celula
                tileContactView.tag = tcvh
            }
            //substituir os valores
            with(tileContactView.tag as TileContactViewHolder){
                nameTv.text = contact.name
                emailTv.text = contact.email
            }


            return tileContactView


        }
        private data class TileContactViewHolder(
            val nameTv: TextView,
            val emailTv: TextView,
        )


    }