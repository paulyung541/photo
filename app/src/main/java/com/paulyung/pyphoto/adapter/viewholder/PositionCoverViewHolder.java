package com.paulyung.pyphoto.adapter.viewholder;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PositionCover;
import com.paulyung.pyphoto.utils.UIHelper;

/**
 * Created by yang on 2016/12/6.
 * paulyung@outlook.com
 */

public class PositionCoverViewHolder extends BaseViewHolder<PositionCover> {
    private ImageView imageView;
    private TextView textView;
    private Context context;

    public PositionCoverViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
        imageView = $(R.id.image);
        textView = $(R.id.text);
        context = parent.getContext();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        params.height = UIHelper.getScreenWidth() - parent.getPaddingLeft() - parent.getPaddingRight()
        - 600;
        imageView.setLayoutParams(params);
    }

    @Override
    public void setData(PositionCover data) {
        Glide.with(context)
                .load(data.getCoverImagePath())
                .override(400, 400)
                .into(imageView);
        textView.setText(data.getTitle() + " (" + data.getSize() + ')');
    }
}
