package com.rafelds.russianhelper.di

import com.rafelds.russianhelper.MainActivity
import com.rafelds.russianhelper.details.WordDetailActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(wordDetailActivity: WordDetailActivity)
}