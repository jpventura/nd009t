package com.jpventura.data.di

import com.google.gson.Gson
import io.reactivex.schedulers.TestScheduler
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.converter.gson.GsonConverterFactory

fun dataModule() = Kodein.Module("dataModule") {
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
}
