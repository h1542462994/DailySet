package org.tty.dailyset.common.util

class Diff<TSource, TTarget, TKey>(
    val source: List<TSource>,
    val target: List<TTarget>,
    private val equality: Equality<TSource, TTarget, TKey>
) {
    private var initialized: Boolean = false
    private var sameList: MutableList<Same<TSource, TTarget, TKey>> = mutableListOf()
    private var addList: MutableList<TTarget> = mutableListOf()
    private var removeList: MutableList<TSource> = mutableListOf()

    private fun calculate() {
        if (!initialized) {
            initialized = true
            val sourceMap = source.associateBy { equality.sourceKeySelector(it) }.toMutableMap()

            for (t in target) {
                val k = equality.targetKeySelector(t)
                val s = sourceMap[k]
                if (s == null) {
                    addList.add(t)
                } else {
                    sourceMap.remove(k)
                    sameList.add(Same(k, s, t))
                }
            }
            for (entry in sourceMap.entries) {
                removeList.add(entry.value)
            }
        }
    }

    val sames: List<Same<TSource, TTarget, TKey>>
        get() {
            calculate()
            return sameList
        }

    val adds: List<TTarget>
        get() {
            calculate()
            return addList
        }

    val removes: List<TSource>
        get() {
            calculate()
            return removeList
        }
}

interface Equality<TSource, TTarget, TKey> {
    fun sourceKeySelector(source: TSource): TKey
    fun targetKeySelector(target: TTarget): TKey
}

data class Same<TSource, TTarget, TKey>(
    val key: TKey,
    val source: TSource,
    val target: TTarget
)

class DiffBuilder<TSource, TTarget, TKey> {

    var source = listOf<TSource>()
    var target = listOf<TTarget>()
    lateinit var sourceKeySelector: (source: TSource) -> TKey
    lateinit var targetKeySelector: (target: TTarget) -> TKey

    fun build(): Diff<TSource, TTarget, TKey> {
        return Diff(source, target, object: Equality<TSource, TTarget, TKey> {
            override fun sourceKeySelector(source: TSource): TKey {
                return this@DiffBuilder.sourceKeySelector(source)
            }

            override fun targetKeySelector(target: TTarget): TKey {
                return this@DiffBuilder.targetKeySelector(target)
            }
        })
    }

}

@Suppress("FunctionName")
fun <TSource, TTarget, TKey> Diff(builderLambda: DiffBuilder<TSource, TTarget, TKey>.() -> Unit): Diff<TSource, TTarget, TKey> {
    val builder = DiffBuilder<TSource, TTarget, TKey>()
    builder.builderLambda()
    return builder.build()
}