package org.tty.dailyset.bean.req

import kotlinx.serialization.Serializable

@Serializable
class DailySetUpdateReq(
    val uid: String,
    val type: Int,
    val sourceVersion: Int,
    val matteVersion: Int,
    val metaVersion: Int
)