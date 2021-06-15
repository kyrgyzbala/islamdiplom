package kg.programm.programmingapp.data.test

import java.io.Serializable

data class ModelQuestion(
    val question: String,
    val varA: String,
    val varB: String,
    val varC: String,
    val varD: String,
    val varE: String?,
    val answer: Int,
    val description: String?,
    val photo: String?,
    var userAnswer: Int? = -1
) : Serializable
