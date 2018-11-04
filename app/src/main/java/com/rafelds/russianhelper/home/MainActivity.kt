package com.rafelds.russianhelper.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog.*
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import com.rafelds.russianhelper.R
import com.rafelds.russianhelper.RussianHelperApplication
import com.rafelds.russianhelper.data.RussianWord
import com.rafelds.russianhelper.details.WordDetailActivity
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), MainActivityView {

    @Inject
    lateinit var presenter: MainActivityPresenter

    private lateinit var wordAdapter: WordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (application as RussianHelperApplication).getComponent().inject(this)
        setContentView(R.layout.activity_main)
        setSupportActionBar(activity_main_toolbar)

        wordAdapter = WordAdapter()
        wordAdapter.clickListener = presenter::onItemClick

        activity_main_recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            val animator = FadeInAnimator()
            animator.addDuration = 200L
            animator.setInterpolator(LinearInterpolator())
            itemAnimator = animator
            adapter = wordAdapter
            addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            addOnScrollListener(hideFabOnScrollUpListener(activity_main_fab))
        }

        val swipeTouchHelper = ItemTouchHelper(object : SwipeToDeleteCallback(this) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                presenter.onItemSwipe(viewHolder.itemView.tag as RussianWord)
            }
        })
        swipeTouchHelper.attachToRecyclerView(activity_main_recycler_view)

        activity_main_fab.setOnClickListener { presenter.onFabClick() }

        presenter.attachView(this)
        presenter.onCreate()
    }

    @SuppressLint("InflateParams")
    override fun showAddWordDialog() {
        val inflatedDialogView = layoutInflater.inflate(R.layout.dialog_add_word, null)
        val alertDialog = Builder(this).create()
        alertDialog.apply {
            setButton(BUTTON_POSITIVE, getString(R.string.save)) { _, _ ->
                val mainWord = inflatedDialogView.findViewById<AppCompatEditText>(R.id.dialog_main_word).text.toString()
                val description =
                    inflatedDialogView.findViewById<AppCompatEditText>(R.id.dialog_description).text.toString()
                presenter.onSaveClick(mainWord, description)
            }
            setButton(BUTTON_NEGATIVE, getString(R.string.cancel)) { _, _ -> }
            setTitle(getString(R.string.add_new_word_title))
            setView(inflatedDialogView)
            setCancelable(false)
            window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        }.show()
    }

    override fun updateWordList(results: ArrayList<RussianWord>) {
        wordAdapter.words = results
    }

    override fun getWordIndex(id: String): Int = wordAdapter.getIndex(id)

    override fun insertWord(word: RussianWord, index: Int) = wordAdapter.addItem(word, index)

    override fun deleteWord(id: String) = wordAdapter.deleteItem(id)

    override fun openDetailsScreen(russianWord: RussianWord) {
        val intent = Intent(this, WordDetailActivity::class.java)
        intent.putExtra(WordDetailActivity.EXTRA_WORD, russianWord)
        startActivity(intent)
    }

    override fun showWordAddedSnackbar() {
        val snackBar = Snackbar.make(
            findViewById(R.id.activity_main_fab),
            getString(R.string.word_save_successful),
            Snackbar.LENGTH_LONG
        )
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkGreen))
        snackBar.show()
    }

    override fun showWordDeletedSnackbar(russianWord: RussianWord, index: Int) {
        val snackBar = Snackbar.make(
            findViewById(R.id.activity_main_fab),
            getString(R.string.word_delete_successful),
            Snackbar.LENGTH_LONG
        )
        snackBar.setAction(getString(R.string.undo)) {
            presenter.onUndoClick(russianWord, index)
        }
        snackBar.setActionTextColor(ContextCompat.getColor(this, R.color.colorWhite))
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, R.color.colorDarkGreen))
        snackBar.show()
    }

    private fun hideFabOnScrollUpListener(fab: FloatingActionButton) = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            when {
                dy > 0 -> fab.hide()
                dy < 0 -> fab.show()
            }
        }
    }
}
