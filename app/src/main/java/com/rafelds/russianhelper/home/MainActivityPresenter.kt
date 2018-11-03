package com.rafelds.russianhelper.home

import com.rafelds.russianhelper.data.RussianWord
import com.rafelds.russianhelper.data.RussianWordService
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers.io
import java.util.*
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

    fun onFabClick() = view.showAddWordDialog()

    fun onSaveClick(word: String, description: String) {
        russianWordService.addWord(RussianWord(UUID.randomUUID().toString(), word, description, Date()))
            .observeOn(mainThread())
            .subscribeOn(io())
            .subscribe(getUpdateViewOnSaveObserver())
    }

    fun onItemClick(russianWord: RussianWord) = view.openDetailsScreen(russianWord)

    fun onUndoClick(russianWord: RussianWord, index: Int) {
        russianWordService.addWord(russianWord)
            .observeOn(mainThread())
            .subscribeOn(io())
            .subscribe(getUpdateViewOnUndoObserver(index))
    }

    fun onItemSwipe(russianWord: RussianWord) {
        russianWordService.deleteWord(russianWord.id)
            .observeOn(mainThread())
            .subscribeOn(io())
            .subscribe(getUpdateViewOnDeleteObserver(russianWord, view.getWordIndex(russianWord.id)))
    }

    private fun getUpdateViewObserver() = object : SingleObserver<ArrayList<RussianWord>> {
        override fun onSuccess(results: ArrayList<RussianWord>) = view.updateWordList(results)
        override fun onSubscribe(d: Disposable) = Unit
        override fun onError(e: Throwable) = Unit

    }

    private fun getUpdateViewOnDeleteObserver(russianWord: RussianWord, index: Int) = object : CompletableObserver {
        override fun onComplete() {
            view.deleteWord(russianWord.id)
            view.showWordDeletedSnackbar(russianWord, index)
        }

        override fun onSubscribe(d: Disposable) = Unit
        override fun onError(e: Throwable) = Unit

    }

    private fun getUpdateViewOnSaveObserver() = object : SingleObserver<RussianWord> {
        override fun onSuccess(result: RussianWord) {
            view.insertWord(result, 0)
            view.showWordAddedSnackbar()
        }

        override fun onSubscribe(d: Disposable) = Unit
        override fun onError(e: Throwable) = Unit

    }

    private fun getUpdateViewOnUndoObserver(index: Int) = object : SingleObserver<RussianWord> {
        override fun onSuccess(result: RussianWord) = view.insertWord(result, index)
        override fun onSubscribe(d: Disposable) = Unit
        override fun onError(e: Throwable) = Unit

    }

}