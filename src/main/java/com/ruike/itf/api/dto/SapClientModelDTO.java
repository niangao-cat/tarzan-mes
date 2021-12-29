package com.ruike.itf.api.dto;

import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * sap连接信息DTO
 *
 * @author jiangling.zheng@hand-china.com 2020/7/15 15:41
 */
@Component
@ToString
public class SapClientModelDTO {
    @Value(value = "${jco.client.lang}")
    private String lang;
    @Value(value = "${jco.client.client}")
    private String client;
    @Value(value = "${jco.client.passwd}")
    private String passwd;
    @Value(value = "${jco.client.user}")
    private String user;
    @Value(value = "${jco.client.sysnr}")
    private String sysnr;
    @Value(value = "${jco.client.ashost}")
    private String ashost;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getSysnr() {
        return sysnr;
    }

    public void setSysnr(String sysnr) {
        this.sysnr = sysnr;
    }

    public String getAshost() {
        return ashost;
    }

    public void setAshost(String ashost) {
        this.ashost = ashost;
    }

}
