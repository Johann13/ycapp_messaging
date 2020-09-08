import 'dart:io';

import 'package:firebase_messaging/firebase_messaging.dart';
import 'package:flutter/widgets.dart';

class YcAppPush extends StatefulWidget {
  final Widget child;

  YcAppPush({
    @required this.child,
  });

  @override
  _YcAppPushState createState() => _YcAppPushState();
}

class _YcAppPushState extends State<YcAppPush> {
  final FirebaseMessaging _firebaseMessaging = FirebaseMessaging();

  @override
  initState() {
    super.initState();
    _initFirebaseMessaging();
  }

  _initFirebaseMessaging() {
    _firebaseMessaging.configure(
      onMessage: (Map<String, dynamic> message) {
        print('AppPushs onMessage : $message');
        return _handleNotification(message);
      },
      onBackgroundMessage: Platform.isIOS ? null : myBackgroundMessageHandler,
      onResume: (Map<String, dynamic> message) {
        print('AppPushs onResume : $message');
        return _handleNotification(message);
      },
      onLaunch: (Map<String, dynamic> message) async {
        print('AppPushs onLaunch : $message');
        return _handleNotification(message);
      },
    );
    _firebaseMessaging.requestNotificationPermissions(
        const IosNotificationSettings(sound: true, badge: true, alert: true));
  }

  // TOP-LEVEL or STATIC function to handle background messages
  static Future<dynamic> myBackgroundMessageHandler(
      Map<String, dynamic> message) async {
    print('AppPushs myBackgroundMessageHandler : $message');
    return _handleNotification(message);
  }

  static Future<void> _handleNotification(Map<String, dynamic> message) async {
    if (!message.containsKey('data')) {
      return;
    }
  }

  @override
  Widget build(BuildContext context) {
    return widget.child;
  }
}
