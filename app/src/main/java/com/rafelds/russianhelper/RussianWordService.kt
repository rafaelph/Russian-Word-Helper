package com.rafelds.russianhelper

import io.realm.Realm
import io.realm.kotlin.where
import java.util.*
import javax.inject.Inject

class RussianWordService @Inject constructor() {

    fun addWord(word: String, description: String) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val realmObject = realm.createObject(RussianWordDB::class.java, UUID.randomUUID().toString())
        realmObject.word = word
        realmObject.description = description
        realm.commitTransaction()
        realm.close()
    }

    fun getAllWords(): List<RussianWord> {
        val realm = Realm.getDefaultInstance()
        val russianWords = realm.where(RussianWordDB::class.java).findAll().map {
            RussianWord(it.id!!, it.word!!, it.description!!)
        }.toList()
        realm.close()
        return russianWords
    }

    fun deleteWord(id: String) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        realm.where<RussianWordDB>().equalTo("id", id).findAll().deleteAllFromRealm()
        realm.commitTransaction()
        realm.close()
    }

}