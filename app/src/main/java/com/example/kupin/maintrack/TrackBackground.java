package com.example.kupin.maintrack;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class TrackBackground extends Service {
    public TrackBackground() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
