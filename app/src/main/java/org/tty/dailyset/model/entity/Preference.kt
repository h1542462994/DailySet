package org.tty.dailyset.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

/**
 * 保存关键配置和用户的个性化配置
 */
@Entity(tableName = "preference")
data class Preference(
    @PrimaryKey
    var name: String,
    var useDefault: Boolean,
    var value: String
) {

    companion object {
        fun defaultOrValue(preference: Preference?, name: PreferenceName): String {
            return defaultOrValue(preference, name.key)
        }

        @Suppress
        fun defaultOrValue(preference: Preference?, name: String): String {
            return if (preference != null && !preference.useDefault){
                preference.value
            } else {
                PreferenceName.values().find { it.key == name }?.defaultValue ?: throw IllegalArgumentException("key $name is not defined.")
            }
        }

        fun default(name: PreferenceName): Preference {
            val preferenceName = PreferenceName.values().find { it.key == name.key }
            return if (preferenceName != null)
            {
                Preference(preferenceName.name, true, preferenceName.defaultValue)
            } else {
                throw IllegalArgumentException("key $name is not defined.")
            }
        }
    }

    override fun toString(): String {
        return "($name, $useDefault, $value)"
    }
}