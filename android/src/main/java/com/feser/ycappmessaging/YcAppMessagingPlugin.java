package com.feser.ycappmessaging;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.feser.ycapp_foundation.prefs.Prefs;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/**
 * YcappmessagingPlugin
 */
public class YcAppMessagingPlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;
    private Prefs prefs;
    private Activity activity;
    private final String TAG = "YcAppMessagingPlugin";

    public YcAppMessagingPlugin(Activity activity) {
        this.activity = activity;
        if (prefs == null) {
            this.prefs = new Prefs(activity);
        } else {
            prefs.attach(activity);
        }
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "ycappmessaging");
        channel.setMethodCallHandler(this);
        if (prefs == null) {
            prefs = new Prefs(flutterPluginBinding.getApplicationContext());
        } else {
            prefs.attach(flutterPluginBinding.getApplicationContext());
        }
    }

    // This static function is optional and equivalent to onAttachedToEngine. It supports the old
    // pre-Flutter-1.12 Android projects. You are encouraged to continue supporting
    // plugin registration via this function while apps migrate to use the new Android APIs
    // post-flutter-1.12 via https://flutter.dev/go/android-project-migration.
    //
    // It is encouraged to share logic between onAttachedToEngine and registerWith to keep
    // them functionally equivalent. Only one of onAttachedToEngine or registerWith will be called
    // depending on the user's project. onAttachedToEngine or registerWith must both be defined
    // in the same class.
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "ycappmessaging");
        channel.setMethodCallHandler(new YcAppMessagingPlugin(registrar.activity()));
    }

    @Override
    public void onMethodCall(@NonNull MethodCall methodCall, @NonNull final Result result) {
        switch (methodCall.method) {
            case "subscribeAll":
                Log.d(TAG, "subscribeAll");
                SubscribeAllWorker.enqueue(activity);
                result.success(null);
                break;
            case "enableFCM":
                boolean enableFCM = (boolean) methodCall.argument("enable");
                Log.d(TAG, "enableFCM: " + enableFCM);
                FirebaseMessaging.getInstance()
                        .setAutoInitEnabled(enableFCM);
                if (enableFCM) {
                    SubscribeAllWorker.enqueue(activity);
                    activity.startService(new Intent(activity, CreateInstanceIdService.class));
                    result.success("Token created");
                } else {
                    activity.startService(new Intent(activity, DeleteInstanceIdService.class));
                    result.success("No Token");
                }
                break;
            case "getToken":
                Log.d(TAG, "getToken");
                //result.success(FirebaseInstanceId.getInstance().getId());
                boolean b = prefs.getBool("fcmPermission", false);
                if (b) {
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    result.error("s1", "s2", e);
                                }
                            })
                            .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                                @Override
                                public void onSuccess(InstanceIdResult instanceIdResult) {
                                    Log.d("getInstanceId Token", instanceIdResult.getToken());
                                    Log.d("getInstanceId Id", instanceIdResult.getId());
                                    result.success(instanceIdResult.getToken());
                                }
                            });
                } else {
                    result.success("No Token");
                }
                break;
            case "getId":
                Log.d(TAG, "getId");
                FirebaseInstanceId.getInstance().getInstanceId()
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                result.error("s1", "s2", e);
                            }
                        })
                        .addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
                            @Override
                            public void onSuccess(InstanceIdResult instanceIdResult) {
                                Log.d("getInstanceId Token", instanceIdResult.getToken());
                                Log.d("getInstanceId Id", instanceIdResult.getId());
                                result.success(instanceIdResult.getId());
                            }
                        });

                break;
            case "subscribeToTopic":
                String subTopic = methodCall.argument("channelId");
                Log.d(TAG, "subscribeToTopic: " + subTopic);
                if (subTopic != null) {
                    FirebaseMessaging.getInstance()
                            .subscribeToTopic(subTopic);
                    result.success(true);
                } else {
                    result.success(false);
                }
                break;
            case "unsubscribeFromTopic":
                String unSubTopic = methodCall.argument("channelId");
                Log.d(TAG, "unsubscribeFromTopic: " + unSubTopic);
                if (unSubTopic != null) {
                    FirebaseMessaging.getInstance()
                            .unsubscribeFromTopic(unSubTopic);
                    result.success(true);
                } else {
                    result.success(false);
                }
                break;
            default:
                result.notImplemented();
                break;
        }
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        if (prefs != null) {
            prefs.destroy();
        }
        channel.setMethodCallHandler(null);
    }
}
