package com.jpventura.popularmovies

import android.os.Bundle
import com.jpventura.popularmovies.app.ui.InjectedActivity
import com.jpventura.popularmovies.series.ui.SeriesFragment
import org.kodein.di.Kodein

class MainActivity : InjectedActivity() {

    override fun activityModule() = Kodein.Module("mainModule") {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, SeriesFragment.newInstance(), SeriesFragment.TAG)
                .commit()
        }
    }

}
