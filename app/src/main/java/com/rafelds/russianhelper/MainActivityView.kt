package com.rafelds.russianhelper

interface MainActivityView {
    fun showAddWordDialog()
    fun showSuccessSnackbar()
    fun updateWordList(results: List<RussianWord>)
}