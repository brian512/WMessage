package com.brian.common.datacenter.network;

/**
 * 各个请求的域名配置
 */
public class HostConfig {

    /**
     * API请求域名
     */
    private static final String API_HOST_TEST = "http://test.api.rokkapp.com";
    private static final String API_HOST = "http://api.rokkapp.com";


    /**
     * 获得API请求域名
     */
    public static String getApiHost() {
        return API_HOST;
    }
}
