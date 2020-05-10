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

package com.jpventura.popularmovies.app.di

import android.content.Context
import com.google.gson.Gson
import com.jpventura.data.BuildConfig
import com.jpventura.data.TelevisionEpisodeRepository
import com.jpventura.data.TelevisionSeriesRepository
import com.jpventura.data.cache.CacheEpisodeModel
import com.jpventura.data.cache.CacheSeriesModel
import com.jpventura.data.cloud.TVMazeClient
import com.jpventura.data.cloud.TVMazeEpisodes
import com.jpventura.data.cloud.TVMazeSeries
import com.jpventura.domain.model.TelevisionSeriesModel
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun applicationModule(context: Context) = Kodein.Module("applicationModule") {
    import(applicationModels(context))
}

private fun applicationModels(context: Context) = Kodein.Module("applicationModels") {
    bind<Context>() with provider {
        context
    }

    bind<Gson>() with singleton {
        Gson()
    }

    bind<GsonConverterFactory>() with singleton {
        GsonConverterFactory.create(instance())
    }

    bind<OkHttpClient>() with singleton {
        OkHttpClient.Builder().build()
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

    bind<TelevisionSeriesModel.Episodes>(tag = "cache") with singleton {
        CacheEpisodeModel()
    }

    bind<TelevisionSeriesModel.Episodes>(tag = "cloud") with singleton {
        TVMazeEpisodes(instance())
    }

    bind<TelevisionSeriesModel.Episodes>() with singleton {
        TelevisionEpisodeRepository(cache = instance("cache"), cloud = instance(tag = "cloud"))
    }

    bind<TelevisionSeriesModel.Series>(tag = "cache") with singleton {
        CacheSeriesModel()
    }

    bind<TelevisionSeriesModel.Series>(tag = "cloud") with singleton {
        TVMazeSeries(instance())
    }

    bind<TelevisionSeriesModel.Series>() with singleton {
        TelevisionSeriesRepository(cache = instance(tag = "cache"), cloud = instance(tag = "cloud"))
    }

}
