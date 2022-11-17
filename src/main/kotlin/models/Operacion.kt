package models

import java.io.Serializable

data class Operacion(
    val num1: Int,
    val num2: Int,
    val operador: String
): Serializable {

}