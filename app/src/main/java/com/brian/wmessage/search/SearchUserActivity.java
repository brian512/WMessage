package com.brian.wmessage.search;

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
    EditText et_find_name;
    @BindView(R.id.sw_refresh)
    SwipeRefreshLayout sw_refresh;
    @BindView(R.id.btn_search)
    Button btn_search;
    @BindView(R.id.rc_view)
    RecyclerView rc_view;
    LinearLayoutManager layoutManager;
    SearchUserAdapter mUserAdapter;

    public static void startActivity(Context context) {
        Intent intent = new Intent(context, SearchUserActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_user);
        ButterKnife.bind(this);
        mUserAdapter = new SearchUserAdapter();
        layoutManager = new LinearLayoutManager(this);
        rc_view.setLayoutManager(layoutManager);
        rc_view.setAdapter(mUserAdapter);
        sw_refresh.setEnabled(true);
        sw_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
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

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                query();
            }
        });
    }

    public void query() {
        String name = et_find_name.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showMsg("请填写用户名");
            sw_refresh.setRefreshing(false);
            return;
        }
        BmobHelper.getInstance().queryUsers(name, 20,
                new FindListener<UserInfo>() {
                    @Override
                    public void done(List<UserInfo> list, BmobException e) {
                        if (e == null) {
                            sw_refresh.setRefreshing(false);
                            mUserAdapter.setDatas(list);
                            mUserAdapter.notifyDataSetChanged();
                        } else {
                            sw_refresh.setRefreshing(false);
                            mUserAdapter.setDatas(null);
                            mUserAdapter.notifyDataSetChanged();
                            LogUtil.printError(e);
                        }
                    }
                }


        );
    }

}
