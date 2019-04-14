package ua.andrii.chernysh.rxsockets.data.repository

import ua.andrii.chernysh.rxsockets.data.source.LightsDataSource
import ua.andrii.chernysh.rxsockets.domain.repository.LightsRepository
import javax.inject.Inject

class LightsRepositoryImpl  @Inject constructor(
        private val lightsDataSource: LightsDataSource
) : SocketRepositoryImpl(lightsDataSource), LightsRepository {

    override fun setLight(isOn: Boolean) = lightsDataSource.setLights(isOn)
}