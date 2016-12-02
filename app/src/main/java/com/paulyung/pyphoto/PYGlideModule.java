package com.paulyung.pyphoto;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by yang on 2016/11/29.
 * paulyung@outlook.com
 * http://www.cnblogs.com/whoislcj/p/5565012.html Glide初始化详解
 */

public class PYGlideModule implements GlideModule {
    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int memoryCacheSize = maxMemory / 2;
        builder.setMemoryCache(new LruResourceCache(memoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(memoryCacheSize));
        int diskCacheSize = 1024 * 1024 * 30;//最多可以缓存多少字节的数据
        //设置磁盘缓存大小
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "glide", diskCacheSize));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
