package com.jpventura.data.cloud

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jpventura.data.cloud.entity.Search
import com.jpventura.data.cloud.entity.Show as Series
import com.jpventura.data.di.dataModule
import com.jpventura.data.ktx.asBean
import com.jpventura.data.ktx.fromJson
import com.jpventura.data.ktx.getResourceAsStream
import com.jpventura.domain.bean.Show
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
import org.mockito.Mockito

class TVMazeSeriesTest : KodeinAware {

    override val kodein = Kodein.lazy {
        import(dataModule())

        bind<TVMazeClient>(tag = "mock") with singleton {
            Mockito.`when`(client.getShow(id = 250))
                .thenReturn(Single.just(series.first()))

            Mockito.`when`(client.getShows(page = 1))
                .thenReturn(Single.just(series))


            val search = series.map {
                Search(score = if (it.name == SERIES_NAME) 15.0 else 0.0, show = it)
            }

            Mockito.`when`(client.searchShows(name = SERIES_NAME))
                .thenReturn(Single.just(search))

            client
        }

        bind<TelevisionSeriesModel.Series>(tag = "cloud") with singleton {
            TVMazeSeries(client = instance(tag = "mock"))
        }
    }

    private val client = Mockito.mock(TVMazeClient::class.java)
    private val gson by instance<Gson>()
    private val model by instance<TelevisionSeriesModel.Series>(tag = "cloud")
    private val testScheduler by instance<TestScheduler>()

    private lateinit var series: List<Series>
    private lateinit var shows: List<Show>

    @Before
    fun setUp() {
        series = gson.fromJson(
            getResourceAsStream(FILE_SERIES),
            object : TypeToken<List<Series>>() {}.type
        )

        shows = series.map { it.asBean() }

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
        val show: Show = series.map { it.asBean() }.first()

        model.findOne(key = show.key)
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(show)
            .dispose()
    }

    @Test
    fun `should find shows`() {
        model.find()
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(shows)
            .dispose()
    }

    @Test
    fun `should find shows by name`() {
        model.find(query = mapOf("name" to SERIES_NAME, "page" to 1))
            .firstOrError()
            .map { it.toSet() }
            .test()
            .assertComplete()
            .assertNoErrors()
            .assertValue(shows.toSet())
            .dispose()
    }

    companion object {
        private const val FILE_SERIES = "shows.json"
        private const val SERIES_NAME = "Kirby Buckets"
    }

}
