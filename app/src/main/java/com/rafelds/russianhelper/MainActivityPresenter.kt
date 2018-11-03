package com.rafelds.russianhelper

import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityPresenter @Inject constructor(private val russianWordService: RussianWordService) {

    private lateinit var view: MainActivityView

    fun attachView(view: MainActivityView) {
        this.view = view
    }

    fun onCreate() {
        russianWordService.getAllWords()
            .observeOn(mainThread())
            .subscribeOn(io())
            .subscribe(getUpdateViewObserver())
    }

    fun onAddButtonClick() = view.showAddWordDialog()

    fun onSaveButtonClick(word: String, description: String) {
        russianWordService.addWord(word, description)
            .andThen(russianWordService.getAllWords())
            .observeOn(mainThread())
            .subscribeOn(io())
            .subscribe(getUpdateViewOnSaveObserver())
    }

    fun onItemLongClick(id: String): Boolean {
        russianWordService.deleteWord(id)
            .andThen(russianWordService.getAllWords())
            .observeOn(mainThread())
            .subscribeOn(io())
            .subscribe(getUpdateViewOnDeleteObserver())
        return true
    }

    private fun getUpdateViewObserver() = object : SingleObserver<List<RussianWord>> {
        override fun onSuccess(results: List<RussianWord>) = view.updateWordList(results)
        override fun onSubscribe(d: Disposable) = Unit
        override fun onError(e: Throwable) = Unit

    }

    private fun getUpdateViewOnSaveObserver() = object : SingleObserver<List<RussianWord>> {
        override fun onSuccess(results: List<RussianWord>) {
            view.updateWordList(results)
            view.showWordAddedSnackbar()
        }

        override fun onSubscribe(d: Disposable) = Unit
        override fun onError(e: Throwable) = Unit

    }

    private fun getUpdateViewOnDeleteObserver() = object : SingleObserver<List<RussianWord>> {
        override fun onSuccess(results: List<RussianWord>) {
            view.updateWordList(results)
            view.showWordDeletedSnackbar()
        }

        override fun onSubscribe(d: Disposable) = Unit
        override fun onError(e: Throwable) = Unit

    }

}