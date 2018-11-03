package com.rafelds.russianhelper.details

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.rafelds.russianhelper.R
import com.rafelds.russianhelper.RussianHelperApplication
import com.rafelds.russianhelper.RussianWord
import kotlinx.android.synthetic.main.activity_word_detail.*
import javax.inject.Inject

class WordDetailActivity : AppCompatActivity(), WordDetailActivityView {

    companion object {
        const val EXTRA_WORD = "WORD_OBJECT"
    }

    @Inject
    lateinit var presenter: WordDetailActivityPresenter

    override val russianWord: RussianWord by lazy {
        intent.getSerializableExtra(EXTRA_WORD) as RussianWord
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as RussianHelperApplication).getComponent().inject(this)
        setContentView(R.layout.activity_word_detail)
        setSupportActionBar(activity_word_detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        activity_word_detail_word_title.text = russianWord.russianWord
        activity_word_detail_description.text = russianWord.description

        presenter.attachView(this)
    }

}