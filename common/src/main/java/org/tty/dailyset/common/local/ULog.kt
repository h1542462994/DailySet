package org.tty.dailyset.common.local

import android.util.Log
import org.tty.dioc.util.Logger

/**
 * the local [Logger] to redirect the log to [Log].
 * you should use [logger] instead use [ULog] directly.
 */
object ULog: Logger, LoggerConfig {
    private val tags = HashSet<String>()

    override fun d(tag: String, message: String) {
        if (checkTag(tag)) return
        Log.d(tag, message)
    }

    override fun e(tag: String, message: String) {
        if (checkTag(tag)) return
        Log.e(tag, message)
    }

    override fun i(tag: String, message: String) {
        if (checkTag(tag)) return
        Log.i(tag, message)
    }

    override fun v(tag: String, message: String) {
        if (checkTag(tag)) return
        Log.v(tag, message)
    }

    override fun w(tag: String, message: String) {
        if (checkTag(tag)) return
        Log.w(tag, message)
    }

    override fun closeTag(tag: String) {
        tags.add(tag)
    }

    override fun openTag(tag: String) {
        tags.remove(tag)
    }

    private fun checkTag(tag: String): Boolean {
        return tags.contains(tag)
    }

}