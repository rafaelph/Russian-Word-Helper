package com.rafelds.russianhelper.home

import com.rafelds.russianhelper.data.RussianWord

interface MainActivityView {
    fun showAddWordDialog()
    fun showWordAddedSnackbar()
    fun showWordDeletedSnackbar()
    fun updateWordList(results: ArrayList<RussianWord>)
    fun insertWord(word: RussianWord)
    fun deleteWord(id: String)
    fun openDetailsScreen(russianWord: RussianWord)
}