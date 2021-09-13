package org.tty.dailyset.common.local
import org.tty.dioc.base.Builder

/**
 * TODO: migrate to dioc
 */
class IntSequence(
    start: Int = 0,
    private val step: Int = 1
): Builder<Int> {
    var current = start

    override fun create(): Int {
        val v = current
        current += step
        return v
    }
}