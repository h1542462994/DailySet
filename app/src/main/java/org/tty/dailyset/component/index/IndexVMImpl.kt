package org.tty.dailyset.component.index

import androidx.compose.runtime.Composable
import kotlinx.coroutines.launch
import org.tty.dailyset.MainActions
import org.tty.dailyset.bean.enums.IndexLoadResult
import org.tty.dailyset.common.local.logger
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.sharedComponents0

@Composable
fun indexVM(): IndexVM {
    val sharedComponents = sharedComponents0()
    return IndexVMImpl(sharedComponents)
}

@Deprecated("not used yet, initialized is start by application.")
class IndexVMImpl(private val sharedComponents: SharedComponents): IndexVM {

    override fun firstLoad(nav: MainActions) {
        val scope = sharedComponents.applicationScope
        scope.launch {
            val result = sharedComponents.repositoryCollection.userRepository.firstLoad()
            if (result == IndexLoadResult.NotLoad) {
                // 转向登录界面
                logger.d("IndexVMImpl", "你还没有登录")
            }
        }
    }
}