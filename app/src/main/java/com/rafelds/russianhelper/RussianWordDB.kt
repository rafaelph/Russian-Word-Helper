package com.rafelds.russianhelper

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.Required

open class RussianWordDB : RealmObject() {

    @PrimaryKey
    var id: String? = null

    @Required
    var word: String? = null

    @Required
    var description: String? = ""

}