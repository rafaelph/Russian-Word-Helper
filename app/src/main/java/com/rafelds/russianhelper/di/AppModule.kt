package com.rafelds.russianhelper.di

import dagger.Module
import dagger.Provides
import io.realm.Realm
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideRealmInstance(): Realm = Realm.getDefaultInstance()

}