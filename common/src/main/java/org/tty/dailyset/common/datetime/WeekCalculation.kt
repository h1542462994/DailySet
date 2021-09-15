package org.tty.dailyset.common.datetime

/**
 * the policy to calculate the week count.
 * one week can only belong to a [YearWeek] or [MonthWeek]
 */
enum class WeekCalculation {
    /**
     * 以第一天所在的周记为第1周。在这种策略下，月末的几天可能归属与下一个月的第1周。
     */
    ANY,
    /**
     * 以大部分时间（大于等于4天）所在一周记为第1周，在这种策略下，月初的几天可能归属于上一个月的最后1周，月末的几天可能归属与下一个月的第1周。
     */
    MINOR,
    /**
     * 以全部时间所在的第一周记为第1周，在这种策略下，月出的几天可能归属于上一个月的第1周。
     */
    FULL
}