package com.paulyung.pyphoto.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.utils.UIHelper;

/**
 * Created by yang on 2016/11/23.
 * paulyung@outlook.com
 */

public class PhotoListViewHolder extends BaseViewHolder<PhotoMsg> {
    public static final int SPAN_COUNT = 3;

    Fragment fragment;
    ImageView imageView;
    CheckBox checkBox;
    private RelativeLayout.LayoutParams params;

    public PhotoListViewHolder(Fragment fragment, ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        this.fragment = fragment;
        imageView = $(R.id.image);
        checkBox = $(R.id.checkbox);

        params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.height = (UIHelper.getScreenWidth() - parent.getPaddingLeft() - parent.getPaddingRight()
                - params.leftMargin - params.rightMargin) / SPAN_COUNT;//限制高度，让它等于item的宽度
        imageView.setLayoutParams(params);
    }

    @Override
    public void setData(PhotoMsg data) {
        int maxSize = Math.min(params.height, 300);

        Glide.with(getContext())
                .load(data.getAbsolutePath())
                .error(R.mipmap.error)
                .override(maxSize, maxSize)
                .centerCrop()
                .into(imageView);
        if (data.isShowCheckBox()) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(data.isCheck());
        } else {
            checkBox.setChecked(false);
            checkBox.setVisibility(View.INVISIBLE);
        }
    }
}
