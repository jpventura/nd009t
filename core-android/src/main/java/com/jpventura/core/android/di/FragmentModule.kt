package com.jpventura.core.android.di

import androidx.fragment.app.Fragment
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

/**
 * Fragment scope. All activity modules can depend on this one, which will bring the fragment
 * context into scope. Any dependencies that are just needed during the lifecycle of an fragment
 * are prone to be here. Interactors for example, or the Router since it requires a fragment
 * context.
 */
fun baseFragmentModule(fragment: Fragment) = Kodein.Module("FragmentModule") {
    bind<Fragment>() with provider { fragment }
}
