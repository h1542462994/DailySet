package org.tty.dailyset.model.lifetime

data class PagerInfo(
    val size: Int,
    val pageIndex: Int
) {
    override fun toString(): String {
        return "$pageIndex/${size - 1}"
    }

    companion object {
        fun empty(): PagerInfo {
            return PagerInfo(0, 0)
        }
    }
}