package com.rafelds.russianhelper

interface MainActivityView {
    fun showAddWordDialog()
    fun showWordAddedSnackbar()
    fun showWordDeletedSnackbar()
    fun updateWordList(results: ArrayList<RussianWord>)
    fun insertWord(word: RussianWord)
    fun deleteWord(id: String)
}