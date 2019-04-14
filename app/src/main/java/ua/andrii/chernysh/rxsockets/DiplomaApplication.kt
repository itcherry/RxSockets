package ua.andrii.chernysh.rxsockets

import android.support.v7.app.AppCompatDelegate
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber
import ua.andrii.chernysh.rxsockets.di.component.DaggerAppComponent

class DiplomaApplication: DaggerApplication(){
    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        initThirdParties()
    }

    private fun initThirdParties() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant()
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }

}