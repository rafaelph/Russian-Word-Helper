package com.rafelds.russianhelper.data

import io.reactivex.Completable
import io.reactivex.Single
import io.realm.Realm
import io.realm.Sort
import io.realm.kotlin.where
import java.util.*
import javax.inject.Inject

class RussianWordService @Inject constructor() {

    companion object {
        private const val FIELD_ID = "id"
    }

    fun addWord(russianWord: RussianWord): Single<RussianWord> {
        return Single.create { emitter ->
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            val realmObject = realm.createObject(RussianWordDB::class.java, russianWord.id)
            realmObject.word = russianWord.russianWord
            realmObject.description = russianWord.description
            realmObject.dateAdded = russianWord.dateAdded
            realm.commitTransaction()
            realm.close()
            emitter.onSuccess(RussianWord(russianWord.id, russianWord.russianWord, russianWord.description, russianWord.dateAdded))
        }
    }

    fun getAllWords(): Single<ArrayList<RussianWord>> {
        return Single.create { emitter ->
            val realm = Realm.getDefaultInstance()
            val russianWords =
                realm.where(RussianWordDB::class.java).sort("dateAdded", Sort.DESCENDING).findAll().map { russianWord ->
                    RussianWord(russianWord.id!!, russianWord.word, russianWord.description, russianWord.dateAdded)
                }.toCollection(arrayListOf())
            realm.close()
            emitter.onSuccess(russianWords)
        }
    }

    fun deleteWord(id: String): Completable {
        return Completable.create { source ->
            val realm = Realm.getDefaultInstance()
            realm.beginTransaction()
            realm.where<RussianWordDB>().equalTo(FIELD_ID, id).findAll().deleteAllFromRealm()
            realm.commitTransaction()
            realm.close()
            source.onComplete()
        }
    }

}