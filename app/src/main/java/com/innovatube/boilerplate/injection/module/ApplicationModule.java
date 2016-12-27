package com.innovatube.boilerplate.injection.module;

import android.app.Application;
import android.content.Context;

import com.innovatube.boilerplate.data.InnovatubeRepository;
import com.innovatube.boilerplate.data.InnovatubeRepositoryImpl;
import com.innovatube.boilerplate.data.local.LocalDataSource;
import com.innovatube.boilerplate.data.local.LocalDataSourceImpl;
import com.innovatube.boilerplate.data.local.PreferenceDataSource;
import com.innovatube.boilerplate.data.local.PreferenceDataSourceImpl;
import com.innovatube.boilerplate.data.prefs.UserPrefs;
import com.innovatube.boilerplate.data.remote.InnovatubeService;
import com.innovatube.boilerplate.data.remote.RemoteDataSource;
import com.innovatube.boilerplate.data.remote.RemoteDataSourceImpl;
import com.innovatube.boilerplate.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import retrofit2.Retrofit;

/**
 * Created by TOIDV on 4/4/2016.
 */

@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    InnovatubeService provideInnovatubeService(Retrofit retrofit) {
        return retrofit.create(InnovatubeService.class);
    }

    @Provides
    LocalDataSource provideRealmHelper() {
        return new LocalDataSourceImpl();
    }

    @Provides
    @Singleton
    Retrofit provideRetrofitInstance() {
        return InnovatubeService.Creator.newRetrofitInstance();
    }

    @Provides
    @Singleton
    Realm provideRealm() {
        return Realm.getDefaultInstance();
    }


    @Provides
    @Singleton
    RemoteDataSource provideRemoteDataSource(InnovatubeService innovatubeService) {
        return new RemoteDataSourceImpl(innovatubeService);
    }

    @Provides
    @Singleton
    UserPrefs provideUserPrefs( @ApplicationContext Context context) {
        return new UserPrefs(context);
    }

    @Provides
    @Singleton
    PreferenceDataSource providePreferenceDataSource(UserPrefs userPrefs) {
        return new PreferenceDataSourceImpl(userPrefs);
    }

    @Provides
    @Singleton
    InnovatubeRepository provideInnovatubeRepository(RemoteDataSource remoteDataSource,
                                                     PreferenceDataSource preferenceDataSource,
                                                     LocalDataSource localDataSource) {
        return new InnovatubeRepositoryImpl(remoteDataSource,
                preferenceDataSource, localDataSource);
    }
}
