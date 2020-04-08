package com.feser.ycappmessaging;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.ListenableWorker;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.feser.ycapp_foundation.prefs.Prefs;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class SubscribeAllWorker extends Worker {

    public SubscribeAllWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void enqueue(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        OneTimeWorkRequest workRequest =
                new OneTimeWorkRequest.Builder(SubscribeAllWorker.class)
                        .setConstraints(constraints)
                        .build();
        WorkManager.getInstance(context).enqueue(workRequest);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {

        Prefs prefs = new Prefs(getApplicationContext());

        List<String> creator = prefs.getCreator();
        List<String> twitch = prefs.getTwitch();
        List<String> youtube = prefs.getYoutube();

        FirebaseMessaging firebaseMessaging =
                FirebaseMessaging
                        .getInstance();
        for (String c : creator) {
            firebaseMessaging
                    .subscribeToTopic(c);
        }
        for (String t : twitch) {
            firebaseMessaging
                    .subscribeToTopic(t);
        }
        for (String y : youtube) {
            firebaseMessaging
                    .subscribeToTopic(y);
        }
        firebaseMessaging
                .subscribeToTopic("all");
        prefs.destroy();

        return ListenableWorker.Result.success();
    }
}
