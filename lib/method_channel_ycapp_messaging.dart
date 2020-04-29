import 'package:flutter/services.dart';
import 'package:ycapp_foundation/prefs/prefs.dart';
import 'package:ycapp_messaging/ycapp_messaging_platform.dart';

class MethodChannelYMessaging extends YMessagingPlatform {
  static const MethodChannel _channel = const MethodChannel('ycappmessaging');

  Future<String> getToken() {
    return _channel.invokeMethod('getToken');
  }

  Future<dynamic> subscribeToTopic(String _channelId) {
    return _channel.invokeMethod(
        'subscribeToTopic', <String, dynamic>{'channelId': _channelId});
  }

  Future<dynamic> unsubscribeFromTopic(String _channelId) {
    return _channel.invokeMethod(
        'unsubscribeFromTopic', <String, dynamic>{'channelId': _channelId});
  }

  Future<dynamic> enableFCM(bool enable) async {
    return _channel.invokeMethod(
      'enableFCM',
      <String, dynamic>{
        'enable': enable,
      },
    );
  }

  Future<void> subscribeAll() {
    return _channel.invokeMethod('subscribeAll');
  }
}
