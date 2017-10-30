package com.example.android.weathernow.dagger.component;

import android.app.Application;

import com.example.android.weathernow.dagger.module.AppModule;
import com.example.android.weathernow.dagger.module.MainActivityModule;
import com.example.android.weathernow.view.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

/**
 * Created by Gauri Gadkari on 10/28/17.
 */

@Singleton
@Component(modules = {
        AndroidInjectionModule.class,
        AppModule.class,
        MainActivityModule.class
})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);

        AppComponent build();
    }

    void inject(App app);
}
