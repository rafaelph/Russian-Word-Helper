package com.rafelds.russianhelper

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class WordAdapter : RecyclerView.Adapter<WordViewHolder>() {

    var longClickListener: (id: String) -> Boolean = { false }

    var words: ArrayList<RussianWord> = arrayListOf()
        set(value) {
            field = value
            notifyItemRangeInserted(0, value.size)
        }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(viewHolder: WordViewHolder, index: Int) {
        viewHolder.setViewDetails(words[index])
        viewHolder.setLongClickListener(longClickListener)
    }

    fun addItem(word: RussianWord) {
        words.add(word)
        notifyItemInserted(words.size - 1)
    }

    fun deleteItem(id: String) {
        val wordToDelete = words.first {
            it.id == id
        }
        notifyItemRemoved(words.indexOf(wordToDelete))
        words.remove(wordToDelete)
    }
}