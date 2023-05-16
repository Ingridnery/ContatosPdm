package br.edu.scl.ifsp.ads.contatospdm.model

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.util.Log
import br.edu.scl.ifsp.ads.contatospdm.R

class ContactDaoSqlite(context: Context): ContactDao {

    companion object Constants{
        private const val CONTACT_DATABASE_FILE="contacts"
        private const val CONTACT_TABLE_NAME="contact"
        private const val CONTACT_ID_COLUMN="id"
        private const val CONTACT_NAME_COLUMN="name"
        private const val CONTACT_ADDRESS_COLUMN="address"
        private const val CONTACT_PHONE_COLUMN="phone"
        private const val CONTACT_EMAIL_COLUMN="email"

        private const val CREATE_CONTACT_TABLE_STATEMENT=
            """
            CREATE TABLE IF NOT EXISTS $CONTACT_TABLE_NAME (
                $CONTACT_ID_COLUMN INTEGER PRIMARY KEY AUTOINCREMENT,
                $CONTACT_NAME_COLUMN TEXT NOT NULL,
                $CONTACT_ADDRESS_COLUMN TEXT NOT NULL,
                $CONTACT_PHONE_COLUMN TEXT NOT NULL,
                $CONTACT_EMAIL_COLUMN TEXT NOT NULL
            );
            """
    }

    private val contactSQLiteDatabase: SQLiteDatabase
    init{
        contactSQLiteDatabase = context.openOrCreateDatabase(CONTACT_DATABASE_FILE, Context.MODE_PRIVATE, null)
        try{
            contactSQLiteDatabase.execSQL(CREATE_CONTACT_TABLE_STATEMENT)
        }catch (se: SQLiteException) {
            Log.e(context.getString(R.string.app_name), se.toString())
        }
    }

    override fun createContact(contact: Contact){
        contactSQLiteDatabase.insert(CONTACT_TABLE_NAME, null, contact.toContentValues()).toInt()
    }


    override fun retrieveContact(id: Int): Contact? {
        val cursor = contactSQLiteDatabase.rawQuery("SELECT * FROM $CONTACT_TABLE_NAME WHERE $CONTACT_ID_COLUMN = ?", arrayOf(id.toString()))
        val contact = if(cursor.moveToFirst()){
          //preencher contato com dados da posição atual do cursor
            cursor.rowToContact()
        }else{
            null
        }
        cursor.close()
        return contact
    }

    override fun retrieveContacts(): MutableList<Contact> {
        val contactList = mutableListOf<Contact>()

        val cursor = contactSQLiteDatabase.rawQuery("SELECT * FROM $CONTACT_TABLE_NAME ORDER BY $CONTACT_NAME_COLUMN", null)
        while(cursor.moveToNext()){
            contactList.add(cursor.rowToContact())
        }
        cursor.close()
        return contactList
    }

    override fun updateContact(contact: Contact) = contactSQLiteDatabase.update(CONTACT_TABLE_NAME, contact.toContentValues(), "$CONTACT_ID_COLUMN = ?", arrayOf(contact.id.toString()))

    override fun deleteContact(contact: Contact) {
        contactSQLiteDatabase.delete(CONTACT_TABLE_NAME, "$CONTACT_ID_COLUMN = ?", arrayOf(contact.id.toString()))
    }

    private fun Contact.toContentValues()= with(ContentValues()){
        put(CONTACT_NAME_COLUMN, name)
        put(CONTACT_ADDRESS_COLUMN, address)
        put(CONTACT_PHONE_COLUMN, phone)
        put(CONTACT_EMAIL_COLUMN, email)
        this

    }
    private fun Cursor.rowToContact() = Contact (
        getInt(getColumnIndexOrThrow(CONTACT_ID_COLUMN)),
        getString(getColumnIndexOrThrow(CONTACT_NAME_COLUMN)),
        getString(getColumnIndexOrThrow(CONTACT_ADDRESS_COLUMN)),
        getString(getColumnIndexOrThrow(CONTACT_PHONE_COLUMN)),
        getString(getColumnIndexOrThrow(CONTACT_EMAIL_COLUMN))
    )
}