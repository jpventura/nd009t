package com.jpventura.core.android.di

import com.jpventura.core.android.lifecycle.ObservableViewModel
import org.kodein.di.Kodein
import org.kodein.di.generic.bind

inline fun <reified T : ObservableViewModel> Kodein.Builder.bindViewModel(
    overrides: Boolean? = null
): Kodein.Builder.TypeBinder<T> {
    return bind<T>(T::class.java.simpleName, overrides)
}
