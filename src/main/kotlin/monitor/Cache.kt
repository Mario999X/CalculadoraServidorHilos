package monitor

import models.Operacion
import mu.KotlinLogging
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

private val log = KotlinLogging.logger { }

class Cache(
    private val limiteHistorial: Int = 3
) {

    // Caso de lector-escritor
    private var escritor = false
    private var lector = false

    // Lista donde se guardan las operaciones / Historial
    private val listaOperaciones = mutableListOf<Operacion>()

    // Lock + Condiciones
    private val lock = ReentrantLock()
    private val obtenerOperaciones: Condition = lock.newCondition()
    private val depositaOperaciones: Condition = lock.newCondition()

    // Obtenemos la lista de operaciones
    fun get(): List<Operacion> {
        // Mientras haya un escritor pendiente
        lock.withLock {
            while (escritor) {
                obtenerOperaciones.await()
            }
            lector = true
            val operaciones = listaOperaciones

            log.debug { "\tSe envia el historial..." }

            depositaOperaciones.signalAll()
            lector = false
            return operaciones
        }
    }

    fun put(item: Operacion) {
        lock.withLock {
            // Mientras haya un lector pendiente
            while (lector) {
                depositaOperaciones.await()
            }
            escritor = true
            // Si se llega al limite, se elimina el primero de la lista
            if (listaOperaciones.size == limiteHistorial) {
                listaOperaciones.removeFirst()

                log.debug { "\t-Limite alcanzado, se elimina una operacion" }
            }
            // Agregamos la operacion
            listaOperaciones.add(item)

            escritor = false
            obtenerOperaciones.signalAll()

            log.debug { "\tOperacion introducida..." }
            //println(listaOperaciones)
        }
    }
}