package com.paulyung.pyphoto.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.TextView;

import com.paulyung.pyphoto.R;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class PositionStateFragment extends BaseFragment {

    public static Fragment getInstance(Bundle bundle) {
        Fragment fragment = new PositionStateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_position_state;
    }

    @Override
    protected void initView() {
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText("position_state_fragment");
    }
}
