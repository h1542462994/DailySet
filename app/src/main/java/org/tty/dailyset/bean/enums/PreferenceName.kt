package org.tty.dailyset.bean.enums

import org.tty.dailyset.bean.entity.User

/**
 * defined name-value pairs in [Preference]
 */
enum class PreferenceName(
    var key: String,
    var defaultValue: String
) {
    /**
     * 数据库种子数据版本号
     */
    SEED_VERSION("seed_version", "0"),
    /**
     * 当前使用的账户
     */
    CURRENT_USER_UID("current_user_uid", "#local"),
    /**
     * 当前使用的亮色主题，值为system表示跟随系统，dark表示深色模式，light表示亮色模式
     */
    CURRENT_LIGHT_MODE("current_light_mode", "system"),
    /**
     * 表示是否启用开发者设置
     */
    DEVELOPMENT_OPEN("development_on", "false"),
    /**
     * 表示是否使用自定义的host
     */
    USE_HOST("use_host", "false"),
    /**
     * 表示当前的host
     */
    CURRENT_HOST("current_host", "124.221.216.127"),

    CURRENT_HTTP_SERVER_ADDRESS("current_http_address", "http://124.221.216.127:8086/"),
    /**
     * 一周的开始星期
     */
    START_DAY_OF_WEEK("start_day_of_week", "1"),

    /**
     * 是否是第一次加载
     */
    FIRST_LOAD_USER("first_load_user", "true"),

    /**
     * 设备码
     */
    DEVICE_CODE("device_code", "")
}