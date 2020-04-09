import 'dart:async';

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
}
