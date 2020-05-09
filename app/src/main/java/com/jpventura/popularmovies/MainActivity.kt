package com.jpventura.popularmovies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jpventura.popularmovies.series.SeriesFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, SeriesFragment.newInstance())
                    .commitNow()
        }
    }
}
