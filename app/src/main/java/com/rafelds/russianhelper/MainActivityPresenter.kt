package com.rafelds.russianhelper

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityPresenter @Inject constructor(private val russianWordService: RussianWordService) {

    private lateinit var view: MainActivityView

    fun attachView(view: MainActivityView) {
        this.view = view
    }

    fun onCreate() {
        view.updateWordList(russianWordService.getAllWords())
    }

    fun onAddButtonClick() {
        view.showAddWordDialog()
    }

    fun onSaveButtonClick(russianWord: RussianWord) {
        russianWordService.addWord(russianWord)
        view.updateWordList(russianWordService.getAllWords())
        view.showSuccessSnackbar()
    }

}