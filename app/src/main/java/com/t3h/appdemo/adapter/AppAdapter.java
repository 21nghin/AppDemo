package com.t3h.appdemo.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class AppAdapter extends FragmentPagerAdapter {

    private Fragment[] frmData;

    public void setFrmData(Fragment[] frmData) {
        this.frmData = frmData;
        notifyDataSetChanged();
    }

    public AppAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return frmData[position];
    }

    @Override
    public int getCount() {
        return frmData == null ? 0 : frmData.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Bảng tin";
            case 1:
                return "Đã lưu";
            default:
                return "Thông báo";
        }
    }
}
