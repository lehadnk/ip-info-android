package com.lehadnk.ipinformation.ui.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.lehadnk.ipinformation.R;
import com.lehadnk.ipinformation.domain.GetIpAddressInformationUseCase;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class GetMyIpFragment extends Fragment {
    private Button refreshButton;
    private IpAddressInformationFragment ipAddressInformationFragment;
    private TextView errorText;

    private GetIpAddressInformationUseCase getIpAddressInformationUseCase = new GetIpAddressInformationUseCase();
    private final Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        var view = inflater.inflate(R.layout.fragment_get_my_ip, container, false);

        this.ipAddressInformationFragment = (IpAddressInformationFragment) this.getChildFragmentManager().findFragmentById(R.id.fragmentContainerView);
        this.errorText = view.findViewById(R.id.getMyIpAddressErrorText);
        this.refreshButton = view.findViewById(R.id.refreshButton);
        this.refreshButton.setOnClickListener(e -> {
            this.startPollingData();
        });

        this.startPollingData();

        return view;
    }

    public void startPollingData()
    {
        this.handler.postDelayed(() -> {
            try {
                this.blockUi();

                var futureResult = this.getIpAddressInformationUseCase.getIpAddressInformation(null).get(4, TimeUnit.SECONDS);
                if (futureResult.isSuccess) {
                    this.ipAddressInformationFragment.update(futureResult.ipAddress);
                } else {
                    this.errorText.setText("Ошибка при получении данных от сервиса IPInfo. Пожалуйста, проверьте интернет-подключение.");
                    this.errorText.setVisibility(View.VISIBLE);
                }
            } catch (TimeoutException e) {
                this.errorText.setText("Ошибка при получении данных от сервиса IPInfo. Пожалуйста, проверьте интернет-подключение.");
                this.errorText.setVisibility(View.VISIBLE);
            } catch (Exception e) {
                this.errorText.setText("Неизвестная ошибка. Пожалуйста, попробуйте еще раз.");
                this.errorText.setVisibility(View.VISIBLE);
            } finally {
                this.unblockUi();
            }
        }, 0);
    }

    private void blockUi()
    {
        this.refreshButton.setEnabled(false);
        this.errorText.setVisibility(View.GONE);
    }

    private void unblockUi()
    {
        this.refreshButton.setEnabled(true);
    }
}
