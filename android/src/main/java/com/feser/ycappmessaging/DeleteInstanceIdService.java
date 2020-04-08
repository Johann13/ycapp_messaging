package com.feser.ycappmessaging;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;
import com.feser.ycapp_foundation.prefs.Prefs;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class DeleteInstanceIdService extends IntentService {

    public DeleteInstanceIdService() {
        super("DeleteInstanceIdService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("DeleteInstanceIdS", " unsub enqueue");
        Prefs prefs = new Prefs(this);
        List<String> creator = prefs.getCreator();
        List<String> twitch = prefs.getTwitch();
        List<String> youtube = prefs.getYoutube();
        for (String c : creator) {
            FirebaseMessaging
                    .getInstance()
                    .unsubscribeFromTopic(c);
        }
        for (String t : twitch) {
            FirebaseMessaging
                    .getInstance()
                    .unsubscribeFromTopic(t);
        }
        for (String y : youtube) {
            FirebaseMessaging
                    .getInstance()
                    .unsubscribeFromTopic(y);
        }
        prefs.destroy();
        Log.d("DeleteInstanceIdS", " unsub done");
        try {
            // Resets Instance ID and revokes all tokens.
            Log.d("DeleteInstanceIdS", "delete");
            FirebaseInstanceId.getInstance().deleteInstanceId();
            Log.d("DeleteInstanceIdS", "delete done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
