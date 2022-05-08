package org.tty.dailyset.bean.enums

/**
 * the period code
 * used for [DailySetBinding]
 * @see [DailySetBinding]
 * @see [DailyDuration]
 */
enum class DailySetPeriodCode(val code: Int) {
    /**
     * unspecified period code.
     */
    UnSpecified(0),

    /**
     * 上学期 the first term of the year.
     */
    FirstTerm(1),

    /**
     * 上学期考试周
     */
    FirstTermEnd(2),

    /**
     * 寒假 winter vacation.
     */
    WinterVacation(4),

    /**
     * 下学期 the second term of the year.
     */
    SecondTerm(7),

    /**
     * 下学期考试周
     */
    SecondTermEnd(8),

    /**
     * 短学期 short term
     */
    ShortTerm(13),

    /**
     * 暑假 summer vacation
     */
    SummerVacation(14);

    companion object {
        fun of(code: Int): DailySetPeriodCode {
            return values().single { it.code == code }
        }
    }

    fun toDisplayString(): String {
        return when (this) {
            UnSpecified -> "未确定"
            FirstTerm -> "上学期"
            FirstTermEnd -> "上学期考试周"
            WinterVacation -> "寒假"
            SecondTerm -> "下学期"
            SecondTermEnd -> "下学期考试周"
            ShortTerm -> "短学期"
            SummerVacation -> "暑假"
        }
    }
}
