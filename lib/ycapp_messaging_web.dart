import 'dart:html' as html;

import 'package:firebase/firebase.dart';
import 'package:flutter_web_plugins/flutter_web_plugins.dart';
import 'package:http/http.dart' as http;
import 'package:ycapp_foundation/prefs/prefs.dart';
import 'package:ycapp_messaging/ycapp_messaging_platform.dart';

class YMessagingWebPlugin extends YMessagingPlatform {
  final Messaging _msg = messaging();
  final Functions _functions = functions();

  static void registerWith(Registrar registrar) {
    YMessagingPlatform.instance = YMessagingWebPlugin();
  }

  Future<String> getToken() {
    return _msg.getToken();
  }

  Future<dynamic> subscribeToTopic(String _channelId) async {
    String token = await getToken();
    return _functions.httpsCallable('chromeExpTopicSub').call({
      'topic': _channelId,
      'token': token,
    });
  }

  Future<dynamic> unsubscribeFromTopic(String _channelId) async {
    String token = await getToken();
    return _functions.httpsCallable('chromeExpTopicUnsub').call({
      'topic': _channelId,
      'token': token,
    });
  }

  Future<dynamic> enableFCM(bool enable) {
    throw UnimplementedError('enableFCM');
  }

  Future<void> subscribeAll() async {
    List<String> creator = await Prefs.getStringList('creator');
    List<String> youtube = await Prefs.getStringList('youtube');
    List<String> twitch = await Prefs.getStringList('twitch');
    List<String> topics = ['all']
      ..addAll(creator)
      ..addAll(youtube)
      ..addAll(twitch);
    String token = await getToken();
    return _functions.httpsCallable('chromeExpTopicSub').call({
      'topics': topics,
      'token': token,
    });
  }
}
