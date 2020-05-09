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
import com.jpventura.data.BuildConfig
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.TestScheduler
import okhttp3.OkHttpClient
import org.junit.After
import org.junit.Test

import org.junit.Before
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class TVMazeClientTest : KodeinAware {

    private val client: TVMazeClient by instance()
    private val testScheduler: TestScheduler by instance()

    override val kodein = Kodein.lazy {
        bind<Gson>() with singleton {
            Gson()
        }

        bind<GsonConverterFactory>() with singleton {
            GsonConverterFactory.create(instance())
        }

        bind<OkHttpClient>() with singleton {
            OkHttpClient.Builder().build()
        }

        bind<TestScheduler>() with singleton {
            TestScheduler()
        }

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

    @Before
    fun setUp() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @After
    fun tearDown() {
        RxJavaPlugins.setIoSchedulerHandler { testScheduler }
        RxJavaPlugins.setComputationSchedulerHandler { testScheduler }
    }

    @Test
    fun `should find one show episode`() {
        client.getEpisode(id = 1)
            .test()
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should find show episodes`() {
        client.getEpisodes(showId = 250, page = 1)
            .test()
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should find show seasons`() {
        client.getSeasons(showId = 250, page = 1)
            .test()
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should find one show season`() {
        client.getSeason(id = 1)
            .test()
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should find shows`() {
        client.getShows(page = 1)
            .test()
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

    @Test
    fun `should find one show`() {
        client.getShow(id = 250)
            .test()
            .assertComplete()
            .assertNoErrors()
            .dispose()
    }

}
