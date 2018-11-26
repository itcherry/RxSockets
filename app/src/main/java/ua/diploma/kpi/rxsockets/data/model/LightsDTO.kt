package ua.diploma.kpi.rxsockets.data.model

import com.google.gson.annotations.SerializedName

data class LightsDTO(@SerializedName("isOn") val isOn: Boolean)