package com.lehadnk.ipinformation.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.lehadnk.ipinformation.R;
import com.lehadnk.ipinformation.data.dto.IpAddress;
import com.murgupluoglu.flagkit.FlagKit;

public class IpAddressDetailsBoxFragment extends Fragment {
    private TextView ipAddress;
    private ImageView flagImage;
    private TextView carrierName;
    private ViewGroup container;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        this.container = container;

        this.view = inflater.inflate(R.layout.fragment_ip_address_information, container, false);
        this.ipAddress = view.findViewById(R.id.ipAddress);
        this.flagImage = view.findViewById(R.id.flagImage);
        this.carrierName = view.findViewById(R.id.carrierName);

        return view;
    }

    public void update(IpAddress ipAddress)
    {
        this.ipAddress.setText(ipAddress.ip.getHostAddress());
        if (ipAddress.countryIsoCode != null) {
            this.flagImage.setImageDrawable(FlagKit.INSTANCE.getDrawable(this.getContext(), ipAddress.countryIsoCode.toLowerCase()));
            this.flagImage.setVisibility(View.VISIBLE);
        } else {
            this.flagImage.setVisibility(View.GONE);
        }

        this.carrierName.setText(ipAddress.carrierName != null ? ipAddress.carrierName : getString(R.string.no_provider_information));
        this.container.setVisibility(View.VISIBLE);
        this.resizeTextContainer();
    }

    /**
     * There's no way to do it during onCreateView since the element is hidden yet
     */
    private void resizeTextContainer()
    {
        var viewTreeObserver = view.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                view.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                int fragmentWidth = view.getWidth();
                int maxWidth = (int) (fragmentWidth * 0.7);
                carrierName.setMaxWidth(maxWidth);
            }
        });
    }
}
