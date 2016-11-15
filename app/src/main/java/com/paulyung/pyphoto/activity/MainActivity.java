package com.paulyung.pyphoto.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.paulyung.pyphoto.R;
import com.paulyung.pyphoto.fragment.PhotoStateFragment;
import com.paulyung.pyphoto.fragment.PositionStateFragment;
import com.paulyung.pyphoto.fragment.TimeStateFragment;

public class MainActivity extends BaseActivity {
    private FragmentManager mFragmentManager;

    @Override
    protected void beforeSetView() {
        mFragmentManager = getSupportFragmentManager();
    }

    @Override
    protected void initView() {
        cheackFragmentById(R.id.action_photo_state);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected Toolbar getInitToolbar() {
        return (Toolbar) findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);
        cheackFragmentById(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    /**
     * 切换Fragment
     */
    private void cheackFragmentById(int id) {
        Fragment fragment = mFragmentManager.findFragmentByTag(String.valueOf(id));
        if (fragment == null) {
            switch (id) {
                case R.id.action_photo_state:
                    fragment = PhotoStateFragment.getInstance(null);
                    break;
                case R.id.action_time_state:
                    fragment = TimeStateFragment.getInstance(null);
                    break;
                case R.id.action_position_state:
                    fragment = PositionStateFragment.getInstance(null);
                    break;
                default:
            }
        }
        mFragmentManager.beginTransaction().replace(R.id.lay_container, fragment, String.valueOf(id)).commit();
    }
}
