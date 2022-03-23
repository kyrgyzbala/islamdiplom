package kg.programm.programmingapp.data.task

import java.io.Serializable

data class ModelTask(
    val name: String = "",
    val description: String = "",
    val photo: String = ""
): Serializable
