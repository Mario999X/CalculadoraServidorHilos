package server

import models.Cache
import mu.KotlinLogging
import java.net.ServerSocket
import java.net.Socket

private val log = KotlinLogging.logger { }

fun main() {
    // Datos del servidor y variables
    val servidor: ServerSocket
    var cliente: Socket
    val puerto = 6969

    val cache = Cache()

    log.debug { "Arrancando servidor..." }
    try {
        servidor = ServerSocket(puerto)
        while (true) {
            log.debug { "\t--Servidor esperando..." }

            cliente = servidor.accept()
            log.debug { "Peticion de cliente -> " + cliente.inetAddress + " --- " + cliente.port }

            val gc = GestionClientes(cliente, cache)
            gc.run()
            log.debug { "Cliente -> " + cliente.inetAddress + " --- " + cliente.port + " desconectado." }
        }
        //servidor.close()
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    }
}
