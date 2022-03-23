package kg.programm.programmingapp.data.lectures

import java.io.Serializable

data class ModelLecture(
    val order: Int = 0,
    val name: String = "",
    val description: String = "",
    val link: String = "",
    val thumbnail: String = "",
    val category: Int = 0
) : Serializable
