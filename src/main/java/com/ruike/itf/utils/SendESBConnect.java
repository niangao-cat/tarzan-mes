package com.ruike.itf.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ruike.itf.domain.vo.ItfEsbRequestVO;
import com.ruike.itf.domain.vo.ItfEsbResponseVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.util.JsonUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.RequestPayloadDTO;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.interfaces.sdk.invoke.InterfaceInvokeSdk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Component
public class SendESBConnect {

    private final InterfaceInvokeSdk interfaceInvokeSdk;

    @Value("${hwms.interface.defaultNamespace}")
    private String namespace;

    /**
     * HTTP请求成功
     */
    private final Integer HTTP_STATUS_OK = 200;

    public SendESBConnect(InterfaceInvokeSdk interfaceInvokeSdk) {
        this.interfaceInvokeSdk = interfaceInvokeSdk;
    }




    /**
     * 与ESB发送数据
     *
     * @param requestMap
     * @return
     * @author jiangling.zheng@hand-china.com 2020/8/14 16:25
     */
    public Map<String, Object> sendEsb(Object requestMap , String dataName, String logName, String itfPath) {
        ItfEsbRequestVO itfEsbRequestVO = new ItfEsbRequestVO(new Date());
        Map<String, Object> requestInfo = new HashMap<>(WmsConstant.ConstantValue.MAP_DEFAULT_CAPACITY);
        RequestPayloadDTO requestPayload = new RequestPayloadDTO();
        // JsonUtils.objToJson 生成json串 会改变属性大小写 比如 VBELN -> vbeln fastJson 加注解JSONField不会改变
        if (StringUtils.isNotBlank(dataName)) {
            requestInfo.put(dataName, requestMap);
            itfEsbRequestVO.setRequestInfo(requestInfo);
            requestPayload.setPayload(JsonUtils.objToJson(itfEsbRequestVO));
        } else {
            requestInfo = (Map<String, Object>) requestMap;
            itfEsbRequestVO.setRequestInfo(requestInfo);
            requestPayload.setPayload(JSONObject.toJSONString(itfEsbRequestVO));
        }
        log.info("<==== " + logName + " requestPayload: {}", requestPayload.toString());

        // 请求接口
        ResponsePayloadDTO responsePayload = new ResponsePayloadDTO();
        try {
            responsePayload = interfaceInvokeSdk.invoke(namespace,
                    ItfConstant.ServerCode.MES_ESB,
                    itfPath,
                    requestPayload);
        } catch (Exception e) {
            //rollbackIfaceStatus(userId, ifaceList);
            throw e;
        }

        if (Objects.isNull(responsePayload)) {
            log.error("<==== " + logName + " Error requestPayload: {}ESB接口调用失败", requestPayload.toString());
            throw new CommonException("<==== " + logName + " Error requestPayload: {}ESB接口调用失败", requestPayload.toString());
        } else if (responsePayload.getStatusCodeValue() != HTTP_STATUS_OK || !HTTP_STATUS_OK.toString().equals(responsePayload.getStatus())) {
            log.error("<==== " + logName + " Error requestPayload: {}ESB接口调用失败,服务器不通", requestPayload.toString());
            throw new CommonException("<==== " + logName + " Error requestPayload: {}ESB接口调用失败,服务器不通", requestPayload.toString());
        } else {
            log.info("<==== " + logName + " Success responsePayload: {}", JSON.toJSON(responsePayload));
        }
        //解析返回报文
        ItfEsbResponseVO itfEsbResponseVO = JsonUtils.jsonToObject(responsePayload.getPayload(), ItfEsbResponseVO.class);
        if (Objects.isNull(itfEsbResponseVO)
                || Objects.isNull(itfEsbResponseVO.getEsbInfo()) || Objects.isNull(itfEsbResponseVO.getResultInfo())) {
            if (StringUtils.isEmpty(responsePayload.getPayload())) {
                log.error("<==== " + logName + " Success ESB {}接口返回结果为空！", responsePayload);
                throw new CommonException("ESB接口返回结果为空！报文内容：" + responsePayload.getPayload());
            } else {
                log.error("ESB接口返回报文解析失败！报文内容：" + responsePayload.getPayload());
                throw new CommonException("ESB接口返回报文解析失败！报文内容：" + responsePayload.getPayload());
            }
        }
        Map<String, Object> resultInfo = itfEsbResponseVO.getResultInfo();
        return resultInfo;

    }
}
