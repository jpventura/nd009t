package com.jpventura.popularmovies.app.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.jpventura.core.android.di.baseFragmentModule
import com.jpventura.popularmovies.app.di.ViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.direct
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

abstract class InjectedFragment : Fragment(), KodeinAware {

    private val activityKodein by kodein()

    override val kodein = Kodein.lazy {
        extend(activityKodein)
        import(baseFragmentModule(this@InjectedFragment), allowOverride = true)
        import(fragmentModule())
        bind<ViewModelProvider.Factory>(overrides = true) with singleton {
            ViewModelFactory(kodein.direct)
        }
        (app().overrideBindings)()
    }

    /**
     * Optional to override, if your fragment needs specific DI.
     */
    open fun fragmentModule() = Kodein.Module("fragmentModule") {
    }
}

inline fun <reified T : InjectedFragment> newInstanceOf(
    arguments: Bundle? = null,
    initBlock: (T.() -> Unit) = {}
): T {
    val fragment = T::class.java.newInstance()
    fragment.arguments = arguments
    fragment.apply(initBlock)
    return fragment
}

fun Fragment.activity() = requireActivity() as InjectedActivity
fun Fragment.app() = requireActivity().app()
