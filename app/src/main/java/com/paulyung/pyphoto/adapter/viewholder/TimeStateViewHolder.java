package com.paulyung.pyphoto.adapter.viewholder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.utils.UIHelper;

/**
 * Created by yang on 2016/12/1.
 * paulyung@outlook.com
 */

public class TimeStateViewHolder {
    public static class TimeShowViewHolder extends BaseViewHolder<PhotoMsg> {
        private TextView tvTime;

        public TimeShowViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            tvTime = $(R.id.time);
        }

        @Override
        public void setData(PhotoMsg data) {
            tvTime.setText(data.getTime());
        }
    }

    public static class PhotoShowViewHolder extends BaseViewHolder<PhotoMsg> {
        private ImageView imageView;
        private CheckBox checkBox;
        private RelativeLayout.LayoutParams params;

        public PhotoShowViewHolder(ViewGroup parent, @LayoutRes int res) {
            super(parent, res);
            imageView = $(R.id.image);
            checkBox = $(R.id.checkbox);

            params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
            params.height = (UIHelper.getScreenWidth() - parent.getPaddingLeft()
                    - parent.getPaddingRight()) / 3;//限制高度，让它等于item的宽度
            imageView.setLayoutParams(params);
        }

        @Override
        public void setData(PhotoMsg data) {
            int maxSize = Math.min(params.height, params.height);

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
}
