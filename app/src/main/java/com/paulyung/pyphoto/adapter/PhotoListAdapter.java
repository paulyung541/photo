package com.paulyung.pyphoto.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;
import com.paulyung.pyphoto.adapter.viewholder.PhotoListViewHolder;
import com.paulyung.pyphoto.bean.PhotoMsg;

/**
 * Created by yang on 2016/11/23.
 * paulyung@outlook.com
 */

public class PhotoListAdapter extends BaseAdapter<PhotoMsg> {
    private Fragment fragment;

    public PhotoListAdapter(Context context, Fragment fragment) {
        super(context);
        this.fragment = fragment;
    }

    @Override
    protected BaseViewHolder createHolder(ViewGroup parent, int type) {
        return new PhotoListViewHolder(fragment, parent, R.layout.item_photo_list);
    }

    //将CheckBox隐藏，并都置为false
    public void clearCheckBoxStateAll() {
        for (int i = 0; i < getDataSize(); ++i) {
            getItem(i).setCheck(false);
            getItem(i).setShowCheckBox(false);
        }
    }
}
