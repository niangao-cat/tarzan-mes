package com.ruike.itf.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * <p>
 * 质量文件解析服务器配置
 * </p>
 *
 * @author Aidan.Zhu yonghui.zhu@hand-china.com 2021/4/6 10:50
 */
@Configuration
public class QualityAnalyzeFileServerConfig {
    @Value("${hwms.server.qualityAnalyze.host}")
    private String host;
    @Value("${hwms.server.qualityAnalyze.port}")
    private Integer port;
    @Value("${hwms.server.qualityAnalyze.user}")
    private String user;
    @Value("${hwms.server.qualityAnalyze.password}")
    private String password;

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }
}
