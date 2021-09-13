package org.tty.dailyset.common.local

import org.tty.dioc.util.Logger

/**
 * the config of [Logger]
 * TODO: migrate to dioc.
 */
interface LoggerConfig {
    /**
     * close the tag.
     */
    fun closeTag(tag: String)

    /**
     * open the tag.
     */
    fun openTag(tag: String)
}