package org.tty.dailyset.component.common


class LtsViewModelStore {
    private val store = HashMap<String, Any>()

    @Suppress("UNCHECKED_CAST")
    fun <T: Any> getVM(key: String, initializer: () -> T): T {
        if (!store.containsKey(key)) {
            store[key] = initializer()
        }
        return store[key] as T
    }
}