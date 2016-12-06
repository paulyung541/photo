package com.paulyung.pyphoto.adapter.viewholder;

import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoCover;
import com.paulyung.pyphoto.utils.UIHelper;

/**
 * Created by yang on 2016/11/18.
 * paulyung@outlook.com
 */
//todo:图片中心显示问题，待解决
public class PhotoViewHolder extends BaseViewHolder<PhotoCover> {
    public static final int SPAN_COUNT = 2;

    private CardView cardView;
    private ImageView imageView;
    private TextView textView;
    private CheckBox checkBox;
    private Fragment fragment;

    public PhotoViewHolder(Fragment fragment, ViewGroup parent, int id) {
        super(parent, id);
        this.fragment = fragment;
        cardView = $(R.id.card_view);
        imageView = $(R.id.image);
        textView = $(R.id.text);
        checkBox = $(R.id.checkbox);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
        params.height = (UIHelper.getScreenWidth() - parent.getPaddingLeft() - parent.getPaddingRight() -
                (int) UIHelper.dpToPx(10)) / SPAN_COUNT;//限制高度，让它等于item的宽度
        imageView.setLayoutParams(params);
    }

    @Override
    public void setData(PhotoCover data) {
        int maxSize = Math.min(imageView.getLayoutParams().height, 400);

        Glide.with(getContext())
                .load(data.getCoverAbsolutePath())
                .error(R.mipmap.error)
                .override(maxSize, maxSize)
                .centerCrop()
                .into(imageView);
        textView.setText(data.getCoverName() + '(' + data.getSize() + ')');
        if (data.isShowCheckBox()) {
            checkBox.setVisibility(View.VISIBLE);
            checkBox.setChecked(data.isCheck());
        } else {
            checkBox.setChecked(false);
            checkBox.setVisibility(View.INVISIBLE);
        }
    }
}
