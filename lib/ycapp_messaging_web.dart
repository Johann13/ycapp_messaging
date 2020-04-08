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

  Future<void> user() async {
    String url =
        'https://europe-west1-yogscastapp-7e6f0.cloudfunctions.net/userAccessData/user';
    String token = await getToken();
    List<String> creator = await Prefs.getStringList('creatorSubscriptions');
    List<String> twitch = await Prefs.getStringList('twitchSubscriptions');
    List<String> youtube = await Prefs.getStringList('youtubeSubscriptions');
    Map<String, dynamic> data = {
      'id': token,
      'creator': creator,
      'twitch': twitch,
      'youtube': youtube,
      'language': html.window.navigator.language,
    };
    Map<String, String> headers = {
      'Content-type': 'application/json',
      'Accept': 'application/json',
    };
    await http.post(
      url,
      headers: headers,
      body: data,
    );
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
