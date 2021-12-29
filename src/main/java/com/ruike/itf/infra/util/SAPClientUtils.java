package com.ruike.itf.infra.util;

import com.ruike.itf.api.dto.SapClientModelDTO;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.ext.DestinationDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * SAP连接工具类
 *
 * @author jiangling.zheng@hand-china.com 2020-07-16 14:39:06
 */
@Component
public class SAPClientUtils {
    @Autowired
    private SapClientModelDTO sapClientModel;

    /**
     * 获取SAP连接
     *
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:27
     * @return com.sap.conn.jco.JCoDestination
     */
    public JCoDestination getNewJcoConnection() throws JCoException {

        File cfg = new File("SapConfig.jcoDestination");
        if (cfg.exists()){
            cfg.delete();
        }
        if (!cfg.exists()) {
            Properties properties = new Properties();
            properties.setProperty(DestinationDataProvider.JCO_ASHOST, sapClientModel.getAshost());// sap服务器地址
            properties.setProperty(DestinationDataProvider.JCO_SYSNR, sapClientModel.getSysnr());// 系统编号
            properties.setProperty(DestinationDataProvider.JCO_CLIENT, sapClientModel.getClient());// 集团号，
            properties.setProperty(DestinationDataProvider.JCO_USER, sapClientModel.getUser());// 帐号
            properties.setProperty(DestinationDataProvider.JCO_PASSWD, sapClientModel.getPasswd());// 密码
            properties.setProperty(DestinationDataProvider.JCO_LANG, sapClientModel.getLang());// 语言
            properties.setProperty(DestinationDataProvider.JCO_POOL_CAPACITY,"2");

            try {
                FileOutputStream fos = new FileOutputStream(cfg, false);
                properties.store(fos, "properties for sap connection !");
                fos.close();
            } catch (Exception e) {
                throw new RuntimeException("Unable to create the destination file " + cfg.getName(), e);
            }
        }
        JCoDestination jCoDestination = JCoDestinationManager.getDestination("SapConfig");
        return jCoDestination;
    }

    /**
     * 获取SAP代理对象
     *
     * @param destination
     * @param functionName
     * @author jiangling.zheng@hand-china.com 2020/7/16 15:27
     * @return com.sap.conn.jco.JCoFunction
     */
    public static JCoFunction CreateFunction(JCoDestination destination, String functionName) throws JCoException {
        JCoFunction function = destination.getRepository().getFunction(functionName);
        return function;
    }
}
