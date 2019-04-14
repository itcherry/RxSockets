package ua.andrii.chernysh.rxsockets.di.component;
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

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import ua.andrii.chernysh.rxsockets.DiplomaApplication;
import ua.andrii.chernysh.rxsockets.data.repository.RepositoryModule;
import ua.andrii.chernysh.rxsockets.di.AppModule;
import ua.andrii.chernysh.rxsockets.di.RxModule;
import ua.andrii.chernysh.rxsockets.di.scope.ApplicationScope;

/**
 * Component for Dagger 2 in order to create
 * Application level graph.
 *
 * @author Andrii Chernysh. E-mail: itcherry97@gmail.com
 */
@ApplicationScope
@Component(modules = {
        AndroidSupportInjectionModule.class,
        AppModule.class,
        RepositoryModule.class,
        RxModule.class})
public interface AppComponent extends AndroidInjector<DiplomaApplication> {
    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<DiplomaApplication> {
    }
}
