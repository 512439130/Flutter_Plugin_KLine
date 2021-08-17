package com.zb.flutter.flutter_plugin_kline.factory;

import android.content.Context;

import com.zb.flutter.flutter_plugin_kline.platform.MyKLineView;

import java.util.Map;

import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.StandardMessageCodec;
import io.flutter.plugin.platform.PlatformView;
import io.flutter.plugin.platform.PlatformViewFactory;

/**
 * @author SoMustYY
 * @create 2021/4/2 12:37 PM
 * @organize ZB
 * @describe
 * @update
 */
public class KLineViewFactory extends PlatformViewFactory {
    private MyKLineView myKLineView;
    private final BinaryMessenger messenger;
    public KLineViewFactory(BinaryMessenger messenger) {
        super(StandardMessageCodec.INSTANCE);
        this.messenger = messenger;
    }
    @Override
    public PlatformView create(Context context, int viewId, Object args) {
        Map<String, Object> params = (Map<String, Object>) args;
        myKLineView = new MyKLineView(context, messenger, viewId, params);
        return myKLineView;
    }

    public MyKLineView getMyKLineView() {
        return myKLineView;
    }
}