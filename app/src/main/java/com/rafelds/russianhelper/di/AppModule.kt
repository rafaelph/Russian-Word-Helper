package com.rafelds.russianhelper.di

import android.app.Application
import android.content.Context
import android.media.AudioManager
import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(private val application: Application) {

    @Provides
    fun providesApplication(): Context {
        return application
    }

    @Provides
    fun providesMediaPlayer(): AudioManager {
        return application.getSystemService(Application.AUDIO_SERVICE) as AudioManager
    }
}