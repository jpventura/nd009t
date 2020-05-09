package com.jpventura.core.android.di

import android.content.Context
import androidx.fragment.app.FragmentActivity
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider

/**
 * Activity scope. All activity modules can depend on this one, which will bring the activity
 * context into scope. Any dependencies that are just needed during the lifecycle of an activity
 * are prone to be here. Interactors for example, or the Router since it requires an activity
 * context.
 */
fun baseActivityModule(
    activity: FragmentActivity
) = Kodein.Module("activityModule") {
    bind<Context>(overrides = true) with provider { activity }
    bind<FragmentActivity>() with provider { activity }
}
