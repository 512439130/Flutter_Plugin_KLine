
import 'dart:async';

import 'package:flutter/services.dart';

class FlutterPluginKline {

  static const MethodChannel _methodChannel = const MethodChannel('com.zb.flutter.kline/methodChannel');

  ///更新K线数据 KLine-Json数据
  static Future<void> refreshKLineData(String data) async {
    Map<String, Object> params = {
      "data": data,
    };
    _methodChannel.invokeMethod('refreshKLineData', params);
  }

  ///更新K线主题颜色 0:白天 1:黑夜
  static Future<void> updateKLineThemeState(String state) async {
    Map<String, Object> params = {
      "state": state,
    };
    _methodChannel.invokeMethod('updateKLineThemeState', params);
  }

  ///更新K线蜡烛主题 0:实心 1:空心
  static Future<void> updateKLineCandleThemeState(String state) async {
    Map<String, Object> params = {
      "state": state,
    };
    _methodChannel.invokeMethod('updateKLineCandleThemeState', params);
  }

  ///更新K线滑动模式 0:K线滑动惯性 1:K线跟随手指
  static Future<void> updateKLineScrollState(String state) async {
    Map<String, Object> params = {
      "state": state,
    };
    _methodChannel.invokeMethod('updateKLineScrollState', params);
  }

  ///更新K线按压模式 0:坐标点 1:手指
  static Future<void> updateKLinePressState(String state) async {
    Map<String, Object> params = {
      "state": state,
    };
    _methodChannel.invokeMethod('updateKLinePressState', params);
  }

  ///更改K线主图状态 0:MA 1:EMA 2:BOLL -1:隐藏）
  static Future<void> updateKLineMainState(String state) async {
    Map<String, Object> params = {
      "state": state,
    };
    _methodChannel.invokeMethod('updateKLineMainState', params);
  }

  ///更改K线子图状态 0:MACD 1:KDJ 2:RSI 3:WR -1:隐藏
  static Future<void> updateKLineChildState(String state) async {
    Map<String, Object> params = {
      "state": state,
    };
    _methodChannel.invokeMethod('updateKLineChildState', params);
  }
}
