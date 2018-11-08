package com.rafaelds.russianhelper.di

import com.rafaelds.russianhelper.home.MainActivity
import com.rafaelds.russianhelper.details.WordDetailActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(wordDetailActivity: WordDetailActivity)
}