package com.ruike.itf.infra.repository.impl;

import com.alibaba.fastjson.JSON;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.itf.api.dto.ItfLightTaskIfaceDTO;
import com.ruike.itf.domain.vo.*;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfLightTaskIfaceMapper;
import com.ruike.itf.infra.util.JsonUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.RequestPayloadDTO;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.interfaces.sdk.invoke.InterfaceInvokeSdk;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import com.ruike.itf.domain.entity.ItfLightTaskIface;
import com.ruike.itf.domain.repository.ItfLightTaskIfaceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 亮灯指令接口表 资源库实现
 *
 * @author li.zhang13@hand-china.com 2021-08-09 11:12:14
 */
@Component
@Slf4j
public class ItfLightTaskIfaceRepositoryImpl extends BaseRepositoryImpl<ItfLightTaskIface> implements ItfLightTaskIfaceRepository {

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtCustomDbRepository customDbRepository;
    @Autowired
    private ItfLightTaskIfaceRepository itfLightTaskIfaceRepository;
    @Autowired
    private ItfLightTaskIfaceMapper itfLightTaskIfaceMapper;
    @Autowired
    private InterfaceInvokeSdk interfaceInvokeSdk;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;

    @Value("${hwms.interface.defaultNamespace}")
    private String namespace;

    private final Integer HTTP_STATUS_OK = 200;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<String> insertitfLightTaskIface(Long tenantId, List<ItfLightTaskIfaceDTO> dtoList) {
        //获取TaskNum集合，判断是新增还是取消
        List<String> numList = dtoList.stream().map(ItfLightTaskIfaceDTO::getTaskNum).collect(Collectors.toList());
        List<ItfLightTaskIface> itfLightTaskIfaces = new ArrayList<>();
        if(StringUtils.isBlank(dtoList.get(0).getTaskNum())){
            //TaskNum为空，则是新增
            List<String> ids = this.customDbRepository.getNextKeys("itf_light_task_iface_s", dtoList.size());
            List<String> cIds = this.customDbRepository.getNextKeys("itf_light_task_iface_cid_s", dtoList.size());
            Date nowDate = new Date();
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);
            int index = 0;
            Integer taskNumIndex = 0;
            //按照规则生成TaskNum
            MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
            mtNumrangeVO9.setNumQty(Long.valueOf(dtoList.size()));
            mtNumrangeVO9.setObjectCode("STOCK_IN_TASK_NUMBER");
            mtNumrangeVO9.setObjectTypeCode("LIGHT_TASK");
            mtNumrangeVO9.setObjectNumFlag(HmeConstants.ConstantValue.YES);
            mtNumrangeVO9.setSiteId(defaultSiteId);
            MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
            for(ItfLightTaskIfaceDTO dto:dtoList){
                ItfLightTaskIface itfLightTaskIface = new ItfLightTaskIface();
                String taskNum = mtNumrangeVO8.getNumberList().get(taskNumIndex);
                itfLightTaskIface.setTaskNum(taskNum);
                itfLightTaskIface.setTenantId(tenantId);
                itfLightTaskIface.setIfaceId(ids.get(index));
                itfLightTaskIface.setCid(Long.valueOf(cIds.get(index)));
                itfLightTaskIface.setObjectVersionNumber(1L);
                itfLightTaskIface.setCreationDate(nowDate);
                itfLightTaskIface.setCreatedBy(userId);
                itfLightTaskIface.setLastUpdatedBy(userId);
                itfLightTaskIface.setLastUpdateDate(nowDate);
                itfLightTaskIface.setStatus("N");
                itfLightTaskIface.setDocId(dto.getInstructionDocId());
                itfLightTaskIface.setDocLineId(dto.getInstructionId());
                itfLightTaskIface.setLocatorCode(dto.getLocatorCode());
                //判断值集HME.ITF.LOCATOR_LABEL_ID是否存在
                List<LovValueDTO> LovValueDTOs = lovAdapter.queryLovValue("ITF.LOCATOR_LABEL_ID", tenantId);
                if(CollectionUtils.isEmpty(LovValueDTOs)){
                    throw new MtException("exception","值集ITF.LOCATOR_LABEL_ID不存在或值集为空");
                }else{
                    //根据locatorCode获取对应值集的值给locatorLableId
                    List<LovValueDTO> LovValueDTOList = LovValueDTOs.stream().filter(item ->item.getValue().equals(dto.getLocatorCode())).collect(Collectors.toList());
                    if(CollectionUtils.isEmpty(LovValueDTOList)){
                        throw new MtException("exception",dto.getLocatorCode()+"未在值集中维护对应值");
                    }else{
                        itfLightTaskIface.setLocatorLabelId(LovValueDTOList.get(0).getMeaning());
                    }
                }
                itfLightTaskIface.setTaskType(dto.getTaskType());
                itfLightTaskIface.setTaskStatus(StringUtils.isNotBlank(dto.getTaskStatus()) ? dto.getTaskStatus() : "ON");
                itfLightTaskIfaces.add(itfLightTaskIface);
                numList.add(taskNum);
                index++;
                taskNumIndex++;
            }
            //新增接口表数据
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaces)){
                itfLightTaskIfaceRepository.batchInsert(itfLightTaskIfaces);
            }
        }else{
            //TaskNum不为空，则是取消，更新接口表数据
            Date nowDate = new Date();
            CustomUserDetails curUser = DetailsHelper.getUserDetails();
            Long userId = curUser == null ? -1L : curUser.getUserId();
            for(ItfLightTaskIfaceDTO dto:dtoList){
                ItfLightTaskIface itfLightTaskIface = new ItfLightTaskIface();
                itfLightTaskIface.setTaskNum(dto.getTaskNum());
                itfLightTaskIface.setTaskStatus(dto.getTaskStatus());
                itfLightTaskIfaces.add(itfLightTaskIface);
            }
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaces)){
                itfLightTaskIfaceMapper.updateTaskStatus(tenantId,userId,nowDate,itfLightTaskIfaces);
            }
        }
        return numList;
    }

    @Override
    public ItfWcsResponseVO2 validateResponsePayload(Long tenantId, ResponsePayloadDTO responsePayload, List<String> ifaceIdList, Long userId) {
        ItfWcsResponseVO2 itfWcsResponseVO = JsonUtils.jsonToObject(responsePayload.getPayload(), ItfWcsResponseVO2.class);
        if (Objects.isNull(itfWcsResponseVO)
                || Objects.isNull(itfWcsResponseVO.getHeader())) {
            if (org.apache.commons.lang.StringUtils.isEmpty(responsePayload.getPayload())) {
                log.error("<==== " + "ItfLightTaskIface" + " Success WCS {}接口返回结果为空！", responsePayload);
                this.updateIfaceData(tenantId, ifaceIdList, "WCS接口返回结果为空", userId);
                throw new CommonException("WCS接口返回结果为空！报文内容：" + responsePayload.getPayload());
            } else {
                log.error("WCS接口返回报文解析失败！报文内容：" + responsePayload.getPayload());
                this.updateIfaceData(tenantId, ifaceIdList, "WCS接口返回报文解析失败", userId);
                throw new CommonException("WCS接口返回报文解析失败！报文内容：" + responsePayload.getPayload());
            }
        }
        List<ItfLightTaskIfaceVO3> header = itfWcsResponseVO.getHeader();
        if(header.size() != ifaceIdList.size()){
            this.updateIfaceData(tenantId, ifaceIdList,
                    "WCS接口返回结果数据个数不一致,MES:" + ifaceIdList.size() + "WCS:" + header.size(),
                    userId);
            throw new CommonException("WCS接口返回数据个数不一致！报文内容：" + responsePayload.getPayload());
        }
        return itfWcsResponseVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public void updateIfaceData(Long tenantId, List<String> ifaceIdList, String message, Long userId) {
        itfLightTaskIfaceMapper.updateIfaceData(tenantId,ifaceIdList,message,userId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<ItfLightTaskIfaceVO> updateIface(Long tenantId, List<ItfLightTaskIfaceVO3> header, Long userId, List<String> ifaceIdList, List<String> taskNumList) {
        List<ItfLightTaskIfaceVO> resultList = new ArrayList<>();
        List<ItfLightTaskIface> itfLightTaskIfaceList = new ArrayList<>();
        List<ItfLightTaskIface> itfLightTaskIfaces = itfLightTaskIfaceMapper.selectByTaskNum(tenantId,taskNumList);
        Date nowDate = new Date();
        for(ItfLightTaskIface itfLightTaskIface:itfLightTaskIfaces){
            List<ItfLightTaskIfaceVO3> itfLightTaskIfaceVO3s= header.stream().filter(item -> itfLightTaskIface.getTaskNum().equals(item.getTaskNum())).collect(Collectors.toList());
            if(CollectionUtils.isNotEmpty(itfLightTaskIfaceVO3s)){
                itfLightTaskIface.setStatus(itfLightTaskIfaceVO3s.get(0).getMsgCode());
                itfLightTaskIface.setMessage(itfLightTaskIfaceVO3s.get(0).getMessage());
                itfLightTaskIfaceList.add(itfLightTaskIface);
                //封装返回消息
                ItfLightTaskIfaceVO itfLightTaskIfaceVO = new ItfLightTaskIfaceVO();
                itfLightTaskIfaceVO.setTaskNum(itfLightTaskIface.getTaskNum());
                itfLightTaskIfaceVO.setStatus(itfLightTaskIface.getStatus());
                itfLightTaskIfaceVO.setMessage(itfLightTaskIface.getMessage());
                resultList.add(itfLightTaskIfaceVO);
            }
        }
        //跟新接口表数据
        itfLightTaskIfaceMapper.updateStatusAndMsg(tenantId,userId,nowDate,itfLightTaskIfaceList);
        return resultList;
    }

    @Override
    public ResponsePayloadDTO sendLight(Object requestMap, String logName, String itfPath) {
        RequestPayloadDTO requestPayload = new RequestPayloadDTO();
        requestPayload.setPayload(JsonUtils.objToJson(requestMap));
        log.info("<==== " + logName + " requestPayload: {}", requestPayload.toString());

        // 请求接口
        ResponsePayloadDTO responsePayload = new ResponsePayloadDTO();
        try {
            responsePayload = interfaceInvokeSdk.invoke(namespace,
                    ItfConstant.ServerCode.MES_LIGHT,
                    itfPath,
                    requestPayload);
        } catch (Exception e) {
            //rollbackIfaceStatus(userId, ifaceList);
            throw e;
        }

        if (Objects.isNull(responsePayload)) {
            log.error("<==== " + logName + " Error requestPayload: {}WCS接口调用失败", requestPayload.toString());
            throw new CommonException("<==== " + logName + " Error requestPayload: {}ESB接口调用失败", requestPayload.toString());
        } else if (responsePayload.getStatusCodeValue() != HTTP_STATUS_OK || !HTTP_STATUS_OK.toString().equals(responsePayload.getStatus())) {
            log.error("<==== " + logName + " Error requestPayload: {}WCS接口调用失败,服务器不通", requestPayload.toString());
            throw new CommonException("<==== " + logName + " Error requestPayload: {}WCS接口调用失败,服务器不通", requestPayload.toString());
        } else {
            log.info("<==== " + logName + " Success responsePayload: {}", JSON.toJSON(responsePayload));
        }
        return responsePayload;
    }
}
