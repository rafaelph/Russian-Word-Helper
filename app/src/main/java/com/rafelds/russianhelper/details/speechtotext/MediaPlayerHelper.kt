package com.rafelds.russianhelper.details.speechtotext

import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaPlayer
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import java.io.FileDescriptor
import javax.inject.Inject

class MediaPlayerHelper @Inject constructor(audioManager: AudioManager) {

    private var mediaPlayer: MediaPlayer? = null

    fun play(file: FileDescriptor): Completable {
        return Completable.create { emitter ->
            mediaPlayer = MediaPlayer()
            mediaPlayer?.apply {
                setAudioAttributes(AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_MUSIC).build())
                setDataSource(file)
                setOnErrorListener(onErrorListener(emitter))
                setOnCompletionListener(onCompletionListener(emitter))
                setOnPreparedListener(onPreparedListener)
                prepareAsync()
            }
        }
    }

    fun clear() = mediaPlayer?.release()


    private val onPreparedListener = MediaPlayer.OnPreparedListener {
        val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val volume =
            (1 - (Math.log((maxVolume.toDouble() - currentVolume.toDouble())) / Math.log(maxVolume.toDouble()))).toFloat()
        mediaPlayer?.setVolume(volume, volume)
        mediaPlayer?.start()
    }

    private fun onErrorListener(emitter: CompletableEmitter) = MediaPlayer.OnErrorListener { _, _, _ ->
        emitter.onError(IllegalStateException())
        true
    }

    private fun onCompletionListener(emitter: CompletableEmitter) = MediaPlayer.OnCompletionListener {
        emitter.onComplete()
        mediaPlayer?.release()
    }
}