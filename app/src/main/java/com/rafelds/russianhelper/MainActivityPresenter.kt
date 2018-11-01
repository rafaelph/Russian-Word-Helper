package com.rafelds.russianhelper

import io.realm.Realm
import io.realm.kotlin.where

class MainActivityPresenter(val view: MainActivityView) {

    fun onCreate() {
        val realm = Realm.getDefaultInstance()
        try {
            view.updateWordList(fetchAllWords(realm))
        } catch (e: Exception) {

        } finally {
            realm.close()
        }
    }

    fun onAddButtonClick() {
        view.showAddWordDialog()
    }

    fun onSaveButtonClick(russianWord: RussianWord) {
        val realm = Realm.getDefaultInstance()
        try {
            realm.beginTransaction()
            val realmObject = realm.createObject(RussianWordDB::class.java)
            realmObject.word = russianWord.russianWord
            realmObject.description = russianWord.description
            realm.commitTransaction()

            view.updateWordList(fetchAllWords(realm))
        } catch (e: Exception) {
            println(e)
        } finally {
            realm.close()
        }
        view.showSuccessSnackbar()
    }

    private fun fetchAllWords(realm: Realm): List<RussianWord> {
        return realm.where<RussianWordDB>().findAll().map {
            RussianWord(it.word!!, it.description!!)
        }.toList()
    }
}