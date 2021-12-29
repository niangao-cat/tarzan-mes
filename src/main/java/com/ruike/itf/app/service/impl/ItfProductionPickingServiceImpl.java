package com.ruike.itf.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.ruike.itf.api.dto.ItfProductionPickingIfaceDTO;
import com.ruike.itf.app.service.ItfProductionPickingIfaceService;
import com.ruike.itf.domain.vo.ItfEsbRequestVO;
import com.ruike.itf.domain.vo.ItfProductionPickingIfaceVO;
import com.ruike.itf.infra.constant.ItfConstant;
import com.ruike.itf.infra.util.JsonUtils;
import com.ruike.wms.infra.constant.WmsConstant;
import io.choerodon.core.exception.CommonException;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.vo.MtExtendAttrVO1;
import io.tarzan.common.domain.vo.MtExtendVO1;
import io.tarzan.common.domain.vo.MtExtendVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.interfaces.sdk.dto.RequestPayloadDTO;
import org.hzero.boot.interfaces.sdk.invoke.InterfaceInvokeSdk;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tarzan.instruction.domain.entity.MtInstruction;
import tarzan.instruction.domain.entity.MtInstructionDoc;
import tarzan.instruction.domain.repository.MtInstructionDocRepository;
import tarzan.instruction.domain.repository.MtInstructionRepository;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 生产领料执行数据回传接口应用服务默认实现
 *
 * @author li.zhang 2021/08/11 10:46
 */
@Service
@Slf4j
public class ItfProductionPickingServiceImpl implements ItfProductionPickingIfaceService {

    @Autowired
    private LovAdapter lovAdapter;
    @Autowired
    private MtInstructionDocRepository mtInstructionDocRepository;
    @Autowired
    private MtInstructionRepository mtInstructionRepository;
    @Autowired
    private InterfaceInvokeSdk interfaceInvokeSdk;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Value("${hwms.interface.defaultNamespace}")
    private String namespace;

    @Override
    public void itfProductionPickingIface(Long tenantId, List<ItfProductionPickingIfaceDTO> dtoList) {
        List<LovValueDTO> lovValueList = lovAdapter.queryLovValue("ITF.INTERNAL_FLAG", tenantId);
        if (CollectionUtils.isEmpty(lovValueList)){
            throw new CommonException("ITF.INTERNAL_FLAG值集没有维护\n含义值为Y或N【Y为实时发送，N为不实时发送】");
        }
        Optional<LovValueDTO> lovFlag = lovValueList.stream().filter(lov -> StringUtils.equals(lov.getValue(), "TIMELY_INTERFACE_FLAG")).findFirst();
        String interfaceFlag = lovFlag.isPresent() ? lovFlag.get().getMeaning() : "";
        if (ItfConstant.ConstantValue.YES.equals(interfaceFlag)) {
            //将传入数据进行整理
            //获取指令单据ID及指令Id集合
            List<String> instructionDocIdList = dtoList.stream().map(ItfProductionPickingIfaceDTO::getInstructionDocId).collect(Collectors.toList());
            List<String> instructionIdList = dtoList.stream().map(ItfProductionPickingIfaceDTO::getInstructionId).collect(Collectors.toList());
            //通过Id集合获取相应的信息
            List<MtInstructionDoc> mtInstructionDocs = mtInstructionDocRepository.instructionDocPropertyBatchGet(tenantId,instructionDocIdList);
            MtExtendVO1 mtExtendVO1 = new MtExtendVO1();
            mtExtendVO1.setTableName("mt_instruction_attr");
            mtExtendVO1.setKeyIdList(instructionIdList);
            List<MtExtendVO5> attrs = new ArrayList<>();
            MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
            mtExtendVO5.setAttrName("INSTRUCTION_LINE_NUM");
            attrs.add(mtExtendVO5);
            mtExtendVO1.setAttrs(attrs);
            List<MtExtendAttrVO1> mtExtendAttrVO1s = mtExtendSettingsRepository.attrPropertyBatchQuery(tenantId,mtExtendVO1);
            List<ItfProductionPickingIfaceVO> itfProductionPickingIfaceVOS = new ArrayList<>();
            for(ItfProductionPickingIfaceDTO itfProductionPickingIfaceDTO:dtoList){
                ItfProductionPickingIfaceVO itfProductionPickingIfaceVO = new ItfProductionPickingIfaceVO();
                List<MtInstructionDoc> mtInstructionDocList = mtInstructionDocs.stream().filter(item ->item.getInstructionDocId().equals(itfProductionPickingIfaceDTO.getInstructionDocId())).collect(Collectors.toList());
                List<MtExtendAttrVO1> mtExtendAttrVO1List = mtExtendAttrVO1s.stream().filter(item ->item.getKeyId().equals(itfProductionPickingIfaceDTO.getInstructionId())).collect(Collectors.toList());
                if(CollectionUtils.isNotEmpty(mtInstructionDocList)){
                    itfProductionPickingIfaceVO.setZDONU(mtInstructionDocList.get(0).getInstructionDocNum());
                }
                if(CollectionUtils.isNotEmpty(mtExtendAttrVO1List)){
                    itfProductionPickingIfaceVO.setZPSELP(mtExtendAttrVO1List.get(0).getAttrValue());
                }
                itfProductionPickingIfaceVO.setZQTY(String.valueOf(itfProductionPickingIfaceDTO.getActualQty()));
                itfProductionPickingIfaceVOS.add(itfProductionPickingIfaceVO);
            }
            if(CollectionUtils.isNotEmpty(itfProductionPickingIfaceVOS)){
                //调用接口
                Map<String, Object> requestMap = new HashMap<>();
                requestMap.put("zitem",itfProductionPickingIfaceVOS);
                ItfEsbRequestVO itfEsbRequestVO = new ItfEsbRequestVO(new Date());
                RequestPayloadDTO requestPayload = new RequestPayloadDTO();
                itfEsbRequestVO.setRequestInfo(requestMap);
                requestPayload.setPayload(JsonUtils.objToJson(itfEsbRequestVO));

                log.info("<==== " + "ItfProductionPickingIface" + " requestPayload: {}", requestPayload.toString());
                interfaceInvokeSdk.invoke(namespace,
                        ItfConstant.ServerCode.MES_ESB,
                        ItfConstant.InterfaceCode.ESB_PRODUCTION_PICKING,
                        requestPayload);
            }

        }
    }
}
