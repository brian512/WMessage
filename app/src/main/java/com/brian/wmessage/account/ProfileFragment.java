package com.brian.wmessage.account;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.brian.common.base.BaseFragment;
import com.brian.wmessage.R;
import com.brian.wmessage.imservice.bmob.BmobHelper;
import com.brian.wmessage.entity.UserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author huamm
 */
public class ProfileFragment extends BaseFragment {

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
        BmobHelper.getInstance().logout();
        LoginActivity.startActivity(getContext());
        getActivity().finish();
    }
}
