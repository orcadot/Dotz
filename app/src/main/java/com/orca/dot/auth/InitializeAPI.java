package com.orca.dot.auth;

import android.content.Context;
import android.provider.Settings;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.orca.dot.backend.dotApi.DotApi;

import java.io.IOException;

/**
 * Created by dot on 28/7/16.
 */
public class InitializeAPI {

    public static DotApi dotApiService;
    public static Context mContext;

    public static void init(Context context) {
        if (dotApiService == null) {
            mContext = context;
            DotApi.Builder builder = new DotApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    .setRootUrl("https://dotz-2f65f.appspot.com/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver
            dotApiService = builder.build();
        }
    }

    public static String getDeviceId() {
        return Settings.Secure.getString(mContext.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
}
