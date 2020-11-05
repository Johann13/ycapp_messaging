import 'dart:async';

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:ycapp_foundation/prefs/prefs.dart';

class YMessaging {
  static FirebaseMessaging _messaging = FirebaseMessaging.instance;

  static Future<String> getToken() {
    return _messaging.getToken();
  }

  static Future<void> subscribeToTopic(String _channelId) {
    return _messaging.subscribeToTopic(_channelId);
  }

  static Future<void> unsubscribeFromTopic(String _channelId) {
    return _messaging.unsubscribeFromTopic(_channelId);
  }

  static Future<void> enableFCM(bool enable) {
    return _messaging.setAutoInitEnabled(enable);
  }

  static Future<void> subscribeAll() async {
    List<String> creator = await Prefs.getStringList('creator');
    List<String> youtube = await Prefs.getStringList('youtube');
    List<String> twitch = await Prefs.getStringList('twitch');
    await Future.wait([
      for(String id in creator)
        subscribeToTopic(id),
      for(String id in youtube)
        subscribeToTopic(id),
      for(String id in twitch)
        subscribeToTopic(id),
    ]);
  }
}
