package com.paulyung.pyphoto.fragment;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.paulyung.pyphoto.BaseApplication;
import com.paulyung.pyphoto.PermissionManager;
import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.adapter.BaseAdapter;
import com.paulyung.pyphoto.adapter.viewholder.BaseViewHolder;
import com.paulyung.pyphoto.bean.PhotoMsg;
import com.paulyung.pyphoto.callback.OnPhotoMsgBackListener;
import com.paulyung.pyphoto.utils.DBHelper;
import com.paulyung.pyphoto.utils.MultiMap;
import com.paulyung.pyphoto.utils.UIHelper;
import com.paulyung.pyphoto.widget.SpaceDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by yang on 2016/11/15.
 * paulyung@outlook.com
 */

public class PhotoStateFragment extends BaseFragment implements OnPhotoMsgBackListener {
    private RecyclerView mRyView;
    private BaseAdapter<PhotoMsg> adapter;

    public static Fragment getInstance(Bundle bundle) {
        Fragment fragment = new PhotoStateFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionManager.getInstants().addInitCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                new PermissionManager.InitOptions() {
            @Override
            public void doInit() {
                DBHelper.scanPhoto(getContext(), PhotoStateFragment.this);//扫描SD卡，获取图片信息
            }
        }).enableDialog(false).cheackAndRequest(getActivity(), new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, false);

    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_photo_state;
    }

    @Override
    protected void initView() {
        mRyView = (RecyclerView) findViewById(R.id.ry_view);
        adapter = new BaseAdapter<PhotoMsg>(getActivity()) {
            @Override
            protected BaseViewHolder createHolder(ViewGroup parent) {
                return new PhotoViewHolder(parent, R.layout.item_photo_state);
            }
        };
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        manager.setSpanSizeLookup(adapter.getGridLookUp(2));
        mRyView.setLayoutManager(manager);

        SpaceDecoration itemDecoration = new SpaceDecoration((int) UIHelper.dpToPx(8,getContext()));
        itemDecoration.setPaddingEdgeSide(true);
        itemDecoration.setPaddingStart(false);
        itemDecoration.setPaddingHeaderFooter(false);
        mRyView.addItemDecoration(itemDecoration);
        mRyView.setAdapter(adapter);
    }

    @Override
    public void onAllPhotoGet() {
        List<PhotoMsg> list = new ArrayList<>();
        MultiMap<String, PhotoMsg> map = BaseApplication.getInstance().getPhotoMsg();
        Set<String> set = map.keySet();
        for (String s : set) {
            list.add(map.get(s).get(map.get(s).size()-1));
        }
        adapter.addAll(list);
    }

    private class PhotoViewHolder extends BaseViewHolder<PhotoMsg> {
        CardView cardView;
        ImageView imageView;
        TextView textView;
        public PhotoViewHolder(ViewGroup parent, int id) {
            super(parent, id);
            cardView = $(R.id.card_view);
            imageView = $(R.id.image);
            textView = $(R.id.text);

            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) cardView.getLayoutParams();
            params.width = (dm.widthPixels) / 2;
            params.height = params.width;
            cardView.setLayoutParams(params);
        }

        @Override
        public void setData(PhotoMsg data) {
            Glide.with(getActivity())
                    .load(data.getAbsolutePath())
                    .placeholder(R.mipmap.loadding)
                    .thumbnail(0.2f)
                    .into(imageView);
            textView.setText(data.getParentName());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.getInstants().handleRequest(getActivity(), requestCode, permissions, grantResults);
    }
}
