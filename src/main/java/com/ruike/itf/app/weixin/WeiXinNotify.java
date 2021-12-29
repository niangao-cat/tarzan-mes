package com.ruike.itf.app.weixin;

import com.alibaba.fastjson.JSON;
import com.ruike.itf.app.weixin.vo.TaskCardP;

import java.util.HashMap;
import java.util.Map;

/**
 * create by kejin.liu@hand-china.com
 * create date 2019年06月27日15:29:44
 * desc 微信企业号推送个人消息
 */

public class WeiXinNotify {
    public static final String WX_APP_ID = "wwacda7fc3f4b6cc4a";// 企业号ID
    public static final String WX_SECRET = "NVeR-eDGpW5GwiqWWxrORYL3uAsYK2iunpiGeUPVTow";// 自建应用Secret
    public static final String AGENT_ID = "1000002";//应用id
    public static final String ACTION = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=";//发送消息地址

    public static Map<String, Object> WXAccessToken = new HashMap<String, Object>();//微信AccessToken


    /**
     * 消息发送
     *
     * @param taskCard     消息内容
     */
    public static void sendTextMessageToUser(TaskCardP taskCard) {
        // 获取的Token 防止到map中，后期需要放到redis里面并且设置过期时间为2小时
        WXAccessToken = AccessToken.getWeinXinToken(WX_APP_ID, WX_SECRET);
        String accessToken = (String) WXAccessToken.get("accessToken");

        try {
            taskCard.setAgentid(AGENT_ID);
            String post = RemoteServiceUtil.post(ACTION + accessToken, JSON.toJSONString(taskCard));
            System.out.println("POST:" + post);

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

//    public static void main(String[] args) {
//
//
//        TaskCardP taskCard = new TaskCardP();
//        taskCard.setAgentid(AGENT_ID);
//        taskCard.setTouser("LiuKeJin");
//        String content = "礼品：A31茶具套装<br>用途：赠与小黑科技张总经理";
//        taskCard.setTaskcard(content);
//        sendTextMessageToUser(JSON.toJSONString(taskCard));
//    }
}
