package org.tty.dailyset.actor

import org.tty.dailyset.component.common.BaseVM
import org.tty.dailyset.datasource.DataSourceCollection
import org.tty.dailyset.component.common.SharedComponents

/**
 * **actor** is the interaction between [BaseVM] and [DataSourceCollection].
 * @see [SharedComponents]
 */
class ActorCollection(
    val userActor: UserActor,
    val preferenceActor: PreferenceActor,
    val dailySetActor: DailySetActor,
    val messageActor: MessageActor,
)