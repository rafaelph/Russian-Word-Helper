package com.rafelds.russianhelper.data

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required
import java.util.*

open class RussianWordDB : RealmObject() {

    @PrimaryKey
    var id: String? = ""

    @Required
    var word: String = ""

    @Required
    var description: String = ""

    @Required
    var dateAdded: Date = Date()

}