package com.lehadnk.ipinformation.ui.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lehadnk.ipinformation.ui.fragments.GetIpAddressInformationFragment;
import com.lehadnk.ipinformation.ui.fragments.GetMyIpFragment;

public class TabsPagesAdapter extends FragmentStateAdapter {
    public TabsPagesAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new GetMyIpFragment();
            case 1:
                return new GetIpAddressInformationFragment();
            default:
                return new GetMyIpFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
