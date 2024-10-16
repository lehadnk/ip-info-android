package com.lehadnk.ipinformation.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.net.InetAddress;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IpAddressApiModel {
    public InetAddress ip;
    public String city;
    public String country;
    public String org;
}
