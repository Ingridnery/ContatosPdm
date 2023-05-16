package br.edu.scl.ifsp.ads.contatospdm.controller

import androidx.room.Room
import br.edu.scl.ifsp.ads.contatospdm.model.Contact
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDao
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDaoRoom
import br.edu.scl.ifsp.ads.contatospdm.model.ContactDaoSqlite
import br.edu.scl.ifsp.ads.contatospdm.view.MainActivity

class ContactController(private val mainActivity: MainActivity) {
    //private val contactDaoImpl: ContactDao = ContactDaoSqlite(mainActivity)
    private val contactDaoImpl: ContactDao = Room.databaseBuilder(mainActivity, ContactDaoRoom::class.java, ContactDaoRoom.CONTACT_DATABASE_FILE).build().getContactDao()
    fun insertContact(contact: Contact){
        Thread{
            contactDaoImpl.createContact(contact)
        }.start()
    }
    fun getContact(id: Int) = contactDaoImpl.retrieveContact(id)
    fun getContacts(){
        Thread{
            mainActivity.updateContactList(contactDaoImpl.retrieveContacts())
        }.start()
    }
    fun editContact(contact: Contact) = contactDaoImpl.updateContact(contact)
    fun removeContact(contact: Contact) = contactDaoImpl.deleteContact(contact)

}