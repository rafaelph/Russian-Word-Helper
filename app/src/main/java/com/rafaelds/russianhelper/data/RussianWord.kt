package com.rafaelds.russianhelper.data

import java.io.Serializable
import java.util.*

data class RussianWord(
    val id: String,
    val russianWord: String,
    val description: String,
    val dateAdded: Date
) : Serializable