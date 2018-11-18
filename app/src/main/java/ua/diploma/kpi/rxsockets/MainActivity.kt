package ua.diploma.kpi.rxsockets

import android.R
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ua.diploma.kpi.kotlinrxsockets.socket.createRxSocket

class MainActivity : AppCompatActivity() {
    private val socket = createRxSocket {
        hostIp = "http://176.36.146.229"
        port = 9092
        gson = Gson()
        namespace = "DHT22"
        options {
            forceNew = false
            reconnection = true
            reconnectionAttempts = 3
            reconnectionDelay = 1000
            reconnectionDelayMax = 10000
            randomizationFactor = 0.3
            timeout = 5000
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        socket.observableOnConnect()
                .subscribe {
                    Log.d("TAG", "Socket connected")
                    socket.sendData("checkLogin", "andrii")
                    socket.sendData("checkLogin", "bzdo")
                    socket.sendData("checkLogin", "dsafsdfasdfasdf")
                }

        socket.observableOnConnectError()
                .subscribe {
                    Log.d("TAG", "Socket connect error: $it")
                }

        socket.observableOnError()
                .subscribe {
                    Log.d("TAG", "Socket error: $it")
                }

        socket.observableOnDisconnect()
                .subscribe {
                    Log.d("TAG", "Socket disconnected")
                }

        socket.observableOn("loginResult", HashMap::class.java)
                .subscribe {
                    Log.d("TAG", "Login result: $it")
                }

        socket.sendData("Diploma", "NTUU KPI")

        socket.connect()
    }

    override fun onDestroy() {
        super.onDestroy()
        socket.disconnect()
    }
}
