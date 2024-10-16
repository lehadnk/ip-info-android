package com.lehadnk.ipinformation.domain.dto;

import com.lehadnk.ipinformation.data.dto.IpAddress;

public class GetIpAddressInformationResult {
    public IpAddress ipAddress;
    public Boolean isSuccess;
    public String errorMessage;

    public static GetIpAddressInformationResult success(IpAddress ipAddress)
    {
        var result = new GetIpAddressInformationResult();
        result.ipAddress = ipAddress;
        result.isSuccess = true;

        return result;
    }

    public static GetIpAddressInformationResult error(String errorMessage)
    {
        var result = new GetIpAddressInformationResult();
        result.errorMessage = errorMessage;
        result.isSuccess = true;

        return result;
    }
}
