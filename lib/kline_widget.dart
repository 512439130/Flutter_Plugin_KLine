import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:flutter/gestures.dart';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';


class KLineWidget extends StatefulWidget {
  final String theme;
  final String data;

  const KLineWidget({Key key, this.theme, this.data}) : super(key: key);
  @override
  _MyKLineState createState() => _MyKLineState();
}

class _MyKLineState extends State<KLineWidget> {
  String viewType = "com.zb.flutter.kline/klineView";
  @override
  Widget build(BuildContext context) {
    print("refreshKLineView-theme:"+widget.theme.toString());
    var params = {
      'data': widget.data,
      "theme":widget.theme,
    }; // 视图创建参数，可以被插件用来传递构造函数参数到嵌入式Android视图
    // 视图创建完毕的回调
    PlatformViewCreatedCallback callback = (id) {
      print("PlatformViewCreatedCallback:"+id.toString());
    };
    // 判断设备类型，也可用：defaultTargetPlatform == TargetPlatform.android
    Set<Factory<OneSequenceGestureRecognizer>> gestureRecognizers = <Factory<OneSequenceGestureRecognizer>>[Factory<OneSequenceGestureRecognizer>(() => EagerGestureRecognizer())].toSet();  //滑动冲突

    if (Platform.isAndroid) {
      return AndroidView(
        viewType: viewType,
        onPlatformViewCreated: callback,
        creationParams: params,
        creationParamsCodec: const StandardMessageCodec(),
        gestureRecognizers: gestureRecognizers,
      );
    } else if (Platform.isIOS) {
      return UiKitView(
        viewType: viewType,
        onPlatformViewCreated: callback,
      );
    } else {
      return Text('Platform is not yet supported by this plugin');
    }
  }
}