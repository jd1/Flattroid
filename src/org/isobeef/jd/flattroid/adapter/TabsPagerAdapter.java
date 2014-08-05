package org.isobeef.jd.flattroid.adapter;


import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;

import org.isobeef.jd.flattroid.fragment.TitleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jo on 05.08.14.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter
        implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
    private final Context mContext;
    private final ActionBar mActionBar;
    private final ViewPager mViewPager;
    private final List<TabInfo> mTabs = new ArrayList<TabInfo>();

    public static final class TabInfo implements Parcelable {
        private final Class<? extends  Fragment> clss;
        private final Bundle args;
        private final String title;

        TabInfo(Class<? extends Fragment> _class, Bundle _args, String title) {
            clss = _class;
            args = _args;
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public Class<? extends  Fragment>  getClazz() {
            return clss;
        }

        public Bundle getArgs() {
            return args;
        }

        protected TabInfo(Parcel in) {
            clss = (Class) in.readValue(Class.class.getClassLoader());
            args = in.readBundle();
            title = in.readString();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeValue(clss);
            dest.writeBundle(args);
            dest.writeString(title);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<TabInfo> CREATOR = new Parcelable.Creator<TabInfo>() {
            @Override
            public TabInfo createFromParcel(Parcel in) {
                return new TabInfo(in);
            }

            @Override
            public TabInfo[] newArray(int size) {
                return new TabInfo[size];
            }
        };
    }

    public TabsPagerAdapter(ActionBarActivity activity, ViewPager pager) {
        super(activity.getSupportFragmentManager());
        mContext = activity;
        mActionBar = activity.getSupportActionBar();
        mViewPager = pager;
        mViewPager.setAdapter(this);
        mViewPager.setOnPageChangeListener(this);
    }

    public void addTab(TabInfo info) {
        ActionBar.Tab tab = mActionBar.newTab();
        tab.setText(info.title);
        tab.setTag(info);
        tab.setTabListener(this);
        mTabs.add(info);
        notifyDataSetChanged();
        mActionBar.addTab(tab);
    }

    public void addTab(String title, Class<? extends Fragment> clss, Bundle args) {
        addTab(new TabInfo(clss, args, title));
    }



    @Override
    public int getCount() {
        return mTabs.size();
    }

    @Override
    public Fragment getItem(int position) {
        TabInfo info = mTabs.get(position);
        return Fragment.instantiate(mContext, info.clss.getName(), info.args);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        mActionBar.setSelectedNavigationItem(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        Object tag = tab.getTag();
        for (int i=0; i<mTabs.size(); i++) {
            if (mTabs.get(i) == tag) {
                mViewPager.setCurrentItem(i);
            }
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
    }

    public ArrayList<TabInfo> getTabs() {
        return new ArrayList<TabInfo>(mTabs);
    }
}
