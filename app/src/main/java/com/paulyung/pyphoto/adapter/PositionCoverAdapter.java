package com.paulyung.pyphoto.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;
import com.paulyung.pyphoto.adapter.viewholder.PositionCoverViewHolder;
import com.paulyung.pyphoto.bean.PositionCover;

import java.util.List;

/**
 * Created by yang on 2016/12/6.
 * paulyung@outlook.com
 */

public class PositionCoverAdapter extends BaseAdapter<PositionCover> {

    public PositionCoverAdapter(Context context, List<PositionCover> datas) {
        super(context, datas);
    }

    @Override
    protected BaseViewHolder createHolder(ViewGroup parent, int viewType) {
        return new PositionCoverViewHolder(parent, R.layout.item_positon_cover);
    }
}
