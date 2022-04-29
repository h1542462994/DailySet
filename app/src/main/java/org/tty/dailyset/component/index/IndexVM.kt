package org.tty.dailyset.component.index

import org.tty.dailyset.MainActions
import org.tty.dailyset.component.common.BaseVM

@Deprecated("not used yet, initialized is start by application.")
interface IndexVM: BaseVM {
    /**
     * 进入首页之后的初次加载
     */
    fun firstLoad(nav: MainActions)
}