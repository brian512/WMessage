package com.brian.wmessage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.base.BaseFragment;
import com.brian.wmessage.bmob.BmobHelper;
import com.brian.wmessage.entity.UserInfo;
import com.brian.wmessage.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.newim.BmobIM;
import cn.bmob.v3.BmobUser;

/**
 * @author huamm
 */
public class SetFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    ImageView mHeadView;
    @BindView(R.id.tv_name)
    TextView mNameView;
    @BindView(R.id.tv_logout)
    TextView mLogoutTv;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootLy = inflater.inflate(R.layout.profile_fragment_layout, null);
        ButterKnife.bind(this, rootLy);

        UserInfo userInfo = BmobHelper.getInstance().getCurrentUser();

        userInfo.showHead(mHeadView);
        mNameView.setText(userInfo.getUsername());
        mLogoutTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        return rootLy;
    }

    private void logout() {
        BmobUser.logOut();
        BmobIM.getInstance().disConnect();
        LoginActivity.startActivity(getContext());
        getActivity().finish();
    }
}
