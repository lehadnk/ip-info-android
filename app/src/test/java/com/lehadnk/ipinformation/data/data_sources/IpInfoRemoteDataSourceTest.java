package com.lehadnk.ipinformation.data.data_sources;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.lehadnk.ipinformation.data.exceptions.RemoteDataFetchException;

import org.junit.Test;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpInfoRemoteDataSourceTest {
    @Test
    public void testSuccessResponse() throws UnknownHostException, RemoteDataFetchException {
        var dataSource = new IpinfoRemoteDataSource();
        var data = dataSource.getForIpAddress(InetAddress.getByName("8.8.8.8"));

        assertEquals("/8.8.8.8", data.ip.toString());
        assertEquals("US", data.country);
        assertEquals("Mountain View", data.city);
        assertEquals("AS15169 Google LLC", data.org);
    }

    @Test
    public void testGetCurrentIpAddress() throws RemoteDataFetchException {
        var dataSource = new IpinfoRemoteDataSource();
        var data = dataSource.getForCurrentIpAddress();

        assertNotNull(data.city);
        assertNotNull(data.country);
        assertNotNull(data.org);
        assertNotNull(data.ip);
    }
}
