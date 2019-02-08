package io.bricks.cache.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import io.bricks.cache.core.CachedProperty
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import kotlin.reflect.KClass

val cachedPropertyModule = Kodein.Module("cachedPropertyModule") {
    bind<SharedPreferences>() with factory { type: KClass<*> ->
        SharedPreferencesFactoryImpl(instance()).invoke(type)
    }
    bind<CachedProperty.Factory>() with singleton { SharedPreferencesCachedProperty.Factory(factory(), instance()) }
}

internal class SharedPreferencesFactoryImpl(
    private val context: Context
) : SharedPreferencesFactory {

    override fun invoke(type: KClass<*>) = context
        .getSharedPreferences(type.java.name, Context.MODE_PRIVATE)!!
}

typealias SharedPreferencesFactory = (KClass<*>) -> SharedPreferences