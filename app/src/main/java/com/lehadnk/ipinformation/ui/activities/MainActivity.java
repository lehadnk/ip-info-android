package com.lehadnk.ipinformation.ui.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.lehadnk.ipinformation.R;
import com.lehadnk.ipinformation.ui.adapters.TabsPagesAdapter;

public class MainActivity extends AppCompatActivity
{
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public void onCreate(Bundle savedInstanceBundle)
    {
        super.onCreate(savedInstanceBundle);
        this.setContentView(R.layout.activity_main);

        this.tabLayout = this.findViewById(R.id.tabLayout);
        this.viewPager = this.findViewById(R.id.viewPager);

        var tabsPagesAdapter = new TabsPagesAdapter(this);
        viewPager.setAdapter(tabsPagesAdapter);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(getString(R.string.my_ip));
                    break;
                case  1:
                    tab.setText(getString(R.string.other_ip));
                    break;
            }
        }).attach();
    }
}
