package com.ruike.itf.infra.repository.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.hme.infra.constant.HmeConstants;
import com.ruike.hme.infra.util.CommonUtils;
import com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO;
import com.ruike.itf.api.dto.ItfFinishDeliveryInstructionIfaceDTO2;
import com.ruike.itf.domain.entity.ItfWcsTaskIface;
import com.ruike.itf.domain.entity.ItfWcsTaskLineIface;
import com.ruike.itf.domain.repository.ItfFinishDeliveryInstructionIfaceRepository;
import com.ruike.itf.domain.repository.ItfMaterialLotConfirmIfaceRepository;
import com.ruike.itf.domain.repository.ItfWcsTaskIfaceRepository;
import com.ruike.itf.domain.repository.ItfWcsTaskLineIfaceRepository;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO2;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO3;
import com.ruike.itf.domain.vo.ItfFinishDeliveryInstructionIfaceVO4;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.mapper.ItfFinishDeliveryInstructionIfaceMapper;
import com.ruike.itf.infra.util.JsonUtils;
import com.ruike.wms.domain.repository.WmsSiteRepository;
import io.choerodon.core.exception.CommonException;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.vo.MtNumrangeVO8;
import io.tarzan.common.domain.vo.MtNumrangeVO9;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.ResponsePayloadDTO;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tarzan.instruction.domain.entity.MtInstructionDoc;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 成品出库指令信息接口
 *
 * @author sanfeng.zhang@hand-china.com 2021/7/16 15:42
 */
@Component
@Slf4j
public class ItfFinishDeliveryInstructionIfaceRepositoryImpl implements ItfFinishDeliveryInstructionIfaceRepository {

