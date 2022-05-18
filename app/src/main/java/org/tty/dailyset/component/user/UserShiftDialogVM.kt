package org.tty.dailyset.component.user

import kotlinx.coroutines.flow.MutableStateFlow
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.component.common.DialogVM

interface UserShiftDialogVM: DialogVM {
    val shiftToUser: MutableStateFlow<User>
}