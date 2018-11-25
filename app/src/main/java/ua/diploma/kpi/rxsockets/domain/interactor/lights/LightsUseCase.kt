package ua.diploma.kpi.rxsockets.domain.interactor.lights

import io.reactivex.ObservableTransformer
import io.reactivex.disposables.CompositeDisposable
import ua.diploma.kpi.rxsockets.domain.ObservableUseCase
import ua.diploma.kpi.rxsockets.domain.repository.LightsRepository
import javax.inject.Inject

class LightsUseCase @Inject constructor(observableTransformer: ObservableTransformer<Any, Any>,
                                       compositeDisposable: CompositeDisposable,
                                       private val lightsRepository: LightsRepository):
        ObservableUseCase<Any, Boolean>(observableTransformer, compositeDisposable){

    override fun buildUseCaseObservable(params: Boolean) = lightsRepository.setLight(params)
}