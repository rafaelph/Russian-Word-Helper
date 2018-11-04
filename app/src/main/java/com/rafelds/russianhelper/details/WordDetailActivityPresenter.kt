package com.rafelds.russianhelper.details

import com.rafelds.russianhelper.data.RussianWordService
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject

class WordDetailActivityPresenter @Inject constructor(private val russianWordService: RussianWordService) {

    private lateinit var view: WordDetailActivityView

    fun attachView(view: WordDetailActivityView) {
        this.view = view
    }

    fun onEditButtonClick() {
        view.showEditDialog(view.id, view.wordTitle, view.description)
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

                override fun onComplete() = Unit
                override fun onError(e: Throwable) = Unit
            })
    }
}