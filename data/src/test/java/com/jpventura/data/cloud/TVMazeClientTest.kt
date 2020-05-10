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

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jpventura.data.BuildConfig
import com.jpventura.data.cloud.entity.Episode
import com.jpventura.data.cloud.entity.Season
import com.jpventura.data.cloud.entity.Show
import com.jpventura.data.di.dataModule
import com.jpventura.data.ktx.fromJson
import com.jpventura.data.ktx.getResourceAsStream
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.InputStream

class TVMazeClientTest : KodeinAware {

    override val kodein = Kodein.lazy {
        import(dataModule())

        bind<TVMazeClient>() with singleton {
            Retrofit.Builder()
                .baseUrl(BuildConfig.TV_MAZE_API_URL)
                .client(instance())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(instance())
                .build()
                .create(TVMazeClient::class.java)
        }
    }

    private val client by instance<TVMazeClient>()
    private val gson by instance<Gson>()
    private val testScheduler by instance<TestScheduler>()

    private lateinit var episodes: List<Episode>
    private lateinit var seasons: List<Season>
    private lateinit var shows: List<Show>

    @Before
    @Throws(IllegalArgumentException::class)
    fun setUp() {
        episodes = gson.fromJson(
            getResourceAsStream(FILE_EPISODES),
            object : TypeToken<List<Episode>>(){}.type
        )

        seasons = gson.fromJson(
            getResourceAsStream(FILE_SEASONS),
            object : TypeToken<List<Season>>(){}.type
        )

        shows = gson.fromJson(
            getResourceAsStream(FILE_SHOWS),
            object : TypeToken<List<Show>>(){}.type
        )

        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.setIoSchedulerHandler { null }
        RxJavaPlugins.setComputationSchedulerHandler { null }
    }

    @Test
    fun `should find one show episode`() {
        val got = episodes.first()

        client.getEpisode(id = got.id)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(got)
            .dispose()
    }

    @Test
    fun `should find show episodes`() {
        client.getEpisodes(showId = GAME_OF_THRONES_ID)
            .toObservable()
            .flatMapIterable { it }
            .firstOrError()
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(episodes.first())
            .dispose()
    }

    @Test
    fun `should find show seasons`() {
        client.getSeasons(showId = GAME_OF_THRONES_ID)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(seasons)
            .dispose()
    }

    @Test
    fun `should find one show season`() {
        val got = seasons.first()

        client.getSeason(id = got.id)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(got)
            .dispose()
    }

    @Test
    fun `should find shows`() {
        client.getShows(page = 1)
            .toObservable()
            .flatMapIterable { it }
            .take(10)
            .toList()
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(shows.subList(0, 10))
            .dispose()
    }

    @Test
    fun `should find one show`() {
        client.getShow(id = 250)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(shows.first())
            .dispose()
    }

    @Test
    fun `should find shows by name`() {
        client.searchShows(page = 1, name = shows.first().name)
            .toObservable()
            .flatMapIterable { it }
            .sorted { a, b -> a.score.compareTo(b.score) }
            .firstOrError()
            .map { it.show }
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(shows.first())
            .dispose()
    }

    companion object {

        private const val FILE_EPISODES = "episodes.json"
        private const val FILE_SEASONS = "seasons.json"
        private const val FILE_SHOWS = "shows.json"

        private const val GAME_OF_THRONES_ID = 82L

    }

}
