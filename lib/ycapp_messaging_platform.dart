import 'package:plugin_platform_interface/plugin_platform_interface.dart';
import 'package:ycappmessaging/method_channel_ycapp_messaging.dart';

abstract class YMessagingPlatform extends PlatformInterface {
  YMessagingPlatform() : super(token: _token);
  static final Object _token = Object();
  static YMessagingPlatform _instance = MethodChannelYMessaging();

  static YMessagingPlatform get instance => _instance;

  static set instance(YMessagingPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String> getToken() {
    throw UnimplementedError('');
  }

  Future<dynamic> subscribeToTopic(String _channelId) {
    throw UnimplementedError('');
  }

  Future<dynamic> unsubscribeFromTopic(String _channelId) {
    throw UnimplementedError('');
  }

  Future<dynamic> enableFCM(bool enable) {
    throw UnimplementedError('');
  }

  Future<dynamic> enableAnalytics(bool enable) {
    throw UnimplementedError('');
  }

  Future<void> subscribeAll() {
    throw UnimplementedError('');
  }

  Future<void> user() {
    throw UnimplementedError('');
  }

  Future<void> logUserSub(int hours) {
    throw UnimplementedError('');
  }
}
