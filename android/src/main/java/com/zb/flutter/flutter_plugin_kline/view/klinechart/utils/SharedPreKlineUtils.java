package com.zb.flutter.flutter_plugin_kline.view.klinechart.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Title: SharedPreUtils
 * Description: TODO(SharedPreferences数据存储)
 * Copyright: Copyright (c) 2018
 * Company:  zsdk Teachnology
 * CreateTime: 2019-05-14 10:53
 *
 * @author GuoJun
 * @CheckItem 自己填写
 * @since JDK1.8
 */

public class SharedPreKlineUtils {
    private static final String SP_NAME = "chbtc";
    private static final String KEY_CACHE_THEME_CUSTOM_TAG = "Cache_themeCustomTag";
    private static SharedPreKlineUtils instance = new SharedPreKlineUtils();

    private Activity mActivity;

    public SharedPreKlineUtils() {
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            instance = new SharedPreKlineUtils();
        }
    }

    public static SharedPreKlineUtils getInstance() {
        if (instance == null) {

            syncInit();
        }
        return instance;
    }

    private SharedPreferences getSp(Context mContext) {
        return mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }






    public int getInt(String key, int def,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null)
                def = sp.getInt(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putInt(String key, int val,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putInt(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public long getLong(String key, long def,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null)
                def = sp.getLong(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putLong(String key, long val,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putLong(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getString(String key, String def,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null)
                def = sp.getString(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putString(String key, String val,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putString(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String key, boolean def,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null)
                def = sp.getBoolean(key, def);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return def;
    }

    public void putBoolean(String key, boolean val,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.putBoolean(key, val);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void remove(String key,Context context) {
        try {
            SharedPreferences sp = getSp(context);
            if (sp != null) {
                SharedPreferences.Editor e = sp.edit();
                e.remove(key);
                e.commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
