package com.rafelds.russianhelper

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class WordAdapter : RecyclerView.Adapter<WordViewHolder>() {

    var words: List<RussianWord> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): WordViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_word, parent, false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int = words.size

    override fun onBindViewHolder(viewHolder: WordViewHolder, index: Int) {
        viewHolder.setViewDetails(words[index])
    }
}