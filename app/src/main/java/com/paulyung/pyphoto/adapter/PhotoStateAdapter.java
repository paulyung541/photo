package com.paulyung.pyphoto.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;
import com.paulyung.pyphoto.adapter.viewholder.PhotoViewHolder;
import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.fragment.PhotoStateFragment;

/**
 * Created by yang on 2016/11/21.
 * paulyung@outlook.com
 */

public class PhotoStateAdapter extends BaseAdapter<PhotoCover> {
    private PhotoStateFragment fragment;

    public PhotoStateAdapter(Context context, PhotoStateFragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    protected BaseViewHolder createHolder(ViewGroup parent) {
        //传入fragment，让Glide加载跟随fragment周期
        return new PhotoViewHolder(fragment, parent, R.layout.item_photo_state);
    }
}
