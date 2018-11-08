package com.rafaelds.russianhelper.details

import com.rafaelds.russianhelper.data.RussianWordService
import com.rafaelds.russianhelper.details.speechtotext.VoiceRssServiceWrapper
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class WordDetailActivityPresenter
@Inject constructor(
    private val russianWordService: RussianWordService,
    private val voiceRssServiceWrapper: VoiceRssServiceWrapper
) {

    private lateinit var view: WordDetailActivityView

    fun attachView(view: WordDetailActivityView) {
        this.view = view
    }

    fun onEditButtonClick() {
        view.showEditDialog(view.id, view.wordTitle, view.description)
    }

    fun onPlayButtonClick() {
        val wordId = view.id
        if (voiceRssServiceWrapper.voiceExists(wordId)) {
            voiceRssServiceWrapper.play(wordId).subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) = view.disableSpeakerButton()
                override fun onComplete() = view.enableSpeakerButton()
                override fun onError(e: Throwable) = Unit
            })
        } else {
            voiceRssServiceWrapper.downloadSpeech(wordId, view.wordTitle)
                .observeOn(mainThread())
                .subscribeOn(io())
                .doAfterSuccess {
                    view.hideSpeakerLoading()
                }
                .flatMapCompletable {
                    voiceRssServiceWrapper.play(it)
                        .doOnSubscribe {
                            view.disableSpeakerButton()
                        }.doOnComplete {
                            view.enableSpeakerButton()
                        }
                }.subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) = view.showSpeakerLoading()
                    override fun onComplete() = Unit
                    override fun onError(e: Throwable) = Unit
                })
        }
    }

    fun onSaveClick(id: String, word: String, description: String) {
        russianWordService.editWord(id, word, description)
            .observeOn(mainThread())
            .subscribeOn(io())
            .subscribe(object : CompletableObserver {
                override fun onSubscribe(d: Disposable) {
                    view.showSnackbarEditSuccessful()
                    view.updateView(word, description)
                }

                override fun onComplete() = voiceRssServiceWrapper.clearCache(id)
                override fun onError(e: Throwable) = Unit
            })
    }

    fun onStop() = voiceRssServiceWrapper.clear()
}