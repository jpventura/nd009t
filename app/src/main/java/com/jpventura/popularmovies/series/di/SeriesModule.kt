package com.jpventura.popularmovies.series.di

import com.jpventura.data.TelevisionEpisodeRepository
import com.jpventura.data.TelevisionSeriesRepository
import com.jpventura.domain.model.TelevisionSeriesModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

fun seriesModule() = Kodein.Module("SeriesModule") {

}
