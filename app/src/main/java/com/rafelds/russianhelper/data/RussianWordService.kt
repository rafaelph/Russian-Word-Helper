package com.rafelds.russianhelper.data

import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm
import io.realm.kotlin.where
import java.util.*
import javax.inject.Inject

class RussianWordService @Inject constructor() {

    companion object {
        private const val FIELD_ID = "id"
    }

    fun addWord(word: String, description: String): Single<RussianWord> {
        return Single.create {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val id = UUID.randomUUID().toString()
            val realmObject = realm.createObject(RussianWordDB::class.java, id)
            realmObject.word = word
            realmObject.description = description
            realm.commitTransaction()
            realm.close()
            it.onSuccess(RussianWord(id, word, description))
        }
    }

    fun getAllWords(): Single<ArrayList<RussianWord>> {
        return Single.create { emitter ->
            val realm = Realm.getDefaultInstance()
            val russianWords = realm.where(RussianWordDB::class.java).findAll().map {
                RussianWord(it.id!!, it.word!!, it.description!!)
            }.toCollection(arrayListOf())
            realm.close()
            emitter.onSuccess(russianWords)
        }
    }

    fun deleteWord(id: String): Completable {
        return Completable.create {
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.where<RussianWordDB>().equalTo(FIELD_ID, id).findAll().deleteAllFromRealm()
            realm.commitTransaction()
            realm.close()
            it.onComplete()
        }
    }

}