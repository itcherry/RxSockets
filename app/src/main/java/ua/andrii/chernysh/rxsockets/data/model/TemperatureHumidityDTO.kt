package ua.andrii.chernysh.rxsockets.data.model

import com.google.gson.annotations.SerializedName
import ua.andrii.chernysh.rxsockets.domain.model.TemperatureHumidityData

data class TemperatureHumidityDTO(@SerializedName("temperature") val temperature: Double,
                                  @SerializedName("humidity") val humidity: Double){
    fun mapToEntity() =
            TemperatureHumidityData(temperature, humidity)
}