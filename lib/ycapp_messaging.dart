import 'dart:async';

import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';
import 'package:ycapp_foundation/prefs/prefs.dart';
import 'package:ycapp_messaging/ycapp_messaging_platform.dart';

class YMessaging {

  static Future<String> getToken() async {
    return YMessagingPlatform.instance.getToken();
  }

  static Future<dynamic> subscribeToTopic(String _channelId) async {
    return YMessagingPlatform.instance.subscribeToTopic(_channelId);
  }

  static Future<dynamic> unsubscribeFromTopic(String _channelId) async {
    return YMessagingPlatform.instance.unsubscribeFromTopic(_channelId);
  }

  static Future<dynamic> enableFCM(bool enable) async {
    return YMessagingPlatform.instance.enableFCM(enable);
  }

  static Future<void> subscribeAll() async {
    return YMessagingPlatform.instance.subscribeAll();
  }

  static Future<void> user() async {
    return YMessagingPlatform.instance.user();
  }

  static Future<void> logUserSub(int hours) async {
    return YMessagingPlatform.instance.logUserSub(hours);
  }
}
