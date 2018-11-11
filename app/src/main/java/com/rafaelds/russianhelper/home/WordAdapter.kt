package com.rafaelds.russianhelper.home

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.miguelcatalan.materialsearchview.MaterialSearchView
import com.rafaelds.russianhelper.R
import com.rafaelds.russianhelper.data.RussianWord
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class WordAdapter : RecyclerView.Adapter<WordViewHolder>(), MaterialSearchView.OnQueryTextListener {

    var clickListener: (russianWord: RussianWord) -> Unit = {}

    private var wordsToDisplay: ArrayList<RussianWord> = arrayListOf()
        @SuppressLint("CheckResult")
        set(value) {
            WordDiffCallback(field, value)
                .calculate()
                .observeOn(mainThread())
                .subscribeOn(io())
                .subscribe { result ->
                    field = value
                    result.dispatchUpdatesTo(this)
                }
        }

    var words: ArrayList<RussianWord> = arrayListOf()
        set(value) {
            field = value
            wordsToDisplay = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int = wordsToDisplay.size

    override fun onBindViewHolder(viewHolder: WordViewHolder, index: Int) {
        viewHolder.setViewDetails(wordsToDisplay[index])
        viewHolder.setClickListener(clickListener)
    }

    fun addItem(word: RussianWord, index: Int) {
        val tempWords = ArrayList(words)
        tempWords.add(index, word)
        words = tempWords
    }

    fun deleteItem(id: String) {
        val wordToDelete = words.first {
            it.id == id
        }
        val tempWords = ArrayList(words)
        tempWords.remove(wordToDelete)
        words = tempWords
    }

    fun getIndex(id: String) = words.indexOf(words.first {
        it.id == id
    })

    override fun onQueryTextSubmit(query: String?): Boolean = true

    override fun onQueryTextChange(searchString: String): Boolean {
        wordsToDisplay = words.filter { word ->
            word.russianWord.contains(searchString, true) || word.description.contains(searchString, true)
        }.toCollection(arrayListOf())
        return true
    }
}