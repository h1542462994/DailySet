package org.tty.dailyset.common.local

import org.tty.dioc.base.Builder

/**
 * a generator for creating unique sequence.
 * TODO: migrate to dioc.
 */
class Signature<T: Any>(private val sequence: Builder<T>) {
    private lateinit var value: T
    private var depth = 0

    fun <R> withSignal(action: (signal: T) -> R): R {
        if (depth == 0) {
            value = sequence.create()
        }
        ++depth
        val r = action(value)
        --depth
        return r
    }

    val signal = value

    companion object {
        /**
         * default int signal
         */
        fun int(start: Int = 0, step: Int = 1): Signature<Int> {
            return Signature(IntSequence(start, step))
        }
    }
}