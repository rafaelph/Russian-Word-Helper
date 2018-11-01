package com.rafelds.russianhelper

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainActivityPresenter @Inject constructor(private val russianWordService: RussianWordService) {

    private lateinit var view: MainActivityView

    fun attachView(view: MainActivityView) {
        this.view = view
    }

    fun onCreate() = view.updateWordList(russianWordService.getAllWords())

    fun onAddButtonClick() = view.showAddWordDialog()

    fun onSaveButtonClick(word: String, description: String) {
        russianWordService.addWord(word, description)
        view.updateWordList(russianWordService.getAllWords())
        view.showWordAddedSnackbar()
    }

    fun onItemLongClick(id: String): Boolean {
        russianWordService.deleteWord(id)
        view.updateWordList(russianWordService.getAllWords())
        view.showWordDeletedSnackbar()
        return true
    }

}