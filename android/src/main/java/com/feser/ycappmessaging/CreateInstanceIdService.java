package com.feser.ycappmessaging;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class CreateInstanceIdService extends IntentService {

    public CreateInstanceIdService() {
        super("CreateInstanceIdService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CreateInstanceIdS", e.getMessage());
                    }
                })
                .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        Log.d("CreateInstanceIdS", "Token " + instanceIdResult.getToken());
                        Log.d("CreateInstanceIdS", "ID " + instanceIdResult.getId());
                    }
                });
    }

}
