package ua.diploma.kpi.rxsockets.data.model

import com.google.gson.annotations.SerializedName
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData

data class TemperatureHumidityDTO(@SerializedName("temperature") val temperature: Double,
                                  @SerializedName("humidity") val humidity: Double){
    fun mapToEntity() =
            TemperatureHumidityData(temperature, humidity)
}