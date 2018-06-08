
package com.brian.wmessage;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brian.wmessage.contact.ContactFragment;
import com.brian.wmessage.conversations.ConversationListFragment;

/**
 * 主页面Tab适配器
 * @author huamm
 */
public class MainTabAdapter extends FragmentPagerAdapter {
    
    private Fragment[] mFragments = null;

    public MainTabAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new Fragment[mTabTitles.length];
    }
    
    private String[] mTabTitles = new String[]{
            "聊天", "联系人", "我的"
    };
    // 获取项
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = mFragments[position];
        
        if (fragment != null) {
            return fragment;
        }

        switch (position) {
            case 0:
                fragment = ConversationListFragment.newInstance();
                break;
            case 1:
                fragment = new ContactFragment();
                break;
            case 2:
                fragment = new SetFragment();
                break;
        }
        
        mFragments[position] = fragment;
        
        return fragment;
    }

    @Override
    public String getPageTitle(int position) {
        // 返回页面标题
        return mTabTitles[position % mTabTitles.length];
    }

    @Override
    public int getCount() {
        // 页面个数
        return mTabTitles.length;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // 避免视图被销毁
//        super.destroyItem(container, position, object);
    }


    public View getTabView(Context context, int position) {
        View tabView = LayoutInflater.from(context).inflate(R.layout.item_tab_layout, null);
        TextView tabTitle = tabView.findViewById(R.id.tv_tab_title);
        tabTitle.setText(mTabTitles[position]);
        return tabView;
    }
}
