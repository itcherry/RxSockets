package ua.diploma.kpi.rxsockets.data.network

/**
 * Copyright 2018. Andrii Chernysh
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import timber.log.Timber
import ua.diploma.kpi.kotlinrxsockets.socket.RxSocket
import ua.diploma.kpi.kotlinrxsockets.socket.SocketLoggingInterceptor
import ua.diploma.kpi.kotlinrxsockets.socket.createRxSocket
import ua.diploma.kpi.rxsockets.di.scope.ApplicationScope

/**
 * Module that provides all API and socket services
 * and Retrofit instances
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
@Module
class SocketServiceModule {
    @Provides
    @ApplicationScope
    fun provideSocket(gsonProvided: Gson, loggingInterceptor: SocketLoggingInterceptor): RxSocket {
        return createRxSocket {
            hostIp = "http://176.36.81.205"
            port = 9092
            namespace = "raspberry"
            socketLoggingInterceptor = loggingInterceptor
            gson = gsonProvided
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
    }

    @Provides
    @ApplicationScope
    fun provideGson(): Gson {
        return GsonBuilder()
                .setLenient()
                .create()
    }

    @Provides
    @ApplicationScope
    fun provideSocketLoggingInterceptor(): SocketLoggingInterceptor {
        return object : SocketLoggingInterceptor {
            override fun logInfo(message: String) {
                Timber.i(message)
            }

            override fun logError(message: String) {
                Timber.e(message)
            }
        }
    }
}
