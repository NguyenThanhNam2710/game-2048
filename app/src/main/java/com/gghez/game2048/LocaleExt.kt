package com.gghez.game2048

import android.content.Context
import android.content.res.Configuration
import java.util.Locale

fun Context.withLocale(tag: String): Context {
    val locale = Locale(tag)
    Locale.setDefault(locale)
    val config = Configuration(resources.configuration)
    config.setLocale(locale)
    return createConfigurationContext(config)
}