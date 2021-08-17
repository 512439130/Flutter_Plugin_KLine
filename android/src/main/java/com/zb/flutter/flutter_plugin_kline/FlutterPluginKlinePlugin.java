package com.zb.flutter.flutter_plugin_kline;

import com.zb.flutter.flutter_plugin_kline.factory.KLineViewFactory;
import com.zb.flutter.flutter_plugin_kline.platform.MyKLineView;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;

/**
 * FlutterPluginKlinePlugin
 */
public class FlutterPluginKlinePlugin implements FlutterPlugin, MethodCallHandler {
    /// The MethodChannel that will the communication between Flutter and native Android
    ///
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    private static final String ZB_FLUTTER_KLINE_CHANNEL = "com.zb.flutter.kline/methodChannel";
    public static final String VIEW_TYPE = "com.zb.flutter.kline/klineView";

    private KLineViewFactory kLineViewFactory;
    private MyKLineView myKLineView;

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
        channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), ZB_FLUTTER_KLINE_CHANNEL);
        channel.setMethodCallHandler(this);
        registerViewFactory(flutterPluginBinding);
    }

    private void registerViewFactory(@NonNull FlutterPluginBinding flutterPluginBinding) {
        // 注册原生view，通过注册视图工厂（ViewFactory），需要传入唯一标识和ViewFactory类
        // 需要注册的视图的唯一标识
        kLineViewFactory = new KLineViewFactory(flutterPluginBinding.getBinaryMessenger());
        flutterPluginBinding.getPlatformViewRegistry().registerViewFactory(VIEW_TYPE, kLineViewFactory);

    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
        MyKLineView myKLineView = kLineViewFactory.getMyKLineView();
        if (call.method != null) {
            switch (call.method) {
                case "refreshKLineData":
                    if (myKLineView != null) {
                        if (call.arguments != null) {
                            String data = call.argument("data");
                            if (data == null) data = "";
                            myKLineView.refreshKLineData(data);
                        }
                    }
                    break;
                case "updateKLineThemeState":
                    if (myKLineView != null) {
                        if (call.arguments != null) {
                            String state = call.argument("state");
                            if (state == null) state = "0";
                            myKLineView.updateKlineColor(state);
                        }
                    }
                    break;
                case "updateKLineCandleThemeState":
                    if (myKLineView != null) {
                        if (call.arguments != null) {
                            String state = call.argument("state");
                            if (state == null) state = "0";
                            myKLineView.updateKLineCandleThemeState(state);
                        }
                    }
                    break;
                case "updateKLineScrollState":
                    if (myKLineView != null) {
                        if (call.arguments != null) {
                            String state = call.argument("state");
                            if (state == null) state = "0";
                            myKLineView.updateKLineScrollState(state);
                        }
                    }
                    break;
                case "updateKLinePressState":
                    if (myKLineView != null) {
                        if (call.arguments != null) {
                            String state = call.argument("state");
                            if (state == null) state = "0";
                            myKLineView.updateKLinePressState(state);
                        }
                    }
                    break;
                case "updateKLineMainState":
                    if (myKLineView != null) {
                        if (call.arguments != null) {
                            String state = call.argument("state");
                            if (state == null) state = "-1";
                            myKLineView.updateKlineMainIndexState(state);

                        }
                    }
                    break;
                case "updateKLineChildState":
                    if (myKLineView != null) {
                        if (call.arguments != null) {
                            String state = call.argument("state");
                            if (state == null) state = "-1";
                            myKLineView.updateKlineChildIndexState(state);
                        }
                    }
                    break;
                default:
                    result.notImplemented();
                    break;
            }
        }
    }
}
