package com.rafaelds.russianhelper.home

import com.rafaelds.russianhelper.data.RussianWord

interface MainActivityView {
    fun showAddWordDialog()
    fun showWordAddedSnackbar()
    fun updateWordList(results: ArrayList<RussianWord>)
    fun getWordIndex(id: String) : Int
    fun insertWord(word: RussianWord, index: Int)
    fun deleteWord(id: String)
    fun openDetailsScreen(russianWord: RussianWord)
    fun showWordDeletedSnackbar(russianWord: RussianWord, index: Int)
}