package com.easysteps.network;

import com.easysteps.activity.LoginActivity;
import com.easysteps.helper.UtilsModule;
import com.google.android.datatransport.runtime.dagger.Component;

import javax.inject.Singleton;

/**
 * Created by NIKUNJ
 */
@Singleton
@Component(modules = {AppModule.class, UtilsModule.class})
public interface AppComponent {

    void doInjection(LoginActivity loginActivity);

}
