package com.rafelds.russianhelper

import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration

class RussianHelperApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        val config = RealmConfiguration.Builder()
            .name("russian_helper.realm")
            .schemaVersion(1L)
            .build()
        Realm.setDefaultConfiguration(config)
    }
}