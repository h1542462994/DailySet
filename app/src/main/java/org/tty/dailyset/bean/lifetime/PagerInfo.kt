package org.tty.dailyset.bean.lifetime

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