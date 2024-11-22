package com.lehadnk.ipinformation.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lehadnk.ipinformation.ui.fragments.EnteredIpAddressInformationFragment;
import com.lehadnk.ipinformation.ui.fragments.MyIpAddressFragment;

public class TabsPagesAdapter extends FragmentStateAdapter {
    public TabsPagesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new MyIpAddressFragment();
            case 1:
                return new EnteredIpAddressInformationFragment();
            default:
                return new MyIpAddressFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
