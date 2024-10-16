package com.lehadnk.ipinformation.data.data_sources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lehadnk.ipinformation.data.dto.IpAddressApiModel;
import com.lehadnk.ipinformation.data.exceptions.RemoteDataFetchException;

import java.io.IOException;
import java.net.InetAddress;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class IpinfoRemoteDataSource {
    public IpAddressApiModel getForCurrentIpAddress() throws RemoteDataFetchException {
        return this.request("");
    }

    public IpAddressApiModel getForIpAddress(InetAddress ipAddress) throws RemoteDataFetchException {
        return this.request(ipAddress.toString() + "/");
    }

    private IpAddressApiModel request(String method) throws RemoteDataFetchException {
        var url = "https://ipinfo.io/" + method + "json";
        var request = new Request.Builder().url(url).build();
        var client = new OkHttpClient.Builder().build();

        try {
            var response = client.newCall(request).execute();
            if (response.code() != 200) {
                throw new RemoteDataFetchException("IPInfo returned non-200 code (" + response.code() + "): " + response.body().toString());
            }

            var objectMapper = new ObjectMapper();
            return objectMapper.readValue(response.body().string(), IpAddressApiModel.class);
        } catch (IOException e) {
            throw new RemoteDataFetchException("Error receiving data from ipinfo: " + e.toString());
        }
    }
}
