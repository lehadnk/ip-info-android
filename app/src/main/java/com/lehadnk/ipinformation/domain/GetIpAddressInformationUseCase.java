package com.lehadnk.ipinformation.domain;

import com.lehadnk.ipinformation.data.exceptions.RemoteDataFetchException;
import com.lehadnk.ipinformation.data.repositories.IpinfoRepository;
import com.lehadnk.ipinformation.domain.dto.GetIpAddressInformationResult;

import java.net.InetAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class GetIpAddressInformationUseCase
{
    private IpinfoRepository ipinfoRepository = new IpinfoRepository();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public Future<GetIpAddressInformationResult> getIpAddressInformation(InetAddress ipAddress)
    {
        return this.executorService.submit(() -> {
            try {
                return GetIpAddressInformationResult.success(this.ipinfoRepository.getIpAddressInformation(ipAddress));
            } catch (RemoteDataFetchException e) {
                return GetIpAddressInformationResult.error("Не удалось загрузить данные с сервиса IPInfo. Проверьте подключение к интернету, после чего попробуйте еще раз");
            }
        });
    }
}
