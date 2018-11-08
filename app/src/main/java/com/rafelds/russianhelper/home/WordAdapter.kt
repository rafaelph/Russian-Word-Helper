package com.rafelds.russianhelper.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rafelds.russianhelper.R
import com.rafelds.russianhelper.data.RussianWord
import io.reactivex.android.schedulers.AndroidSchedulers.mainThread
import io.reactivex.schedulers.Schedulers.io

class WordAdapter : RecyclerView.Adapter<WordViewHolder>() {

    var clickListener: (russianWord: RussianWord) -> Unit = {}

    var words: ArrayList<RussianWord> = arrayListOf()
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

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(viewHolder: WordViewHolder, index: Int) {
        viewHolder.setViewDetails(words[index])
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
}