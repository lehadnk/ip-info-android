package com.lehadnk.ipinformation.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.lehadnk.ipinformation.R;
import com.lehadnk.ipinformation.domain.GetIpAddressInformationUseCase;
import com.lehadnk.ipinformation.domain.dto.GetIpAddressInformationResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class EnteredIpAddressInformationFragment extends Fragment {
    private IpAddressDetailsBoxFragment ipAddressInformationFragment;
    private FrameLayout ipAddressInformationFragmentContainer;
    private TextInputEditText ipAddressInput;
    private Button getIpInformationButton;
    private TextView errorText;
    private ProgressBar progressBar;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private GetIpAddressInformationUseCase getIpAddressInformationUseCase = new GetIpAddressInformationUseCase(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_get_ip_address_information, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState)
    {
        this.ipAddressInformationFragment = (IpAddressDetailsBoxFragment) this.getChildFragmentManager().findFragmentById(R.id.ipAddressInformationFragment2);
        this.ipAddressInformationFragmentContainer = view.findViewById(R.id.ipAddressInformationFragment2);

        this.ipAddressInput = view.findViewById(R.id.ipAddressInput);
        this.errorText = view.findViewById(R.id.getIpAddressInformationError);
        this.getIpInformationButton = view.findViewById(R.id.getIpInformationButton);
        this.progressBar = view.findViewById(R.id.progressBar2);
        this.getIpInformationButton.setOnClickListener(e -> {
            this.startPollingData();
        });
    }

    private boolean isIpv4Address(String ip)
    {
        var ipv4Pattern = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\." +
                "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        var pattern = Pattern.compile(ipv4Pattern);

        return pattern.matcher(ip).matches();
    }

    private void startPollingData() {
        this.blockUi();

        var inputIp = this.ipAddressInput.getText().toString();
        if (!this.isIpv4Address(inputIp)) {
            this.processResult(GetIpAddressInformationResult.error(getString(R.string.incorrect_ip_address_format)));
            return;
        }

        new Thread(() -> {
            GetIpAddressInformationResult futureResult = null;
            try {
                futureResult = this.getIpAddressInformationUseCase.getIpAddressInformation(InetAddress.getByName(inputIp)).get(10, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException e) {
                futureResult = GetIpAddressInformationResult.error(getString(R.string.unknown_error));
            } catch (TimeoutException e) {
                futureResult = GetIpAddressInformationResult.error(getString(R.string.unable_to_reach_ipinfo));
            } catch (UnknownHostException e) {
                futureResult = GetIpAddressInformationResult.error(getString(R.string.incorrect_ip_address_format));
            }
            this.processResult(futureResult);
        }).start();
    }

    private void processResult(GetIpAddressInformationResult result)
    {
        this.handler.post(() -> {
            if (result.isSuccess) {
                this.ipAddressInformationFragment.update(result.ipAddress);
                this.ipAddressInformationFragmentContainer.setVisibility(View.VISIBLE);
            } else {
                this.errorText.setText(result.errorMessage);
                this.errorText.setVisibility(View.VISIBLE);
            }

            this.unblockUi();
        });
    }

    private void blockUi()
    {
        this.ipAddressInput.setEnabled(false);
        this.getIpInformationButton.setEnabled(false);
        this.errorText.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.ipAddressInformationFragmentContainer.setVisibility(View.GONE);
    }

    private void unblockUi()
    {
        this.ipAddressInput.setEnabled(true);
        this.getIpInformationButton.setEnabled(true);
        this.progressBar.setVisibility(View.GONE);
    }
}
