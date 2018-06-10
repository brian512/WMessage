package com.brian.wmessage.search;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.brian.common.base.BaseActivity;
import com.brian.common.utils.LogUtil;
import com.brian.common.utils.ToastUtil;
import com.brian.wmessage.R;
import com.brian.wmessage.account.UserInfoActivity;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.chat.OnRecyclerViewListener;
import com.brian.wmessage.entity.UserInfo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 搜索用户
 * @author huamm
 */
public class SearchUserActivity extends BaseActivity {

    @BindView(R.id.et_find_name)
    EditText mNameEt;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout mRefreshLayout;
    @BindView(R.id.btn_search)
    Button mSearchBtn;
    @BindView(R.id.rc_view)
    RecyclerView mRecyclerView;

    private SearchUserAdapter mUserAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_user_activity);
        ButterKnife.bind(this);
        mUserAdapter = new SearchUserAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mUserAdapter);
        mRefreshLayout.setEnabled(true);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                query();
            }
        });
        mUserAdapter.setOnRecyclerViewListener(new OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position) {
                UserInfo user = mUserAdapter.getItem(position);
                UserInfoActivity.startActivity(getBaseContext(), user);
            }

            @Override
            public boolean onItemLongClick(int position) {
                return true;
            }
        });

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
    }

    public void query() {
        String name = mNameEt.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showMsg("请填写用户名");
            mRefreshLayout.setRefreshing(false);
            return;
        }
        BmobHelper.getInstance().queryUsers(name, 20,
                new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {
                            mRefreshLayout.setRefreshing(false);
                            mUserAdapter.setDatas(list);
                            mUserAdapter.notifyDataSetChanged();
                        } else {
                            mRefreshLayout.setRefreshing(false);
                            mUserAdapter.setDatas(null);
                            mUserAdapter.notifyDataSetChanged();
                            LogUtil.printError(e);
                        }
                    }
                }
        );
    }

}
