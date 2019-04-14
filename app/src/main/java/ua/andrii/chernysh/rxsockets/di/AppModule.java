package ua.andrii.chernysh.rxsockets.di;
/**
 * Copyright 2018. Andrii Chernysh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.support.DaggerAppCompatActivity;
import ua.andrii.chernysh.rxsockets.DiplomaApplication;
import ua.andrii.chernysh.rxsockets.data.network.SocketServiceModule;
import ua.andrii.chernysh.rxsockets.di.qualifier.ApplicationContext;
import ua.andrii.chernysh.rxsockets.presentation.MainActivity;

/**
 * Dagger 2 module for DiplomaApplication
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
@Module(includes = {SocketServiceModule.class, ActivityBindingModule.class})
public abstract class AppModule {
    @Provides
    public static Application provideApplication(DiplomaApplication diplomaApplication) {
        return diplomaApplication;
    }

    @Binds
    @ApplicationContext
    public abstract Context bindContext(DiplomaApplication diplomaApplication);

    @Binds
    public abstract DaggerAppCompatActivity bindBaseActivity(MainActivity mainActivity);
}
