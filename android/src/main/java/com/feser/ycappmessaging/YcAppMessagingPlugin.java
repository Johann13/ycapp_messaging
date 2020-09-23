package com.feser.ycappmessaging;

import android.app.Activity;
import android.content.Context;
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
    private Context activity;
    private final String TAG = "YcAppMessagingPlugin";


    private static void setup(YcAppMessagingPlugin plugin, @NonNull FlutterPluginBinding flutterPluginBinding) {
        plugin.channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "ycappmessaging");
        plugin.channel.setMethodCallHandler(plugin);
        if (plugin.prefs == null) {
            plugin.prefs = new Prefs(flutterPluginBinding.getApplicationContext());
        } else {
            plugin.prefs.attach(flutterPluginBinding.getApplicationContext());
        }
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        setup(this, flutterPluginBinding);
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
