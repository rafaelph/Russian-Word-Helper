package com.rafelds.russianhelper.details.speechtotext

import android.content.Context
import com.rafelds.russianhelper.BuildConfig
import com.voicerss.tts.*
import io.reactivex.Completable
import io.reactivex.Single
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import javax.inject.Inject


class VoiceRssServiceWrapper
@Inject constructor(
    private val context: Context,
    private val mediaPlayerHelper: MediaPlayerHelper
) {

    companion object {
        private const val API_KEY = BuildConfig.VOICE_API_KEY
    }

    fun downloadSpeech(id: String, text: String): Single<String> {
        return Single.create { emitter ->
            val voiceProvider = VoiceProvider(API_KEY)
            val params = VoiceParameters(text, Languages.Russian).apply {
                codec = AudioCodec.WAV
                format = AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo
                base64 = false
                ssml = false
                rate = 0
            }

            val fileName = id.toMp3FilePathName(context)
            voiceProvider.addSpeechDataEventListener(object : SpeechDataEventListener() {
                override fun handleSpeechDataEvent(speechData: SpeechDataEvent<*>) {
                    try {
                        val fileOutputStream = FileOutputStream(File(fileName))
                        fileOutputStream.write(speechData.data as ByteArray)
                        fileOutputStream.close()
                        emitter.onSuccess(id)
                    } catch (e: Exception) {
                        emitter.onError(e)
                    }
                }
            })
            voiceProvider.speechAsync(params)
        }
    }


    fun play(id: String): Completable = mediaPlayerHelper.play(FileInputStream(id.toMp3FilePathName(context)).fd)

    fun clear() = mediaPlayerHelper.clear()

    fun clearCache(id: String) {
        val file = File(id.toMp3FilePathName(context))
        if (file.exists()) {
            file.delete()
        }
    }

    fun voiceExists(id: String): Boolean {
        return File(id.toMp3FilePathName(context)).exists()
    }

    private fun String.toMp3FilePathName(context: Context): String {
        return "${context.filesDir}/$this.mp3"
    }

}