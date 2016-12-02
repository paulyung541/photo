package com.paulyung.pyphoto.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;
import com.paulyung.pyphoto.adapter.viewholder.TimeStateViewHolder;
import com.paulyung.pyphoto.bean.PhotoMsg;

import java.util.List;

/**
 * Created by yang on 2016/12/1.
 * paulyung@outlook.com
 */

public class TimeStateAdapter extends BaseAdapter<PhotoMsg> {
    private static final int TIME_TYPE = 0x01;
    private static final int PHOTO_TYPE = 0x02;

    public TimeStateAdapter(Context context, List<PhotoMsg> datas) {
        super(context, datas);
    }

    public class TimeLookUp extends GridSpanSizeLookup {

        public TimeLookUp(int normalSize) {
            super(normalSize);
        }

        @Override
        public int getSpanSize(int position) {
            int res = super.getSpanSize(position);
            if (res == 1)
                if (getItemViewType(position) == TIME_TYPE)
                    res = mNormalSize;
            return res;
        }
    }

    @Override
    public TimeLookUp getGridLookUp(int normalSize) {
        return new TimeLookUp(normalSize);
    }

    @Override
    protected BaseViewHolder createHolder(ViewGroup parent, int viewType) {
        if (viewType == TIME_TYPE)
            return new TimeStateViewHolder.TimeShowViewHolder(parent, R.layout.item_time_show);
        else if (viewType == PHOTO_TYPE)
            return new TimeStateViewHolder.PhotoShowViewHolder(parent, R.layout.item_photo_list);
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        int res = super.getItemViewType(position);
        if (res == 0) {
            // TODO: 2016/12/1 自己处理
            PhotoMsg msg = getItem(position);
            if (msg.getAbsolutePath() == null)
                res = TIME_TYPE;
            else
                res = PHOTO_TYPE;
        }
        return res;
    }
}