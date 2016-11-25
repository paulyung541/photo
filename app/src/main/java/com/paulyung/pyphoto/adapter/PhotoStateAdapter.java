package com.paulyung.pyphoto.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;
import com.paulyung.pyphoto.adapter.viewholder.PhotoViewHolder;
import com.paulyung.pyphoto.bean.PhotoCover;

/**
 * Created by yang on 2016/11/21.
 * paulyung@outlook.com
 */

public class PhotoStateAdapter extends BaseAdapter<PhotoCover> {
    private Fragment fragment;

    public PhotoStateAdapter(Context context, Fragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    protected BaseViewHolder createHolder(ViewGroup parent) {
        //传入fragment，让Glide加载跟随fragment周期
        return new PhotoViewHolder(fragment, parent, R.layout.item_photo_state);
    }
}
