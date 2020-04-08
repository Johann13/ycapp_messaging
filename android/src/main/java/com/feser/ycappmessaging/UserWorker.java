package com.feser.ycappmessaging;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.feser.ycapp_foundation.prefs.Prefs;
import com.google.gson.Gson;

import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserWorker extends Worker {


    public UserWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    public static void enqueue(Context context) {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .build();
        OneTimeWorkRequest analyticsWork =
                new OneTimeWorkRequest.Builder(UserWorker.class)
                        .setConstraints(constraints)
                        .build();
        WorkManager.getInstance(context).enqueue(analyticsWork);
    }

    @NonNull
    @Override
    public Result doWork() {

        Prefs prefs = new Prefs(getApplicationContext());
        String token = prefs.getString("token", null);
        prefs.destroy();

        if (token != null) {
            sendToken(token);
        }

        return Result.success();
    }

    private void sendToken(String token) {
        Prefs prefs = new Prefs(getApplicationContext());

        boolean analyticsPermission = prefs.getBool("analyticsPermission", false);

        if (!analyticsPermission) {
            prefs.destroy();
            return;
        }


        String url = "https://europe-west1-yogscastapp-7e6f0.cloudfunctions.net/userAccessData/user";

        UserData syncData = new UserData(token, prefs.getCreator(),
                prefs.getTwitch(), prefs.getYoutube(),
                prefs.getTwitchNotifications(), prefs.getYoutubeNotifications(),
                prefs.getTwitchInbox(), prefs.getYoutubeInbox());

        Gson gson = new Gson();

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody
                .create(MediaType.parse("application/json; charset=utf-8"),
                        gson.toJson(syncData));
        Request request = new Request.Builder()
                .url(url).post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            response.body().close();
        } catch (Exception e) {
            Log.e("AnalyticsWorker", "log error", e);
        } finally {
            prefs.destroy();
        }
    }


    class UserData {
        private String id;
        private int version;
        private String country;
        private String language;
        private List<String> creator;
        private List<String> twitch;
        private List<String> youtube;
        private List<String> twitchNotifications;
        private List<String> youtubeNotifications;
        private List<String> twitchInbox;
        private List<String> youtubeInbox;


        UserData(String id, List<String> creator, List<String> twitch, List<String> youtube,
                 List<String> twitchNotifications, List<String> youtubeNotifications,
                 List<String> twitchInbox, List<String> youtubeInbox) {
            this.id = id;
            this.creator = creator;
            this.twitch = twitch;
            this.youtube = youtube;
            this.twitchNotifications = twitchNotifications;
            this.youtubeNotifications = youtubeNotifications;
            this.twitchInbox = twitchInbox;
            this.youtubeInbox = youtubeInbox;
            this.version = 20191009;
            this.country = Locale.getDefault().getISO3Country();
            this.language = Locale.getDefault().getISO3Language();
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public List<String> getCreator() {
            return creator;
        }

        public void setCreator(List<String> creator) {
            this.creator = creator;
        }

        public List<String> getTwitch() {
            return twitch;
        }

        public void setTwitch(List<String> twitch) {
            this.twitch = twitch;
        }

        public List<String> getYoutube() {
            return youtube;
        }

        public void setYoutube(List<String> youtube) {
            this.youtube = youtube;
        }

        public List<String> getTwitchNotifications() {
            return twitchNotifications;
        }

        public void setTwitchNotifications(List<String> twitchNotifications) {
            this.twitchNotifications = twitchNotifications;
        }

        public List<String> getYoutubeNotifications() {
            return youtubeNotifications;
        }

        public void setYoutubeNotifications(List<String> youtubeNotifications) {
            this.youtubeNotifications = youtubeNotifications;
        }

        public List<String> getTwitchInbox() {
            return twitchInbox;
        }

        public void setTwitchInbox(List<String> twitchInbox) {
            this.twitchInbox = twitchInbox;
        }

        public List<String> getYoutubeInbox() {
            return youtubeInbox;
        }

        public void setYoutubeInbox(List<String> youtubeInbox) {
            this.youtubeInbox = youtubeInbox;
        }
    }
}
