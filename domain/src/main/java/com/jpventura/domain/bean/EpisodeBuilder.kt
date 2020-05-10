/**
 * Copyright (c) 2020 Joao Paulo Fernandes Ventura
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */

package com.jpventura.domain.bean

fun episode(block: EpisodeBuilder.() -> Unit): Episode = EpisodeBuilder()
    .apply(block)
    .build()

class EpisodeBuilder {
    var id: Long? = null
    var name: String? = null
    var number: Int? = null
    var season: Int? = null
    var summary: String? = null
    var image: String? = null
    var seriesId: Long? = null

    fun build(): Episode = Episode(
        key = requireNotNull(id) { "Missing episode ID" },
        parentKey = requireNotNull(seriesId) { "Missing series ID" },
        image = requireNotNull(image) { "Missing episode image" },
        name = requireNotNull(name) { "Missing episode name" },
        number = requireNotNull(number) { "Missing episode number" },
        season = requireNotNull(season) { "Missing episode season" },
        summary = summary ?: "No summary"
    )
}
