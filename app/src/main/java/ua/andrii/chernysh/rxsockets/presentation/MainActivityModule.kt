package ua.andrii.chernysh.rxsockets.presentation

import android.content.Context
import dagger.Binds
import dagger.Module
import ua.andrii.chernysh.rxsockets.di.qualifier.ActivityContext
import ua.andrii.chernysh.rxsockets.di.scope.ActivityScope

@Module
interface MainActivityModule {
    @ActivityScope
    @Binds
    fun bindGamePresenter(mainActivityPresenter: MainActivityPresenter): MainActivityContract.Presenter

    @ActivityContext
    @Binds
    fun bindActivityContext(mainActivity: MainActivity): Context
}