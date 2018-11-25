package ua.diploma.kpi.rxsockets.data.repository

import ua.diploma.kpi.rxsockets.data.source.LightsDataSource
import ua.diploma.kpi.rxsockets.domain.repository.LightsRepository
import javax.inject.Inject

class LightsRepositoryImpl  @Inject constructor(
        private val lightsDataSource: LightsDataSource
) : SocketRepositoryImpl(lightsDataSource), LightsRepository {

    override fun setLight(isOn: Boolean) = lightsDataSource.setLights(isOn)
}