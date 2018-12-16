package ua.andrii.chernysh.rxsockets.domain.interactor.lights

import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import ua.andrii.chernysh.rxsockets.domain.ObservableUseCase
import ua.andrii.chernysh.rxsockets.domain.repository.LightsRepository
import javax.inject.Inject

class LightsUseCase @Inject constructor(observableTransformer: ObservableTransformer<Any, Any>,
                                       compositeDisposable: CompositeDisposable,
                                       private val lightsRepository: LightsRepository):
        ObservableUseCase<Any, Boolean>(observableTransformer, compositeDisposable){

    override fun buildUseCaseObservable(params: Boolean) = lightsRepository.setLight(params)
}