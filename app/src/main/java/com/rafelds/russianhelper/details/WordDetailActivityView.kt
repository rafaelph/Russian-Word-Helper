package com.rafelds.russianhelper.details

interface WordDetailActivityView {
    val id: String
    val wordTitle: String
    val description: String

    fun showEditDialog(id: String, word: String, description: String)
    fun showSnackbarEditSuccessful()
    fun updateView(word: String, description: String)
    fun showSpeakerLoading()
    fun hideSpeakerLoading()
    fun enableSpeakerButton()
    fun disableSpeakerButton()
}
