package com.jpventura.popularmovies.app.ui

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.jpventura.core.android.di.baseActivityModule
import com.jpventura.popularmovies.app.KApplication
import com.jpventura.popularmovies.app.di.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

abstract class InjectedActivity : AppCompatActivity(), KodeinAware {

    // kodein() automatically fetches app Kodein scope.
    private val appKodein by kodein()

    override val kodein: Kodein by retainedKodein {
        extend(appKodein)
        import(baseActivityModule(this@InjectedActivity), allowOverride = true)
        importOnce(activityModule())
        bind<ViewModelProvider.Factory>() with singleton { ViewModelFactory(kodein.direct) }
        (app().overrideBindings)()
    }

    /**
     * Optional to override, if your activity needs specific DI.
     */
    open fun activityModule() = Kodein.Module("activityModule", allowSilentOverride = true) {
    }
}

fun Activity.app() = applicationContext as KApplication
