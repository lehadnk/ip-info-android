package com.lehadnk.ipinformation.domain;



import android.content.Context;

import com.lehadnk.ipinformation.R;
import com.lehadnk.ipinformation.data.exceptions.RemoteDataFetchException;
import com.lehadnk.ipinformation.data.repositories.IpinfoRepository;
import com.lehadnk.ipinformation.domain.dto.GetIpAddressInformationResult;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GetIpAddressInformationUseCase
{
    private final Context context;
    private IpinfoRepository ipinfoRepository = new IpinfoRepository();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public GetIpAddressInformationUseCase(
            Context context
    ) {
        this.context = context;
    }

    public Future<GetIpAddressInformationResult> getIpAddressInformation(InetAddress ipAddress)
    {
        return this.executorService.submit(() -> {
            try {
                return GetIpAddressInformationResult.success(this.ipinfoRepository.getIpAddressInformation(ipAddress));
            } catch (RemoteDataFetchException e) {
                return GetIpAddressInformationResult.error(this.context.getString(R.string.unable_to_reach_ipinfo));
            }
        });
    }
}
