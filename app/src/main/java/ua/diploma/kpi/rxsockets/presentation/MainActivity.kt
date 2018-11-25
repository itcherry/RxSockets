package ua.diploma.kpi.rxsockets.presentation

import android.os.Bundle
import com.jakewharton.rxbinding2.view.RxView
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ua.diploma.kpi.rxsockets.R
import ua.diploma.kpi.rxsockets.domain.model.TemperatureHumidityData
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity(), MainActivityContract.View {
    @Inject
    lateinit var presenter: MainActivityPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        presenter.model = TemperatureHumidityData(0.0, 0.0)
        setClickListeners()
    }

    override fun onResume() {
        super.onResume()
        presenter.bindView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.unbindView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    private fun setClickListeners() {
        presenter.enableLightObservable(
                RxView.clicks(btnEnableLight)
                        .debounce(200L, TimeUnit.MILLISECONDS)
        )

        presenter.disableLightObservable(
                RxView.clicks(btnDisableLight)
                        .debounce(200L, TimeUnit.MILLISECONDS)
        )
    }

    override fun showTemperatureHumidity(temperatureHumidityData: TemperatureHumidityData) {
        tvTemperatureValue.text = temperatureHumidityData.temperature.toString()
        tvHumidityValue.text = temperatureHumidityData.humidity.toString()
    }

    override fun showSocketConnected() {
        tvSocketState.text = getString(R.string.socket_state_connected)
    }

    override fun showSocketDisconnected() {
        tvSocketState.text = getString(R.string.socket_state_disconnected)
    }

    override fun showSocketConnecting() {
        tvSocketState.text = getString(R.string.socket_state_connecting)
    }

    override fun showSocketError() {
        tvSocketState.text = getString(R.string.socket_state_error)
    }

    override fun showSocketReconnecting() {
        tvSocketState.text = getString(R.string.socket_state_reconnecting)
    }
}
