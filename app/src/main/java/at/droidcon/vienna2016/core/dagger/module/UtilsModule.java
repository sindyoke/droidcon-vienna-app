package at.droidcon.vienna2016.core.dagger.module;

import android.app.Application;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import javax.inject.Singleton;

import at.droidcon.vienna2016.core.firebase.FirebaseAnalyticsWrapper;
import at.droidcon.vienna2016.core.firebase.FirebaseConfiguration;
import at.droidcon.vienna2016.utils.Analytics;
import at.droidcon.vienna2016.utils.Configuration;
import dagger.Module;
import dagger.Provides;

/**
 * Created by helmuth on 19/08/16.
 */

@Module
public final class UtilsModule {
    @Provides @Singleton
    Configuration provideConfiguration() {
        return new FirebaseConfiguration(
                FirebaseRemoteConfig.getInstance());
    }

    @Provides @Singleton
    Analytics provideAnalytics(Application context) {
        return new FirebaseAnalyticsWrapper(
                FirebaseAnalytics.getInstance(context)
        );
    }

    @Provides @Singleton
    DatabaseReference provideDatabaseReference() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Provides @Singleton
    FirebaseInstanceId provideFirebaseInstanceIc() {
        return FirebaseInstanceId.getInstance();
    }
}
