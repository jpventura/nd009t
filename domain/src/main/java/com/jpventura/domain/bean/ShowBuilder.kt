package com.jpventura.domain.bean

fun show(block: ShowBuilder .() -> Unit): Show = ShowBuilder()
    .apply(block)
    .build()

class ShowBuilder {
    var id: Long? = null
    var genres: List<String>? = null
    var name: String? = null
    var poster: String? = null
    var rating: Double? = 0.0
    var schedule: Schedule? = null
    var summary: String? = null

    fun build(): Show = Show(
        key = requireNotNull(id) { "Missing id" },
        genres = requireNotNull(genres) { "Missing genres" },
        name = requireNotNull(name) { "Missing name" },
        poster = requireNotNull(poster) { "Missing poster" },
        schedule = requireNotNull(schedule) { "Missing schedule" },
        rating = rating ?: 0.0,
        summary = summary ?: ""
    )

}
