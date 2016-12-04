package com.nbicc.gywlw.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by BigMao on 2016/12/3.
 */
@Component
@ConfigurationProperties(prefix = "iotcloud",locations={"classpath:iotcloud.properties"})
public class PropertyConfig {
    //	@Value("#{iotcloud.localPort}")
    private String localPort;

    private String upgrade_ip;

    private String upgrade_port;

    private String mongodb;

    //	@Value("#{iotcloud.serviceIp}")
    private String serviceIp;

    //	@Value("#{iotcloud.localIp}")
    private String localIp;

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public String getServiceIp() {
        return serviceIp;
    }

    public void setServiceIp(String serviceIp) {
        this.serviceIp = serviceIp;
    }

    public String getMongodb() {
        return mongodb;
    }

    public void setMongodb(String mongodb) {
        this.mongodb = mongodb;
    }

    public String getUpgrade_ip() {
        return upgrade_ip;
    }

    public void setUpgrade_ip(String upgrade_ip) {
        this.upgrade_ip = upgrade_ip;
    }

    public String getUpgrade_port() {
        return upgrade_port;
    }

    public void setUpgrade_port(String upgrade_port) {
        this.upgrade_port = upgrade_port;
    }

    public String getLocalPort() {
        return localPort;
    }

    public void setLocalPort(String localPort) {
        this.localPort = localPort;
    }
}
