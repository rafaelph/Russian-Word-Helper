package com.rafelds.russianhelper

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.list_item_word.view.*

class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setViewDetails(russianWord: RussianWord) {
        itemView.russian_word.text = russianWord.russianWord
        itemView.description.text = russianWord.description
        itemView.tag = russianWord.id
    }

    fun setLongClickListener(clickListener: (id: String) -> Boolean) {
        itemView.setOnLongClickListener {
            clickListener.invoke(itemView.tag as String)
        }
    }

}