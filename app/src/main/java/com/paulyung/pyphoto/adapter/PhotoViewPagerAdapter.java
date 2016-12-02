package com.paulyung.pyphoto.adapter;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.widget.TouchImageView;

import java.util.List;

/**
 * Created by yang on 2016/11/28.
 * paulyung@outlook.com
 */

public class PhotoViewPagerAdapter extends PagerAdapter {
    public static final int OFFSCREEN_PAGES = 2;
    private List<PhotoMsg> mDatas;
    private ViewPager viewPager;
    private int mCurrentPositio = -1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (viewPager != null) {//防止Activity销毁
                if (msg.arg1 == viewPager.getCurrentItem()) {
                    final ImageView imageView = (ImageView) msg.obj;
                    Glide.with(viewPager.getContext())
                            .load(mDatas
                                    .get(msg.arg1)
                                    .getAbsolutePath())
                            .asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (imageView != null) {
                                //防止 imageView被销毁
                                imageView.setImageBitmap(resource);
                            }
                        }
                    });
                }
            }
        }
    };

    public PhotoViewPagerAdapter(ViewPager viewPager, List<PhotoMsg> datas) {
        this.viewPager = viewPager;
        mDatas = datas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        TouchImageView imageView = new TouchImageView(container.getContext());
//        imageView.setAdjustViewBounds(true);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        String path = mDatas.get(position).getAbsolutePath();
        Glide.with(container.getContext()).load(path).override(200, 200).into(imageView);
        container.addView(imageView, params);
        return imageView;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
//        直接加载大图太耗内存，如果用户一直滑动，则没必要加载大图，如果用户停留超过1s，则加载大图
        if (mCurrentPositio != position) {
            mCurrentPositio = position;
            Message msg = handler.obtainMessage();
            msg.obj = object;
            msg.arg1 = position;
            handler.sendMessageDelayed(msg, 300);
        }
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
