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

package com.jpventura.data.cloud

import com.jpventura.data.cloud.entity.Episode
import com.jpventura.data.cloud.entity.Search
import com.jpventura.data.cloud.entity.Season
import com.jpventura.data.cloud.entity.Show
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TVMazeClient {

    @GET("episodes/{episodeId}")
    fun getEpisode(
        @Path("episodeId") id: Long
    ): Single<Episode>

    @GET("shows/{showId}/episodes")
    fun getEpisodes(
        @Path("showId") showId: Long,
        @Query("page") page: Int? = 1
    ): Single<List<Episode>>

    @GET("seasons/{seasonId}")
    fun getSeason(
        @Path("seasonId") id: Long
    ): Single<Season>

    @GET("shows/{showId}/seasons")
    fun getSeasons(
        @Path("showId") showId: Long,
        @Query("page") page: Int? = 1
    ): Single<List<Season>>

    @GET("shows/{showId}")
    fun getShow(
        @Path("showId") id: Long
    ): Single<Show>

    @GET("shows")
    fun getShows(
        @Query("page") page: Int? = 1,
        @Query("q") query: String? = null
    ): Single<List<Show>>


    @GET("search/shows")
    fun searchShows(
        @Query("q") name: String,
        @Query("page") page: Int? = 1
    ): Single<List<Search>>

}
