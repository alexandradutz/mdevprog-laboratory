package com.example.dutzi.snowy;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by dutzi on 12.01.2017.
 */

public class FirebaseNotification extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("NEW TOK", "Refreshed token: " + refreshedToken);
//        sendRegistrationToServer(refreshedToken);
    }
}
