package com.rafelds.russianhelper

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(activity_main_toolbar)

        val wordAdapter = WordAdapter()
        wordAdapter.words = getDummyWords()

        activity_main_recycler_view.layoutManager = LinearLayoutManager(this)
        activity_main_recycler_view.adapter = wordAdapter
        activity_main_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    activity_main_fab.hide()
                } else if (dy < 0) {
                    activity_main_fab.show()
                }
            }
        })

        activity_main_fab.setOnClickListener { view ->
            Snackbar.make(view, "Feature to be implemented soon!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }

    private fun getDummyWords(): List<RussianWord> {
        return listOf(
            RussianWord("Любовь", "To love"),
            RussianWord("Книга", "Book"),
            RussianWord("Здравстьвуйте", "Formal way of saying hello"),
            RussianWord("Любовь", "To love"),
            RussianWord("Книга", "Book"),
            RussianWord("Книга", "Book"),
            RussianWord("Здравстьвуйте", "Formal way of saying hello"),
            RussianWord("Любовь", "To love"),
            RussianWord("Книга", "Book"),
            RussianWord("Книга", "Book"),
            RussianWord("Здравстьвуйте", "Formal way of saying hello"),
            RussianWord("Любовь", "To love"),
            RussianWord("Книга", "Book"),
            RussianWord("Книга", "Book"),
            RussianWord("Здравстьвуйте", "Formal way of saying hello"),
            RussianWord("Любовь", "To love"),
            RussianWord("Книга", "Book"),
            RussianWord("Здравстьвуйте", "Formal way of saying hello")
        )
    }
}
