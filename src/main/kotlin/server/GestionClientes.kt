package server

import models.Operacion
import java.io.DataOutputStream
import java.io.ObjectInputStream
import java.net.Socket

private lateinit var op: Operacion

class GestionClientes(private val s: Socket) : Runnable {

    override fun run() {
        s.setSoLinger(true, 10) // Tiempo que esta abierto el puerto

        // Recibo los datos mandados del cliente
        val readerObject = ObjectInputStream(s.getInputStream())

        op = Operacion(readerObject.readInt(), readerObject.readInt(), readerObject.readUTF())

        var result = 0

        when (op.operador) {
            "suma" -> result = (op.num1 + op.num2)
            "resta" -> result = (op.num1 - op.num2)
            "multiplicacion" -> result = (op.num1 * op.num2)
            "division" -> result = if (op.num2 > 0) (op.num1 / op.num2) else 0
        }

        val dataResult = DataOutputStream(s.getOutputStream())
        dataResult.writeInt(result)

        readerObject.close()
        s.close()
    }

}