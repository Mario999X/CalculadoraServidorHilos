package server

import models.Cache
import models.Operacion
import mu.KotlinLogging
import java.io.DataOutputStream
import java.io.ObjectInputStream
import java.net.Socket

private val log = KotlinLogging.logger { }

class GestionClientes(private val s: Socket, private val c: Cache) : Runnable {

    private var result = 0
    private var historial = c.get()

    override fun run() {
        s.setSoLinger(true, 10) // Tiempo que esta abierto el puerto

        // Preparo el historial para mandarlo al cliente
        val cacheHistorial = DataOutputStream(s.getOutputStream())
        cacheHistorial.writeUTF(historial.toString())

        // Recibo los datos mandados del cliente, en este caso, un objeto Operacion
        val readerObject = ObjectInputStream(s.getInputStream())
        val op = readerObject.readObject() as Operacion
        c.put(op)

        log.debug { "\tOperacion recibida: $op" }

        result = if (op.operador.uppercase() == "SUMA" || op.operador == "+") {
            op.num1 + op.num2
        } else if (op.operador.uppercase() == "RESTA" || op.operador == "-") {
            op.num1 - op.num2
        } else if (op.operador.uppercase() == "MULTIPLICACION" || op.operador == "*") {
            op.num1 * op.num2
        } else if (op.operador.uppercase() == "DIVISION" || op.operador == "/") {
            if (op.num2 > 0) op.num1 / op.num2 else 0
        } else {
            0
        }

        log.debug { "\tResultado enviado: $result" }

        val dataResult = DataOutputStream(s.getOutputStream())
        dataResult.writeInt(result)

        cacheHistorial.close()
        readerObject.close()
        dataResult.close()
        s.close()
    }

}