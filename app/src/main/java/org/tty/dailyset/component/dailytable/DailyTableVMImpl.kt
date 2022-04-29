package org.tty.dailyset.component.dailytable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.tty.dailyset.annotation.UseComponent
import org.tty.dailyset.bean.entity.DailyTRC
import org.tty.dailyset.bean.entity.DailyTable
import org.tty.dailyset.bean.entity.User
import org.tty.dailyset.bean.lifetime.dailytable.DailyTableState2
import org.tty.dailyset.component.common.SharedComponents
import org.tty.dailyset.component.common.sharedComponents

@Composable
@UseComponent
fun rememberDailyTableVM(): DailyTableVM {
    val sharedComponents = sharedComponents()
    return rememberSaveable("", saver = DailyTableVMSaver()) {
        DailyTableVMImpl(sharedComponents)
    }
}

class DailyTableVMImpl(private val sharedComponents: SharedComponents): DailyTableVM {
    override val dropDownTitleOpen: MutableStateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val dropDownExtensionOpen: MutableStateFlow<Boolean>
        get() = TODO("Not yet implemented")
    override val dailyTableUid: MutableStateFlow<String>
        get() = TODO("Not yet implemented")
    override val dailyTables: StateFlow<List<DailyTable>>
        get() = TODO("Not yet implemented")
    override val dailyTRC: StateFlow<DailyTRC>
        get() = TODO("Not yet implemented")
    override val currentUser: StateFlow<User>
        get() = TODO("Not yet implemented")
    override val dailyTableState2: StateFlow<DailyTableState2>
        get() = TODO("Not yet implemented")
}

class DailyTableVMSaver: Saver<DailyTableVM, HashMap<String, Any>> {
    override fun restore(value: HashMap<String, Any>): DailyTableVM? {
        TODO("Not yet implemented")
    }

    override fun SaverScope.save(value: DailyTableVM): HashMap<String, Any>? {
        TODO("Not yet implemented")
    }

}