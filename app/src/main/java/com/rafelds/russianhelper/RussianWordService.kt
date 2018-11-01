package com.rafelds.russianhelper

import io.realm.Realm
import javax.inject.Inject

class RussianWordService @Inject constructor() {

    fun addWord(russianWord: RussianWord) {
        val realm = Realm.getDefaultInstance()
        realm.beginTransaction()
        val realmObject = realm.createObject(RussianWordDB::class.java)
        realmObject.word = russianWord.russianWord
        realmObject.description = russianWord.description
        realm.commitTransaction()
        realm.close()
    }

    fun getAllWords(): List<RussianWord> {
        val realm = Realm.getDefaultInstance()
        val russianWords = realm.where(RussianWordDB::class.java).findAll().map {
            RussianWord(it.word!!, it.description!!)
        }.toList()
        realm.close()
        return russianWords
    }

}