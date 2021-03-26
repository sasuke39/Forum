package cn.bestzuo.zuoforum.admin.pojo;

import lombok.Data;


@Data
public class IpAddress {

    private String status;

    private String country;

    private String countryCode;

    private String region;

    private String regionName;

    private String city;

    private String zip;

    private String lat;

    private String lon;

    private String timezone;

    private String isp;

    private String org;

    private String as;

    private String query;
}
