package com.rafelds.russianhelper.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.rafelds.russianhelper.R
import com.rafelds.russianhelper.data.RussianWord

class WordAdapter : RecyclerView.Adapter<WordViewHolder>() {

    var clickListener: (russianWord: RussianWord) -> Unit = {}

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
        viewHolder.setClickListener(clickListener)
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