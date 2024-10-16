package com.lehadnk.ipinformation.data.repositories;

import com.lehadnk.ipinformation.data.data_sources.IpinfoRemoteDataSource;
import com.lehadnk.ipinformation.data.dto.IpAddress;
import com.lehadnk.ipinformation.data.exceptions.RemoteDataFetchException;
import com.lehadnk.ipinformation.data.mappers.IpAddressMapper;

import java.net.InetAddress;

public class IpinfoRepository {
    private IpinfoRemoteDataSource dataSource = new IpinfoRemoteDataSource();
    private IpAddressMapper mapper = IpAddressMapper.INSTANCE;

    public IpAddress getIpAddressInformation(InetAddress inetAddress) throws RemoteDataFetchException {
        return this.mapper.mapIpAddress(
                inetAddress == null ? this.dataSource.getForCurrentIpAddress() : this.dataSource.getForIpAddress(inetAddress)
        );
    }
}
