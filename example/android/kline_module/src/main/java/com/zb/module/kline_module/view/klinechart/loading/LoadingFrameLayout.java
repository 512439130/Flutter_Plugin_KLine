package com.zb.module.kline_module.view.klinechart.loading;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.zb.module.kline_module.R;

import androidx.annotation.AttrRes;
import androidx.annotation.Nullable;


public class LoadingFrameLayout extends FrameLayout {
    private Animation anim;
    private ImageView loadingImageView;
    public LoadingFrameLayout(Context context) {
        super(context);
        init(context, null,0);
    }
    public LoadingFrameLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs,0);
    }
    public LoadingFrameLayout(Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr){
        super(context,attrs,defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        //加载自定义数据的布局
        View root = LayoutInflater.from(context).inflate(R.layout.plug_view_loading, this, true);
        loadingImageView=root.findViewById(R.id.loadingImageView);
        anim = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());//不停顿
        //anim.setInterpolator(new AccelerateInterpolator()); // 设置插入器
        anim.setRepeatCount(-1);//重复次数
        anim.setFillAfter(true);//停在最后
        anim.setDuration(2000);
        loadingImageView.startAnimation(anim);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility==VISIBLE){
            loadingImageView.startAnimation(anim);
        }else{
            loadingImageView.clearAnimation();
        }
    }
}
