package com.test.demo

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen
import com.test.demo.data.dataModule
import com.test.demo.data.networkModule
import com.test.demo.features.login.loginModule
import com.test.demo.features.main.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class DemoApp: Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        AndroidThreeTen.init(this)

        startKoin {
            androidContext(this@DemoApp)
            modules(
                networkModule,
                dataModule,
                mainModule,
                loginModule
            )
        }
    }
}