package com.brian.wmessage;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.brian.common.base.BaseActivity;
import com.brian.wmessage.recentlist.RecentListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        RecentListFragment fragment = RecentListFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.xlvoice_fragment_container, fragment);
        transaction.commitAllowingStateLoss();
    }
}
