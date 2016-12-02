package com.paulyung.pyphoto.adapter;

import android.content.Context;
import android.view.ViewGroup;

import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;
import com.paulyung.pyphoto.adapter.viewholder.TimeStateViewHolder;
import com.paulyung.pyphoto.bean.PhotoMsg;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yang on 2016/12/1.
 * paulyung@outlook.com
 */

public class TimeStateAdapter extends BaseAdapter<PhotoMsg> {
    private static final int TIME_TYPE = 0x01;
    private static final int PHOTO_TYPE = 0x02;
    private List<PhotoMsg> mPhotoDatas = new ArrayList<>();

    public TimeStateAdapter(Context context, List<PhotoMsg> datas) {
        super(context, datas);
        init();
    }

    private void init() {
        for (int i = 0; i < getDataSize(); i++) {
            PhotoMsg msg = getItem(i);
            if (msg.getAbsolutePath() != null)
                mPhotoDatas.add(msg);
        }
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
    protected int getComponentP(int position) {
        return mPhotoDatas.indexOf(getItem(position));
    }

    @Override
    public synchronized void update(int position) {
        super.update(getAllData().indexOf(mPhotoDatas.get(position)));
    }

    @Override
    public int getItemViewType(int position) {
        int res = super.getItemViewType(position);
        if (res == 0) {
            PhotoMsg msg = getItem(position);
            if (msg.getAbsolutePath() == null)
                res = TIME_TYPE;
            else
                res = PHOTO_TYPE;
        }
        return res;
    }

    public PhotoMsg getPhotoItem(int p) {
        return mPhotoDatas.get(p);
    }

    public int getPhotoSize() {
        return mPhotoDatas.size();
    }

    //将CheckBox隐藏，并都置为false
    public void clearCheckBoxStateAll() {
        for (int i = 0; i < mPhotoDatas.size(); ++i) {
            mPhotoDatas.get(i).setShowCheckBox(false);
            mPhotoDatas.get(i).setCheck(false);
        }
        if (getDataSize() != 0)
            notifyDataSetChanged();
    }

    public void synchronizedData() {
        init();
    }
}