    @Autowired
    private ItfFinishDeliveryInstructionIfaceMapper itfFinishDeliveryInstructionIfaceMapper;
    @Autowired
    private ItfWcsTaskIfaceRepository itfWcsTaskIfaceRepository;
    @Autowired
    private ItfWcsTaskLineIfaceRepository itfWcsTaskLineIfaceRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private ItfMaterialLotConfirmIfaceRepository itfMaterialLotConfirmIfaceRepository;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private WmsSiteRepository wmsSiteRepository;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<ItfFinishDeliveryInstructionIfaceVO> itfWCSTaskIface(Long tenantId, List<ItfFinishDeliveryInstructionIfaceDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            throw new CommonException("未传入成品出库任务,请检查!");
        }
        List<ItfFinishDeliveryInstructionIfaceVO> resultList = new ArrayList<>();
        List<ItfWcsTaskIface> insertTaskIfaceList = new ArrayList<>();
        List<ItfWcsTaskLineIface> insertLineIfaceList = new ArrayList<>();
        List<ItfFinishDeliveryInstructionIfaceDTO2> sendIfaceList = new ArrayList<>();
        // 批量 要么取消 要么新增 不存在又新增又有取消的情况
        Optional<ItfFinishDeliveryInstructionIfaceDTO> cancelOpt = dtoList.stream().filter(dto -> StringUtils.equals(dto.getTaskStatus(), "CANCEL")).findFirst();
        String taskStatus = cancelOpt.isPresent() ? "CANCEL" : "NEW";
        if (StringUtils.equals(taskStatus, "NEW")) {
            List<String> ids = mtCustomDbRepository.getNextKeys("itf_wcs_task_iface_s", dtoList.size());
            List<String> cIds = mtCustomDbRepository.getNextKeys("itf_wcs_task_iface_cid_s", dtoList.size());
            String defaultSiteId = wmsSiteRepository.userDefaultSite(tenantId);

            MtNumrangeVO9 mtNumrangeVO9 = new MtNumrangeVO9();
            mtNumrangeVO9.setNumQty(Long.valueOf(dtoList.size()));
            mtNumrangeVO9.setObjectCode("STOCK_IN_TASK_NUMBER");
            mtNumrangeVO9.setObjectTypeCode("WCS_TASK");
            mtNumrangeVO9.setObjectNumFlag(HmeConstants.ConstantValue.YES);
            mtNumrangeVO9.setSiteId(defaultSiteId);
            MtNumrangeVO8 mtNumrangeVO8 = mtNumrangeRepository.numrangeBatchGenerate(tenantId, mtNumrangeVO9);
            Integer taskIndex = 0;
            Integer taskNumIndex = 0;
            // 批量查询单据
            List<String> instructionDocIdList = dtoList.stream().map(ItfFinishDeliveryInstructionIfaceDTO::getInstructionDocId).distinct().collect(Collectors.toList());
            Map<String, MtInstructionDoc> instructionDocMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(instructionDocIdList)) {
                List<MtInstructionDoc> mtInstructionDocList = itfFinishDeliveryInstructionIfaceMapper.batchQueryDocNumList(tenantId, instructionDocIdList);
                instructionDocMap = mtInstructionDocList.stream().collect(Collectors.toMap(MtInstructionDoc::getInstructionDocId, t -> t));
            }
            // 批量查询行号
            List<String> instructionIdList = dtoList.stream().map(ItfFinishDeliveryInstructionIfaceDTO::getInstructionId).distinct().collect(Collectors.toList());
            Map<String, ItfFinishDeliveryInstructionIfaceVO2> instructionMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(instructionIdList)) {
                List<ItfFinishDeliveryInstructionIfaceVO2> instructionList = itfFinishDeliveryInstructionIfaceMapper.batchQueryDocLineNumList(tenantId, instructionIdList);
                instructionMap = instructionList.stream().collect(Collectors.toMap(ItfFinishDeliveryInstructionIfaceVO2::getInstructionId, t -> t));
            }
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            Date currentDate = CommonUtils.currentTimeGet();
            for (ItfFinishDeliveryInstructionIfaceDTO ifaceDTO : dtoList) {
                // 新增 记录接口表ITF_WCS_TASK_IFACE和ITF_WCS_TASK_LINE_IFACE
                ItfWcsTaskIface itfWcsTaskIface = new ItfWcsTaskIface();
                itfWcsTaskIface.setTenantId(tenantId);
                itfWcsTaskIface.setIfaceId(ids.get(taskIndex));
                itfWcsTaskIface.setTaskNum(mtNumrangeVO8.getNumberList().get(taskNumIndex++));
                itfWcsTaskIface.setDocId(ifaceDTO.getInstructionDocId());
                itfWcsTaskIface.setDocLineId(ifaceDTO.getInstructionId());
                itfWcsTaskIface.setMaterialCode(ifaceDTO.getMaterialCode());
                itfWcsTaskIface.setMaterialVersion(ifaceDTO.getMaterialVersion());
                itfWcsTaskIface.setQty(ifaceDTO.getQty() != null ? ifaceDTO.getQty().toString() : null);
                itfWcsTaskIface.setSoNum(ifaceDTO.getSoNum());
                itfWcsTaskIface.setSoLineNum(ifaceDTO.getSoLineNum());
                itfWcsTaskIface.setExitNum(ifaceDTO.getExitNum());
                itfWcsTaskIface.setWarehouseCode(ifaceDTO.getWarehouseCode());
                itfWcsTaskIface.setTaskStatus(HmeConstants.StatusCode.NEW);
                itfWcsTaskIface.setObjectVersionNumber(1L);
                itfWcsTaskIface.setCid(Long.valueOf(cIds.get(taskIndex++)));
                itfWcsTaskIface.setCreatedBy(userId);
                itfWcsTaskIface.setCreationDate(currentDate);
                itfWcsTaskIface.setLastUpdatedBy(userId);
                itfWcsTaskIface.setLastUpdateDate(currentDate);
                insertTaskIfaceList.add(itfWcsTaskIface);
                ifaceDTO.setTaskNum(itfWcsTaskIface.getTaskNum());
                ifaceDTO.setIfaceId(itfWcsTaskIface.getIfaceId());

                MtInstructionDoc instructionDoc = instructionDocMap.getOrDefault(itfWcsTaskIface.getDocId(), new MtInstructionDoc());
                ItfFinishDeliveryInstructionIfaceVO2 docLine = instructionMap.getOrDefault(itfWcsTaskIface.getDocLineId(), new ItfFinishDeliveryInstructionIfaceVO2());
                // 组装请求接口数据
                ItfFinishDeliveryInstructionIfaceDTO2 dto2 = new ItfFinishDeliveryInstructionIfaceDTO2();
                dto2.setTASK_NUM(itfWcsTaskIface.getTaskNum());
                dto2.setMATERIAL_CODE(itfWcsTaskIface.getMaterialCode());
                dto2.setDOC_NUM(instructionDoc.getInstructionDocNum());
                dto2.setDOC_LINE_NUM(docLine.getInstructionLineNum());
                dto2.setMATERIAL_VERSION(itfWcsTaskIface.getMaterialVersion());
                dto2.setQTY(ifaceDTO.getQty());
                dto2.setSO_NUM(itfWcsTaskIface.getSoNum());
                dto2.setSO_LINE_NUM(itfWcsTaskIface.getSoLineNum());
                dto2.setEXIT_NUM(itfWcsTaskIface.getExitNum());
                dto2.setWAREHOUSE_CODE(itfWcsTaskIface.getWarehouseCode());
                dto2.setSTATUS(itfWcsTaskIface.getTaskStatus());
                dto2.setMATERIAL_LOT_CODE(CollectionUtils.isNotEmpty(ifaceDTO.getMaterialLotCodeList()) ? StringUtils.join(ifaceDTO.getMaterialLotCodeList(), ",") : null);
                sendIfaceList.add(dto2);
                if (CollectionUtils.isNotEmpty(ifaceDTO.getMaterialLotCodeList())) {
                    for (String materialLotCode : ifaceDTO.getMaterialLotCodeList()) {
                        ItfWcsTaskLineIface itfWcsTaskLineIface = new ItfWcsTaskLineIface();
                        itfWcsTaskLineIface.setTenantId(tenantId);
                        itfWcsTaskLineIface.setTaskNum(itfWcsTaskIface.getTaskNum());
                        itfWcsTaskLineIface.setMaterialLotCode(materialLotCode);
                        insertLineIfaceList.add(itfWcsTaskLineIface);
                    }
                }
            }
        } else {
            // 取消 根据出货单查询出成品出库指令信息
            List<String> taskNumList = dtoList.stream().map(ItfFinishDeliveryInstructionIfaceDTO::getTaskNum).collect(Collectors.toList());
            dtoList = itfFinishDeliveryInstructionIfaceMapper.queryWcsTaskIfaceList(tenantId, taskNumList);
            for (ItfFinishDeliveryInstructionIfaceDTO ifaceDTO : dtoList) {
                ItfFinishDeliveryInstructionIfaceDTO2 dto2 = new ItfFinishDeliveryInstructionIfaceDTO2();
                dto2.setTASK_NUM(ifaceDTO.getTaskNum());
                dto2.setMATERIAL_CODE(ifaceDTO.getMaterialCode());
                dto2.setDOC_NUM(ifaceDTO.getDocNum());
                dto2.setDOC_LINE_NUM(ifaceDTO.getDocLineNum());
                dto2.setMATERIAL_VERSION(ifaceDTO.getMaterialVersion());
                dto2.setQTY(ifaceDTO.getQty());
                dto2.setSO_NUM(ifaceDTO.getSoNum());
                dto2.setSO_LINE_NUM(ifaceDTO.getSoLineNum());
                dto2.setEXIT_NUM(ifaceDTO.getExitNum());
                dto2.setWAREHOUSE_CODE(ifaceDTO.getWarehouseCode());
                dto2.setSTATUS("CANCEL");
                dto2.setMATERIAL_LOT_CODE(CollectionUtils.isNotEmpty(ifaceDTO.getMaterialLotCodeList()) ? StringUtils.join(ifaceDTO.getMaterialLotCodeList(), ",") : null);
                sendIfaceList.add(dto2);
            }
        }
        // 新增成品出库指令信息接口头表
        if (CollectionUtils.isNotEmpty(insertTaskIfaceList)) {
            List<List<ItfWcsTaskIface>> splitSqlList = CommonUtils.splitSqlList(insertTaskIfaceList, 500);
            for (List<ItfWcsTaskIface> taskIfaces : splitSqlList) {
                itfFinishDeliveryInstructionIfaceMapper.batchInsertTaskIfaces(taskIfaces);
            }
        }
        // 新增成品出库指令信息接口行表
        if (CollectionUtils.isNotEmpty(insertLineIfaceList)) {
            List<List<ItfWcsTaskLineIface>> splitSqlList = CommonUtils.splitSqlList(insertLineIfaceList, 500);
            for (List<ItfWcsTaskLineIface> lineIfaces : splitSqlList) {
                itfWcsTaskLineIfaceRepository.batchInsertSelective(lineIfaces);
            }
        }
        //调用接口
        List<LovValueDTO> internalFlagList = lovAdapter.queryLovValue("ITF.INTERNAL_FLAG", tenantId);
        if (CollectionUtils.isEmpty(internalFlagList)){
            throw new CommonException("ITF.INTERNAL_FLAG值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        Optional<LovValueDTO> lovFlag = internalFlagList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "TIMELY_INTERFACE_FLAG")).findFirst();
        String interfaceFlag = lovFlag.isPresent() ? lovFlag.get().getMeaning() : "";
        if (ItfConstant.ConstantValue.YES.equals(interfaceFlag)) {
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("requestInfo", JSONArray.toJSONString(sendIfaceList));
            ResponsePayloadDTO responsePayloadDTO = itfMaterialLotConfirmIfaceRepository.sendWcs(requestMap, "ItfFinishDeliveryInstructionIface",
                    ItfConstant.InterfaceCode.WCS_EXTRACT_STOCK_OUT);
            ItfFinishDeliveryInstructionIfaceVO3 itfWcsResponseVO = JsonUtils.jsonToObject(responsePayloadDTO.getPayload(), ItfFinishDeliveryInstructionIfaceVO3.class);
            // 根据返回消息  更新接口表
            resultList = this.handleReturnBackInfo(tenantId, itfWcsResponseVO, dtoList, taskStatus);
        }
        return resultList;
    }

    private List<ItfFinishDeliveryInstructionIfaceVO> handleReturnBackInfo(Long tenantId, ItfFinishDeliveryInstructionIfaceVO3 itfWcsResponseVO, List<ItfFinishDeliveryInstructionIfaceDTO> dtoList, String taskStatus) {
        List<ItfFinishDeliveryInstructionIfaceVO> resultList = new ArrayList<>();
        List<ItfWcsTaskIface> itfWcsTaskIfaceList = new ArrayList<>();
        Map<String, ItfFinishDeliveryInstructionIfaceDTO> dtoMap = dtoList.stream().collect(Collectors.toMap(ItfFinishDeliveryInstructionIfaceDTO::getTaskNum, t -> t));
        for (ItfFinishDeliveryInstructionIfaceVO4 ifaceVO4 : itfWcsResponseVO.getHeader()) {
            ItfFinishDeliveryInstructionIfaceDTO ifaceDTO = dtoMap.getOrDefault(ifaceVO4.getTaskNum(), null);
            if (ifaceDTO != null) {
                if ("NEW".equals(taskStatus)) {
                    // 新增则更新接口表处理状态和处理消息
                    ItfWcsTaskIface itfWcsTaskIface = new ItfWcsTaskIface();
                    itfWcsTaskIface.setIfaceId(ifaceDTO.getIfaceId());
                    itfWcsTaskIface.setMessage(ifaceVO4.getMessage());
                    itfWcsTaskIface.setStatus(ifaceVO4.getMsgCode());
                    itfWcsTaskIface.setTaskStatus(taskStatus);
                    itfWcsTaskIfaceList.add(itfWcsTaskIface);

                    ItfFinishDeliveryInstructionIfaceVO ifaceVO = new ItfFinishDeliveryInstructionIfaceVO();
                    ifaceVO.setTaskNum(ifaceVO4.getTaskNum());
                    if (StringUtils.equals(ifaceVO4.getMsgCode(), "S")) {
                        ifaceVO.setMessage("S");
                        ifaceVO.setSuccess(true);
                    } else {
                        ifaceVO.setMessage(ifaceVO4.getMessage());
                        ifaceVO.setSuccess(false);
                    }
                    resultList.add(ifaceVO);
                } else {
                    // 取消则更新接口状态 成功则更改状态
                    ItfFinishDeliveryInstructionIfaceVO ifaceVO = new ItfFinishDeliveryInstructionIfaceVO();
                    ifaceVO.setTaskNum(ifaceVO4.getTaskNum());
                    ItfWcsTaskIface itfWcsTaskIface = new ItfWcsTaskIface();
                    itfWcsTaskIface.setIfaceId(ifaceDTO.getIfaceId());
                    itfWcsTaskIface.setTaskStatus("CANCEL");
                    itfWcsTaskIface.setMessage(ifaceVO4.getMessage());
                    itfWcsTaskIface.setStatus(ifaceVO4.getMsgCode());
                    itfWcsTaskIfaceList.add(itfWcsTaskIface);
                    if (StringUtils.equals(ifaceVO4.getMsgCode(), "S")) {
                        ifaceVO.setMessage("S");
                        ifaceVO.setSuccess(true);
                    } else {
                        ifaceVO.setSuccess(false);
                        ifaceVO.setMessage(ifaceVO4.getMessage());
                    }
                    resultList.add(ifaceVO);
                }
            }
        }
        // 更新成品出库指令信息接口行表
        if (CollectionUtils.isNotEmpty(itfWcsTaskIfaceList)) {
            Long userId = DetailsHelper.getUserDetails() != null ? DetailsHelper.getUserDetails().getUserId() : -1L;
            List<List<ItfWcsTaskIface>> splitSqlList = CommonUtils.splitSqlList(itfWcsTaskIfaceList, 500);
            for (List<ItfWcsTaskIface> taskIfaces : splitSqlList) {
                itfFinishDeliveryInstructionIfaceMapper.batchUpdateTaskIfaces(tenantId, userId, taskIfaces);
            }
        }
        return resultList;
    }

}
