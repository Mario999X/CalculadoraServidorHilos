package server

import mu.KotlinLogging
import java.net.ServerSocket
import java.net.Socket

//private val log = KotlinLogging.logger { }

fun main() {
    // Datos del servidor y variables
    var servidor: ServerSocket
    var cliente: Socket
    val puerto = 6970

    //log.debug { "Arrancando servidor" }
    try {
        while (true) {

            servidor = ServerSocket(puerto)
            //log.debug { "Servidor esperando..." }

            cliente = servidor.accept()

            val gc = GestionClientes(cliente)
            gc.run()

        }
        //servidor.close()
    } catch (e: IllegalStateException) {
        e.printStackTrace()
    }
}
