package com.brian.common.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brian.common.stat.Stat;
import com.brian.common.tools.Env;
import com.brian.common.tools.NetworkMonitor;
import com.brian.common.utils.LogUtil;

/**
 * Fragment的封装，暂时没有实现任何功能
 *
 * @author huamm
 *
 */
public class BaseFragment extends Fragment implements NetworkMonitor.OnNetworkChangedListener {


    protected boolean isPaused = false;

    protected boolean isStoped = false;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        LogUtil.log(getClass().getSimpleName());
    }

    @Override
    public void onAttachFragment(Fragment childFragment) {
        super.onAttachFragment(childFragment);
        LogUtil.log(getClass().getSimpleName());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.log(getClass().getSimpleName());
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.log(getClass().getSimpleName());

        Stat.onResume(getContext(), getClass().getSimpleName());

        NetworkMonitor.getInstance().subscribe(getContext(), this);

        isPaused = false;
    }

    @Override
    public void onPause() {
        Stat.onPause(getContext(), getClass().getSimpleName());
        LogUtil.log(getClass().getSimpleName());
        super.onPause();

        isPaused = true;

        NetworkMonitor.getInstance().unsubscribe(getContext(), this);
    }

    @Override
    public void onDestroy() {
        LogUtil.log(getClass().getSimpleName());
        super.onDestroy();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LogUtil.log(getClass().getSimpleName());
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        LogUtil.log(getClass().getSimpleName());
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        LogUtil.log(getClass().getSimpleName());
        super.onDetach();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        LogUtil.log(getClass().getSimpleName());
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtil.log(getClass().getSimpleName());
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.log(getClass().getSimpleName());

        isStoped = false;
    }

    @Override
    public void onStop() {
        LogUtil.log(getClass().getSimpleName());
        super.onStop();

        isStoped = true;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        LogUtil.log(getClass().getSimpleName());
        super.onViewStateRestored(savedInstanceState);
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        LogUtil.log(getClass().getSimpleName());
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        LogUtil.log(getClass().getSimpleName());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        LogUtil.log(getClass().getSimpleName() + "; isVisibleToUser=" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        LogUtil.log("hidden=" + hidden + "; page=" + getClass().getSimpleName());
        super.onHiddenChanged(hidden);
    }

    @Override
    public Context getContext() {
        Context context = super.getContext();
        if (context == null) {
            context = BaseActivity.getTopActivity();
        }
        if (context == null) {
            context = Env.getContext();
        }
        return context;
    }

    public Activity getCurrActivity() {
        Activity activity = getActivity();
        if (activity == null) {
            activity = BaseActivity.getTopActivity();
        }
        return activity;
    }

    public boolean onBackPressed() {
        return false;
    }

    @Override
    public void onNetworkChanged(boolean isConnected, int type) {
    }
}
