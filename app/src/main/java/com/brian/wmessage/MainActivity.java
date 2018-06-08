package com.brian.wmessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.base.BaseActivity;
import com.brian.common.views.TitleBar;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.conversations.ConversationListFragment;
import com.brian.wmessage.login.LoginActivity;
import com.brian.wmessage.search.SearchUserActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author huamm
 */
public class MainActivity extends BaseActivity {

    @BindView(R.id.main_pager)
    ViewPager mViewpager;
    @BindView(R.id.tabs)
    TabLayout mTabLayout;
    @BindView(R.id.recentlist_titlebar)
    TitleBar mTitleBar;

    private MainTabAdapter mTabAdapter = null;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BmobHelper.getInstance().checkOnlineState()) {
            LoginActivity.startActivity(this);
            finish();
        }
    }

    private void initView() {

        mTabAdapter = new MainTabAdapter(getSupportFragmentManager());
        // 视图切换器
        mViewpager.setOffscreenPageLimit(2);// 预先加载页面的数量
        mViewpager.setAdapter(mTabAdapter);
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        mTabLayout.setupWithViewPager(mViewpager);

        //设置小红点
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            View tabView = mTabAdapter.getTabView(this, i);
            ImageView imageView = tabView.findViewById(R.id.iv_tab_red);

            mTabLayout.getTabAt(i).setCustomView(tabView);
        }

        mTitleBar.getLeftView().setVisibility(View.GONE);
        mTitleBar.getRightView().setText("搜索");
        mTitleBar.getRightView().setVisibility(View.VISIBLE);
        mTitleBar.setRightListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchUserActivity.startActivity(getBaseContext());
            }
        });
    }
}
