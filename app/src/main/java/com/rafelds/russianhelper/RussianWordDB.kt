package com.rafelds.russianhelper

import io.realm.RealmObject
import io.realm.annotations.Required

open class RussianWordDB : RealmObject() {

    @Required
    var word: String? = null

    @Required
    var description: String? = ""

}