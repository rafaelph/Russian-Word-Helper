package com.rafaelds.russianhelper.home

import android.support.v7.widget.RecyclerView
import android.view.View
import com.rafaelds.russianhelper.data.RussianWord
import kotlinx.android.synthetic.main.list_item_word.view.*

class WordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun setViewDetails(russianWord: RussianWord) {
        itemView.russian_word.text = russianWord.russianWord
        itemView.description.text = russianWord.description
        itemView.tag = russianWord
    }

    fun setClickListener(clickListener: (russianWord: RussianWord) -> Unit) {
        itemView.setOnClickListener {
            clickListener.invoke(itemView.tag as RussianWord)
        }
    }

}