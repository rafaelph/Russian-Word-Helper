package com.rafelds.russianhelper.details

import javax.inject.Inject

class WordDetailActivityPresenter @Inject constructor() {

    private lateinit var view: WordDetailActivityView

    fun attachView(view: WordDetailActivityView) {
        this.view = view
    }
}