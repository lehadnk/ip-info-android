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

import com.google.android.material.textfield.TextInputEditText;
import com.lehadnk.ipinformation.R;
import com.lehadnk.ipinformation.domain.GetIpAddressInformationUseCase;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class GetIpAddressInformationFragment extends Fragment {
    private IpAddressInformationFragment ipAddressInformationFragment;
    private TextInputEditText ipAddressInput;
    private Button getIpInformationButton;
    private TextView errorText;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private GetIpAddressInformationUseCase getIpAddressInformationUseCase = new GetIpAddressInformationUseCase();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        var view = inflater.inflate(R.layout.fragment_get_ip_address_information, container, false);

        this.ipAddressInformationFragment = (IpAddressInformationFragment) this.getChildFragmentManager().findFragmentById(R.id.fragmentContainerView2);
        this.ipAddressInput = view.findViewById(R.id.ipAddressInput);
        this.errorText = view.findViewById(R.id.getIpAddressInformationError);
        this.getIpInformationButton = view.findViewById(R.id.getIpInformationButton);
        this.getIpInformationButton.setOnClickListener(e -> {
            this.startPollingData();
        });

        return view;
    }

    private void startPollingData() {
        this.handler.postDelayed(() -> {
            try {
                this.blockUi();

                var inputIp = this.ipAddressInput.getText().toString();
                if (!this.isIpv4Address(inputIp)) {
                    this.errorText.setText("Введенный IP адрес не является корректным. Пожалуйста, введите четыре числа в диапазоне 0 - 255, разделенные точками.");
                    this.errorText.setVisibility(View.VISIBLE);
                    this.unblockUi();
                    return;
                }

                var ipAddress = InetAddress.getByName(inputIp);

                var futureResult = this.getIpAddressInformationUseCase.getIpAddressInformation(ipAddress).get(4, TimeUnit.SECONDS);
                if (futureResult.isSuccess) {
                    this.ipAddressInformationFragment.update(futureResult.ipAddress);
                } else {
                    this.errorText.setText("Ошибка при получении данных от сервиса IPInfo. Пожалуйста, проверьте интернет-подключение.");
                    this.errorText.setVisibility(View.VISIBLE);
                }
            } catch (ExecutionException | InterruptedException e) {
                this.errorText.setText("Неизвестная ошибка. Пожалуйста, попробуйте еще раз.");
                this.errorText.setVisibility(View.VISIBLE);
            } catch (UnknownHostException e) {
                this.errorText.setText("Введенный IP адрес не является корректным. Пожалуйста, введите четыре числа в диапазоне 0 - 255, разделенные точками.");
                this.errorText.setVisibility(View.VISIBLE);
            } catch (TimeoutException e) {
                this.errorText.setText("Ошибка при получении данных от сервиса IPInfo. Пожалуйста, проверьте интернет-подключение.");
                this.errorText.setVisibility(View.VISIBLE);
            } finally {
                this.unblockUi();
            }
        }, 0);
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

    private void blockUi()
    {
        this.ipAddressInput.setEnabled(false);
        this.getIpInformationButton.setEnabled(false);
        this.errorText.setVisibility(View.GONE);
    }

    private void unblockUi()
    {
        this.ipAddressInput.setEnabled(true);
        this.getIpInformationButton.setEnabled(true);
    }
}
