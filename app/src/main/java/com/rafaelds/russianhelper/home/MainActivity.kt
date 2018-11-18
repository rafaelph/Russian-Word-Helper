package com.rafaelds.russianhelper.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AlertDialog.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.rafaelds.russianhelper.R
import com.rafaelds.russianhelper.RussianHelperApplication
import com.rafaelds.russianhelper.data.RussianWord
import com.rafaelds.russianhelper.details.WordDetailActivity
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
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp)
        }

        activity_main_navigation_view.setNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_cases -> {}
                R.id.nav_about -> {}
                else -> {}
            }
            activity_main_drawer_layout.closeDrawers()
            true
        }

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
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_activity_main, menu)
        val item = menu.findItem(R.id.action_search)
        activity_main_search_view.setMenuItem(item)
        activity_main_search_view.setOnQueryTextListener(wordAdapter)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                activity_main_drawer_layout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
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
