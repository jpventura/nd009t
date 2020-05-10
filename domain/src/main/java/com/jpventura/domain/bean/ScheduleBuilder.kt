package com.jpventura.domain.bean

fun schedule(block: ScheduleBuilder.() -> Unit): Schedule = ScheduleBuilder()
    .apply(block)
    .build()

class ScheduleBuilder {
    var days: List<String>? = mutableListOf()
    var time: String? = null

    fun build(): Schedule = Schedule(
        days = requireNotNull(days),
        time = requireNotNull(time)
    )
}
