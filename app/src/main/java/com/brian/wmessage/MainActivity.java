package com.brian.wmessage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.ToastUtil;
import com.brian.common.views.TitleBar;
import com.brian.wmessage.imservice.bmob.BmobHelper;
import com.brian.wmessage.account.LoginActivity;
import com.brian.wmessage.imservice.IIMServiceStateListener;
import com.brian.wmessage.imservice.IMServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 主页面，由viewpager的三个tab组成
 * @author huamm
 */
public class MainActivity extends BaseActivity implements IIMServiceStateListener {

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
        setContentView(R.layout.homepage_activity);
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

//        //设置小红点
//        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
//            View tabView = mTabAdapter.getTabView(this, i);
//            ImageView imageView = tabView.findViewById(R.id.iv_tab_red);
//            imageView.setVisibility(View.GONE);
//            mTabLayout.getTabAt(i).setCustomView(tabView);
//        }

        mTitleBar.setTitleResource(R.string.app_name);
    }

    @Override
    public void onIMStateChang(int state) {
        if (state == IMServiceManager.STATE_KICK_ASS) {
            ToastUtil.showMsg("您的帐号已在其他设备登陆");
            LoginActivity.startActivity(this);
            finish();
        }
    }
}
