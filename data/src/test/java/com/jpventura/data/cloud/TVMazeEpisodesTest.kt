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
import com.jpventura.data.cloud.entity.Episode as EpisodeEntity
import com.jpventura.data.di.dataModule
import com.jpventura.data.ktx.asBean
import com.jpventura.data.ktx.fromJson
import com.jpventura.data.ktx.getResourceAsStream
import com.jpventura.domain.bean.Episode
import com.jpventura.domain.bean.schedule
import com.jpventura.domain.bean.show
import com.jpventura.domain.model.TelevisionSeriesModel
import io.reactivex.Single
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

class TVMazeEpisodesTest : KodeinAware {

    override val kodein = Kodein.lazy {
        import(dataModule())

        bind<TVMazeClient>(tag = "mock") with singleton {
            `when`(client.getEpisodes(showId = GOT_ID)).thenReturn(Single.just(episodes.toList()))
            `when`(client.getEpisode(id = 4952)).thenReturn(Single.just(episodes.first()))
            client
        }

        bind<TelevisionSeriesModel.Episodes>(tag = "cloud") with singleton {
            TVMazeEpisodes(client = instance(tag = "mock"))
        }
    }

    private val client = mock(TVMazeClient::class.java)
    private val gson by instance<Gson>()
    private val model by instance<TelevisionSeriesModel.Episodes>(tag = "cloud")
    private val testScheduler by instance<TestScheduler>()

    private lateinit var episodes: List<EpisodeEntity>

    @Before
    fun setUp() {
        episodes = gson.fromJson(
            getResourceAsStream(FILE_EPISODES),
            object : TypeToken<List<com.jpventura.data.cloud.entity.Episode>>(){}.type
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
        val episode: Episode = episodes.map { it.asBean(parentId = GOT_ID) }.first()

        model.findOne(parentId = GOT_ID, id = episode.key)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(episode)
            .dispose()
    }

    @Test
    fun `should find show episodes`() {
        model.find(GOT)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(episodes.map { it.asBean(GOT_ID) })
            .dispose()
    }

    companion object {
        private const val FILE_EPISODES = "episodes.json"
        private const val GOT_ID = 82L

        private  val GOT = show {
            id = 82L
            genres = listOf("Drama", "Adventure", "Fantasy")
            name = "Game of Thrones"
            poster = "http://static.tvmaze.com/uploads/images/medium_portrait/190/476117.jpg"
            schedule = schedule {
                days = listOf("Sunday")
                time = "21:00"
            }
            summary = "Disturbing season finale"
        }
    }

}
