package com.rafaelds.russianhelper.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.WindowManager
import android.widget.TextView
import com.rafaelds.russianhelper.R
import com.rafaelds.russianhelper.RussianHelperApplication
import com.rafaelds.russianhelper.data.RussianWord
import kotlinx.android.synthetic.main.activity_word_detail.*
import javax.inject.Inject

class WordDetailActivity : AppCompatActivity(), WordDetailActivityView {

    override val id: String by lazy {
        (intent.getSerializableExtra(EXTRA_WORD) as RussianWord).id
    }

    override val wordTitle: String
        get() = activity_word_detail_word_title.text.toString()

    override val description: String
        get() = activity_word_detail_description.text.toString()

    companion object {
        const val EXTRA_WORD = "WORD_OBJECT"
    }

    @Inject
    lateinit var presenter: WordDetailActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as RussianHelperApplication).getComponent().inject(this)
        setContentView(R.layout.activity_word_detail)
        setSupportActionBar(activity_word_detail_toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val initialRussianWord = intent.getSerializableExtra(EXTRA_WORD) as RussianWord
        activity_word_detail_word_title.text = initialRussianWord.russianWord
        activity_word_detail_description.text = initialRussianWord.description

        activity_word_detail_play_button.setOnClickListener {
            presenter.onPlayButtonClick()
        }

        presenter.attachView(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_word_detail, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit -> {
                presenter.onEditButtonClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    @SuppressLint("InflateParams")
    override fun showEditDialog(id: String, word: String, description: String) {
        val inflatedDialogView = layoutInflater.inflate(R.layout.dialog_add_word, null)
        inflatedDialogView.findViewById<AppCompatEditText>(R.id.dialog_main_word)
            .setText(word, TextView.BufferType.EDITABLE)
        inflatedDialogView.findViewById<AppCompatEditText>(R.id.dialog_description)
            .setText(description, TextView.BufferType.EDITABLE)
        val alertDialog = AlertDialog.Builder(this).create()
        alertDialog.apply {
            setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.save)) { _, _ ->
                val newWord = inflatedDialogView.findViewById<AppCompatEditText>(R.id.dialog_main_word).text.toString()
                val newDescription =
                    inflatedDialogView.findViewById<AppCompatEditText>(R.id.dialog_description).text.toString()
                presenter.onSaveClick(id, newWord, newDescription)
            }
            setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ -> }
            setTitle(getString(R.string.add_edit_word_title))
            setView(inflatedDialogView)
            setCancelable(false)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }.show()
    }

    override fun updateView(word: String, description: String) {
        activity_word_detail_word_title.text = word
        activity_word_detail_description.text = description
    }

    override fun showSnackbarEditSuccessful() {
        val snackBar = Snackbar.make(
            findViewById(R.id.activity_word_detail_toolbar),
            getString(R.string.word_save_successful),
            Snackbar.LENGTH_LONG
        )
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkGreen))
        snackBar.show()
    }

    override fun showSpeakerLoading() {
        activity_word_detail_play_progress.visibility = VISIBLE
        activity_word_detail_play_button.visibility = GONE
    }

    override fun hideSpeakerLoading() {
        activity_word_detail_play_progress.visibility = GONE
        activity_word_detail_play_button.visibility = VISIBLE
    }

    override fun enableSpeakerButton() {
        activity_word_detail_play_button.isEnabled = true
    }

    override fun disableSpeakerButton() {
        activity_word_detail_play_button.isEnabled = false
    }
}