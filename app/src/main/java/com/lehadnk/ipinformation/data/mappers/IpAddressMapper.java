package com.lehadnk.ipinformation.data.mappers;

import com.lehadnk.ipinformation.data.dto.IpAddress;
import com.lehadnk.ipinformation.data.dto.IpAddressApiModel;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface IpAddressMapper {
    IpAddressMapper INSTANCE = Mappers.getMapper(IpAddressMapper.class);

    @Mapping(source = "city", target = "city")
    @Mapping(source = "country", target = "countryIsoCode")
    @Mapping(source = "org", target = "carrierName")
    IpAddress mapIpAddress(IpAddressApiModel addressApiModel);
}
