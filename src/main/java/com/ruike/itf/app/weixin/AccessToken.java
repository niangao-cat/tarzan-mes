package com.ruike.itf.app.weixin;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import static com.ruike.mdm.infra.constant.MdmConstant.ConstantValue.STRING_ZERO;
import static com.ruike.mdm.infra.constant.MdmConstant.ConstantValue.ZERO;

/**
 * create by kejin.liu@hand-china.com
 * create date 2019年06月27日15:29:44
 * desc 获取微信Token
 */

public class AccessToken {
    public static final String GET_TOKEN_WeiXin_URL = "https://qyapi.weixin.qq.com/cgi-bin/gettoken";// 获取access// url

    /**
     * 获取微信token
     *
     * @param corpid     企业唯一corpid（企业微信设置中查看）
     * @param corpsecret 自建应用中查看
     * @return token
     */
    public static Map<String, Object> getWeinXinToken(String corpid, String corpsecret) {
        String accessToken = null;
        String requestUrl = GET_TOKEN_WeiXin_URL + "?corpid=" + corpid + "&corpsecret=" + corpsecret;
        Map<String, Object> wxAccessToken = new HashMap<String, Object>();//微信AccessToken
        String result = "";
        try {
            result = RemoteServiceUtil.get(requestUrl);

            JSONObject json = new JSONObject(result);

            String msg = json.getString("errcode");
            if (STRING_ZERO.equals(msg)) {
                accessToken = json.getString("access_token");
                Calendar now = Calendar.getInstance();
                now.add(Calendar.MINUTE, 90);//accessToken 两个小时过期，这里设置有效期90分钟
                wxAccessToken.put("accessToken", accessToken);
                wxAccessToken.put("time", now.getTimeInMillis());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return wxAccessToken;
    }


}
