package com.ruike.itf.app.weixin;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.httpclient.params.HttpMethodParams;
/**
 * create by kejin.liu@hand-china.com
 * create date 2019年06月27日15:29:44
 * desc 微信企业号推送消息
 */
public class RemoteServiceUtil {
    /**
     *
     * @param serviceUrl 消息发送URL
     * @param jsonData 发送信息
     * @return
     * @throws Exception
     */
    public static String post(String serviceUrl, String jsonData) throws Exception {
        HttpClient httpClient = new HttpClient();
        PostMethod method = null;
        try {
            method = new PostMethod(serviceUrl);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            if (jsonData != null) {
                RequestEntity requestEntity = new StringRequestEntity(jsonData, "text/xml", "UTF-8");
                method.setRequestEntity(requestEntity);
            }

            //执行
            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                //读取内容
                String ResponseHTML = method.getResponseBodyAsString();
                return ResponseHTML;
            } else if (statusCode == HttpStatus.SC_NO_CONTENT) {
                return "";
            }
            throw new Exception("Method failed: " + method.getStatusLine());

        } catch (Exception e) {
            throw e;
        } finally {
            //释放连接
            if (method != null) {
                method.releaseConnection();
            }
        }
    }


    /**
     *
     * @param serviceUrl 获取Token
     * @return Token
     * @throws Exception
     */
    public static String get(String serviceUrl) throws Exception {
        HttpClient httpClient = new HttpClient();
        GetMethod method = null;
        try {
            String postUrl = serviceUrl;

            method = new GetMethod(postUrl);
            method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
            //执行
            int statusCode = httpClient.executeMethod(method);
            if (statusCode == HttpStatus.SC_OK) {
                //读取内容
                String ResponseHTML = method.getResponseBodyAsString();
                return ResponseHTML;
            } else if (statusCode == HttpStatus.SC_NO_CONTENT) {
                return "";
            }
            throw new Exception("Method failed: " + method.getStatusLine());

        } catch (Exception e) {
            throw e;
        } finally {
            //释放连接
            if (method != null) {
                method.releaseConnection();
            }
        }
    }
}
