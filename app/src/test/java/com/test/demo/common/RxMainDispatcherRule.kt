package com.test.demo.common

import io.reactivex.rxjava3.android.plugins.RxAndroidPlugins
import io.reactivex.rxjava3.plugins.RxJavaPlugins
import io.reactivex.rxjava3.schedulers.Schedulers
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RxMainDispatcherRule: TestWatcher() {

    override fun starting(description: Description?) {
        super.starting(description)
        RxJavaPlugins.setIoSchedulerHandler { s -> Schedulers.trampoline() }
        RxJavaPlugins.setComputationSchedulerHandler { s -> Schedulers.trampoline() }

        RxAndroidPlugins.setInitMainThreadSchedulerHandler { s -> Schedulers.trampoline() }
        RxAndroidPlugins.setMainThreadSchedulerHandler { s -> Schedulers.trampoline() }
    }

    override fun finished(description: Description?) {
        super.finished(description)
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }
}