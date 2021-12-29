package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.app.service.ItfLightTaskIfaceService;
import com.ruike.itf.domain.entity.ItfLightTaskIface;
import com.ruike.itf.domain.entity.ItfMaterialLotConfirmIface;
import com.ruike.itf.domain.repository.ItfLightTaskIfaceRepository;
import com.ruike.itf.domain.repository.ItfMaterialLotConfirmIfaceRepository;
import com.ruike.itf.domain.vo.*;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 亮灯指令接口表应用服务默认实现
 *
 * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
 */
@Service
@Slf4j
public class ItfLightTaskIfaceServiceImpl implements ItfLightTaskIfaceService {

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private ItfLightTaskIfaceRepository itfLightTaskIfaceRepository;
    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;
    @Autowired
    private ItfMaterialLotConfirmIfaceRepository itfMaterialLotConfirmIfaceRepository;

    @Override
    public List<ItfLightTaskIfaceVO> itfLightTaskIface(Long tenantId, List<ItfLightTaskIfaceDTO> dtoList) {
        //判断是新增还是取消,对传入参数进行整理.并写入接口表
        List<String> taskNumList = itfLightTaskIfaceRepository.insertitfLightTaskIface(tenantId,dtoList);
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("ITF.INTERNAL_FLAG", tenantId);
        if (CollectionUtils.isEmpty(lovValueList)){
            throw new CommonException("ITF.INTERNAL_FLAG值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        Optional<LovValueDTO> lovFlag = lovValueList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "TIMELY_INTERFACE_FLAG")).findFirst();
        String interfaceFlag = lovFlag.isPresent() ? lovFlag.get().getMeaning() : "";
        List<ItfLightTaskIfaceVO> resultList = new ArrayList<>();
        if (ItfConstant.ConstantValue.YES.equals(interfaceFlag)) {
            //根据taskNum查询传输的数据
            List<ItfLightTaskIface> itfLightTaskIfaces = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
            if(CollectionUtils.isEmpty(itfLightTaskIfaces)){
                throw new CommonException("接口MES方无传输数据");
            }
            List<String> ifaceIdList = itfLightTaskIfaces.stream().map(ItfLightTaskIface::getIfaceId).collect(Collectors.toList());
            List<ItfLightTaskIfaceVO2> itfLightTaskIfaceVO2s = new ArrayList<>();
            List<Map<String, Object>> itfLightTaskIfaceVO2List = new ArrayList<>();
            for(ItfLightTaskIface itfLightTaskIface:itfLightTaskIfaces){
                Map<String, Object> map = new HashMap<>(100);
                map.put("TASK_NUM",itfLightTaskIface.getTaskNum());
                map.put("LOCATOR_CODE",itfLightTaskIface.getLocatorLabelId());
                map.put("TASK_TYPE",itfLightTaskIface.getTaskType());
                map.put("TASK_STATUS",itfLightTaskIface.getTaskStatus());
                itfLightTaskIfaceVO2List.add(map);
//                ItfLightTaskIfaceVO2 itfLightTaskIfaceVO2 = new ItfLightTaskIfaceVO2();
//                itfLightTaskIfaceVO2.setTASK_NUM(itfLightTaskIface.getTaskNum());
//                itfLightTaskIfaceVO2.setLOCATOR_CODE(itfLightTaskIface.getLocatorLabelId());
//                itfLightTaskIfaceVO2.setTASK_TYPE(itfLightTaskIface.getTaskType());
//                itfLightTaskIfaceVO2.setTASK_STATUS(itfLightTaskIface.getTaskStatus());
//                itfLightTaskIfaceVO2s.add(itfLightTaskIfaceVO2);
            }
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();

            //调用接口
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("requestInfo", JSONArray.toJSONString(itfLightTaskIfaceVO2List));
            ResponsePayloadDTO responsePayload = null;
            try {
                responsePayload = itfLightTaskIfaceRepository.sendLight(requestMap, "ItfLightTaskIface",
                        ItfConstant.InterfaceCode.LIGHT_TASK);
            }catch (Exception e){
                log.info("<=========亮灯指令接口调用出错了," + e.getMessage());
                itfLightTaskIfaceRepository.updateIfaceData(tenantId, ifaceIdList, e.getMessage(), userId);
                throw e;
            }
            //解析返回报文,如果有异常情况，需要更新为E
            ItfWcsResponseVO2 itfWcsResponseVO2 = itfLightTaskIfaceRepository.validateResponsePayload(tenantId, responsePayload, ifaceIdList, userId);
            List<ItfLightTaskIfaceVO3> header = itfWcsResponseVO2.getHeader();
            //根据正常返回的报文去更新接口数据,如果有报错条码，则封装报错数据
            resultList = itfLightTaskIfaceRepository.updateIface(tenantId, header, userId, ifaceIdList,taskNumList);
        }
        return resultList;
    }
}
