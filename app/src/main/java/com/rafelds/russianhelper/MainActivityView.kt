package com.rafelds.russianhelper

interface MainActivityView {
    fun showAddWordDialog()
    fun showWordAddedSnackbar()
    fun showWordDeletedSnackbar()
    fun updateWordList(results: List<RussianWord>)
}