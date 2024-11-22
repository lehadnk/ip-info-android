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

import com.lehadnk.ipinformation.R;
import com.lehadnk.ipinformation.domain.GetIpAddressInformationUseCase;
import com.lehadnk.ipinformation.domain.dto.GetIpAddressInformationResult;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyIpAddressFragment extends Fragment {
    private Button refreshButton;
    private IpAddressDetailsBoxFragment ipAddressInformationFragment;
    private FrameLayout ipAddressInformationFragmentContainer;
    private TextView errorText;
    private ProgressBar progressBar;
    private GetIpAddressInformationUseCase getIpAddressInformationUseCase = new GetIpAddressInformationUseCase();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_get_my_ip, container, false);
    }

    @Override
    public void onViewCreated(@NotNull View view, @Nullable Bundle savedInstanceState)
    {
        this.ipAddressInformationFragment = (IpAddressDetailsBoxFragment) this.getChildFragmentManager().findFragmentById(R.id.ipAddressFragmentContainer);
        this.ipAddressInformationFragmentContainer = view.findViewById(R.id.ipAddressFragmentContainer);

        this.errorText = view.findViewById(R.id.getMyIpAddressErrorText);
        this.refreshButton = view.findViewById(R.id.refreshButton);
        this.refreshButton.setOnClickListener(e -> {
            this.startPollingData();
        });
        this.progressBar = view.findViewById(R.id.progressBar);

        this.startPollingData();
    }

    public void startPollingData()
    {
        this.blockUi();

        new Thread(() -> {
            GetIpAddressInformationResult futureResult = null;
            try {
                futureResult = this.getIpAddressInformationUseCase.getIpAddressInformation(null).get(10, TimeUnit.SECONDS);
            } catch (ExecutionException | InterruptedException e) {
                futureResult = GetIpAddressInformationResult.error("Неизвестная ошибка. Пожалуйста, попробуйте еще раз.");
            } catch (TimeoutException e) {
                futureResult = GetIpAddressInformationResult.error("Ошибка при получении данных от сервиса IPInfo. Пожалуйста, проверьте интернет-подключение.");
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
        this.refreshButton.setEnabled(false);
        this.errorText.setVisibility(View.GONE);
        this.progressBar.setVisibility(View.VISIBLE);
        this.ipAddressInformationFragmentContainer.setVisibility(View.GONE);
    }

    private void unblockUi()
    {
        this.refreshButton.setEnabled(true);
        this.progressBar.setVisibility(View.GONE);
    }
}
