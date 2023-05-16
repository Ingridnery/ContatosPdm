package br.edu.scl.ifsp.ads.contatospdm.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import br.edu.scl.ifsp.ads.contatospdm.R
import br.edu.scl.ifsp.ads.contatospdm.databinding.ActivityContactBinding
import br.edu.scl.ifsp.ads.contatospdm.model.Contact
import kotlin.random.Random

class ContactActivity : BaseActivity() {
    private val acb: ActivityContactBinding by lazy {
        ActivityContactBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(acb.root)
        supportActionBar?.subtitle = (getString(R.string.contact_info))

        val receivedContact = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            intent.getParcelableExtra("Contact", Contact::class.java)
        }
        else{
            intent.getParcelableExtra(EXTRA_CONTACT)
        }
        receivedContact?.let{ _receivedContact->
            with(acb){
               with(_receivedContact){
                   nameEt.setText(name)
                   addressEt.setText(address)
                   phoneEt.setText(phone)
                   emailEt.setText(email)
               }
            }
        }
        val viewContact = intent.getBooleanExtra(EXTRA_VIEW_CONTACT, false)
        with(acb){
            nameEt.isEnabled = !viewContact
            addressEt.isEnabled = !viewContact
            phoneEt.isEnabled = !viewContact
            emailEt.isEnabled = !viewContact
            saveBt.visibility= if(viewContact) View.GONE else View.VISIBLE
        }


        acb.saveBt.setOnClickListener(){
            val contact: Contact = Contact(receivedContact?.id, acb.nameEt.text.toString(),
                acb.addressEt.text.toString(), acb.phoneEt.text.toString(),
                acb.emailEt.text.toString())

            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_CONTACT, contact)
            setResult(RESULT_OK, resultIntent)
            finish()
        }

    }

    private fun generateId(): Int{
        return Random(System.currentTimeMillis()).nextInt(1000, 9999)
    }
}