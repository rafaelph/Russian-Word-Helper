package com.rafaelds.russianhelper

import io.realm.DynamicRealm
import io.realm.RealmMigration
import java.util.*

class SchemaMigration : RealmMigration {
    companion object {
        const val RUSSIAN_WORDS_DB = "RussianWordDB"
        const val RUSSIAN_WORDS_DB_DATE_ADDED_FIELD = "dateAdded"
    }

    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        if (oldVersion == 1L) {
            val userSchema = realm.schema.get(RUSSIAN_WORDS_DB)
            userSchema!!.addField(RUSSIAN_WORDS_DB_DATE_ADDED_FIELD, Date::class.java)
            userSchema.setRequired(RUSSIAN_WORDS_DB_DATE_ADDED_FIELD, true)
        }
    }
}