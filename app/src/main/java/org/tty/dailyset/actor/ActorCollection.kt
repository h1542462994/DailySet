package org.tty.dailyset.actor

interface ActorCollection {
    val userActor: UserActor
    val preferenceActor: PreferenceActor
    val dailySetActor: DailySetActor
}