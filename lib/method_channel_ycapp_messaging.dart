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
        'subscribeToTopic', <String, dynamic>{'_channelId': _channelId});
  }

  Future<dynamic> unsubscribeFromTopic(String _channelId) {
    return _channel.invokeMethod(
        'unsubscribeFromTopic', <String, dynamic>{'_channelId': _channelId});
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

  Future<void> user() {
    return _channel.invokeMethod('user');
  }

  Future<void> logUserSub(int hours) async {
    int lastUserLog = await Prefs.getInt('lastUserLog',
        DateTime.now().subtract(Duration(days: 7)).millisecondsSinceEpoch);
    DateTime now = DateTime.now();
    DateTime lastLogDate = DateTime.fromMillisecondsSinceEpoch(lastUserLog);
    Duration duration = now.difference(lastLogDate);
    if (duration.inHours >= hours) {
      await user();
      await Prefs.setInt('lastUserLog', now.millisecondsSinceEpoch);
    }
  }
}
