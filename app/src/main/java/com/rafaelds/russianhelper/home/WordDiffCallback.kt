package com.rafaelds.russianhelper.home

import androidx.recyclerview.widget.DiffUtil
import com.rafaelds.russianhelper.data.RussianWord
import io.reactivex.Single

class WordDiffCallback(private val oldWordList: List<RussianWord>, private val newWordList: List<RussianWord>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldPosition: Int, newPosition: Int): Boolean =
        oldWordList[oldPosition].id == newWordList[newPosition].id

    override fun areContentsTheSame(oldPosition: Int, newPosition: Int): Boolean =
        oldWordList[oldPosition] == newWordList[newPosition]

    override fun getOldListSize(): Int = oldWordList.size

    override fun getNewListSize(): Int = newWordList.size

    fun calculate(): Single<DiffUtil.DiffResult> = Single.fromCallable { DiffUtil.calculateDiff(this) }
}