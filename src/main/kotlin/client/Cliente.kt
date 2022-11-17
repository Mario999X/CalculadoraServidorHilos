package client

import models.Operacion
import mu.KotlinLogging
import java.io.DataInputStream
import java.io.ObjectOutputStream
import java.net.InetAddress
import java.net.Socket

private val log = KotlinLogging.logger { }

fun main() {
    // Informacion del cliente y la conexion a realizar
    val direccion: InetAddress
    val servidor: Socket
    val puerto = 6969

    log.debug { "Por favor, introduzca el PRIMER numero de su operacion:" }
    val num1 = readln().toInt()
    log.debug { "Por favor, introduzca el SEGUNDO numero de su operacion:" }
    val num2 = readln().toInt()
    log.debug { "Por favor, introduzca el TIPO de OPERACION a realizar:" }
    val operador = readln()

    val operacion = Operacion(num1, num2, operador)
    //println(operacion)
    val resultado: Int

    try {
        direccion = InetAddress.getLocalHost()
        servidor = Socket(direccion, puerto)

        log.debug { "Conectado al servidor" }

        // Envio los datos para el servdor, los que componen a "Operacion"
        val sendOperacion = ObjectOutputStream(servidor.getOutputStream())
        sendOperacion.writeObject(operacion)

        log.debug { "Esperando respuesta del servidor..." }
        // RESPUESTA DEL SERVIDOR

        val respuestaOperacion = DataInputStream(servidor.getInputStream())
        resultado = respuestaOperacion.readInt()

        log.debug { "\t-Respuesta del servidor: $resultado" }

        sendOperacion.close()
        respuestaOperacion.close()
        servidor.close()

        log.debug { "Desconectado" }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}