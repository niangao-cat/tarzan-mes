package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfWorkOrderTimeChangeDTO;
import com.ruike.itf.app.service.ItfWorkOrderTimeChangeService;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.utils.SendESBConnect;
import com.ruike.itf.utils.Utils;
import io.choerodon.core.exception.CommonException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @ClassName ItfWorkOrderTimeChangeServiceImpl
 * @Description 工单时间变更
 * @Author lkj
 * @Date 2020/12/17
 */
@Service
public class ItfWorkOrderTimeChangeServiceImpl implements ItfWorkOrderTimeChangeService {

    private final SendESBConnect sendESBConnect;

    public ItfWorkOrderTimeChangeServiceImpl(SendESBConnect sendESBConnect) {
        this.sendESBConnect = sendESBConnect;
    }

    @Override
    public List<ItfWorkOrderTimeChangeDTO> hmeWorkOrderTimeChange(Long tenantId, List<ItfWorkOrderTimeChangeDTO> dto) {
        if (Objects.isNull(dto)) {
            throw new CommonException("为空不允许发送数据！");
        }
        String[] field = new String[]{"workOrderNum", "startDate", "endDate"};
        List<Map<String, String>> sendList = new ArrayList<>();
        dto.forEach(isNull -> {
            List<String> objectFieldIsNull = Utils.objectFieldIsNull(isNull, field);
            if (!CollectionUtils.isEmpty(objectFieldIsNull)) {
                throw new CommonException(objectFieldIsNull + "不允许为空！");
            }
            sendList.add(new HashMap<String, String>() {{
                put("AUFNR", isNull.getWorkOrderNum());
                put("GSTRP", isNull.getStartDate().replace("-", ""));
                put("GLTRP", isNull.getEndDate().replace("-", ""));

            }});
        });

        Map<String, Object> returnData = sendESBConnect.sendEsb(sendList, "HEADER",
                "ItfWorkOrderTimeChangeServiceImpl.hmeWorkOrderTimeChange",
                ItfConstant.InterfaceCode.ESB_PRODUCTION_ORDER_UPDATE_SYNC);
        List<Map> resultList = JSONArray.parseArray(JSON.toJSONString(returnData.get("HEADER")), Map.class);
        List<ItfWorkOrderTimeChangeDTO> resultDTO = new ArrayList<>();
        resultList.forEach(result -> {
            if (!"S".equals(result.get("TYPE").toString())) {
                throw new CommonException(result.get("MESSAGE").toString());
            }
            resultDTO.add(new ItfWorkOrderTimeChangeDTO(result));
        });
        return resultDTO;
    }
}
