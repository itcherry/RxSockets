package ua.diploma.kpi.rxsockets.presentation

import android.content.Context
import dagger.Binds
import dagger.Module
import ua.diploma.kpi.rxsockets.di.qualifier.ActivityContext
import ua.diploma.kpi.rxsockets.di.scope.ActivityScope

@Module
interface MainActivityModule {
    @ActivityScope
    @Binds
    fun bindGamePresenter(mainActivityPresenter: MainActivityPresenter): MainActivityContract.Presenter

    @ActivityContext
    @Binds
    fun bindActivityContext(mainActivity: MainActivity): Context
}