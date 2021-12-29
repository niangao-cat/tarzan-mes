package com.ruike.itf.app.service.impl;

import com.ruike.itf.api.dto.OperationCollectItfDTO;
import com.ruike.itf.api.dto.OperationCollectItfDTO1;
import com.ruike.itf.app.service.ItfOperationCollectIfaceService;
import com.ruike.itf.infra.mapper.ItfOperationCollectIfaceMapper;
import io.tarzan.common.api.dto.MtExtendAttrDTO;
import io.tarzan.common.app.service.MtExtendSettingsService;
import io.tarzan.common.domain.sys.MtUserClient;
import io.tarzan.common.domain.sys.MtUserInfo;
import liquibase.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.method.domain.entity.MtOperation;
import tarzan.method.domain.repository.MtOperationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName ItfOperationCollectIfaceServiceImpl
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2020/9/17 19:49
 * @Version 1.0
 **/
@Slf4j
@Service
public class ItfOperationCollectIfaceServiceImpl implements ItfOperationCollectIfaceService {

    @Autowired
    private MtOperationRepository mtOperationRepository;

    @Autowired
    private MtExtendSettingsService mtExtendSettingsService;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;
    @Autowired
    private ItfOperationCollectIfaceMapper itfOperationCollectIfaceMapper;
    @Autowired
    private MtUserClient userClient;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Override
    public List<OperationCollectItfDTO> invoke(Long tenantId, List<OperationCollectItfDTO> collectList) {
        List<OperationCollectItfDTO> operationCollectItfDTOs = new ArrayList<>();
        for (OperationCollectItfDTO collect :
                collectList) {
            if ("0".equals(collect.getType())) {
                MtOperation mtOperation = new MtOperation();
                mtOperation.setOperationName(collect.getOperationCode());
                List<MtOperation> select = mtOperationRepository.select(mtOperation);
                if (CollectionUtils.isEmpty(select)) {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("未找到对应的工位");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                List<MtExtendAttrDTO> operationAttrList =
                        mtExtendSettingsService.attrQuery(tenantId, select.get(0).getOperationId(), "MT_OPERATION_ATTR");
                if(CollectionUtils.isEmpty(operationAttrList))
                {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("未找到对应的工位");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                List<String> alias = operationAttrList.stream()
                        .filter(t -> "ALIAS".equals(t.getAttrName()))
                        .map(MtExtendAttrDTO::getAttrValue).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(operationAttrList))
                {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("未找到对应的工位");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
                materialLotVo3.setMaterialLotCode(collect.getSn());
                List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
                if(CollectionUtils.isEmpty(materialLotIds))
                {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("sn对应的物料批不存在");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotIds.get(0));
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());
                OperationCollectItfDTO1 operationCollectItfDTO1 = new OperationCollectItfDTO1();
                operationCollectItfDTO1.setEoId(materialLot.getEoId());
                operationCollectItfDTO1.setOperationId(select.get(0).getOperationId());
                operationCollectItfDTO1.setReworkFlag(collect.getReworkFlag());
                String tagCode = alias.get(0);
                if (StringUtils.isNotEmpty(collect.getCurrent())) {
                    tagCode = tagCode + "-" + collect.getCurrent();
                }
                if (StringUtils.isNotEmpty(collect.getCosPosition())) {
                    tagCode = tagCode + "-" + collect.getCosPosition();
                }
                if (StringUtils.isNotEmpty(collect.getDuration())) {
                    tagCode = tagCode + "-" + collect.getDuration();
                }
                if (StringUtils.isNotEmpty(collect.getTagType())) {
                    tagCode = tagCode + "-" + collect.getTagType();
                }
                operationCollectItfDTO1.setTagCode(tagCode);
                List<OperationCollectItfDTO> result = itfOperationCollectIfaceMapper.queryResult(tenantId, operationCollectItfDTO1);
                for (OperationCollectItfDTO temp :
                        result) {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    BeanUtils.copyProperties(collect, operationCollectItfDTO);
                    MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, temp.getCreatedBy());
                    if (!ObjectUtils.isEmpty(mtUserInfo))
                    {
                        operationCollectItfDTO.setRealName(mtUserInfo.getRealName());
                        operationCollectItfDTO.setLoginName(mtUserInfo.getLoginName());
                    }
                    operationCollectItfDTO.setTagCode(temp.getTagCode());
                    operationCollectItfDTO.setResult(temp.getResult());
                    operationCollectItfDTO.setMaterialCode(mtMaterial.getMaterialCode());
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                }
            } else {
                MtOperation mtOperation = new MtOperation();
                mtOperation.setOperationName(collect.getOperationCode());
                List<MtOperation> select = mtOperationRepository.select(mtOperation);
                if(CollectionUtils.isEmpty(select))
                {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("未找到对应的工位");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                List<MtExtendAttrDTO> operationAttrList =
                        mtExtendSettingsService.attrQuery(tenantId, select.get(0).getOperationId(), "MT_OPERATION_ATTR");
                if(CollectionUtils.isEmpty(operationAttrList))
                {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("未找到对应的工位");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                List<String> alias = operationAttrList.stream()
                        .filter(t -> "ALIAS".equals(t.getAttrName()))
                        .map(MtExtendAttrDTO::getAttrValue).collect(Collectors.toList());
                if(CollectionUtils.isEmpty(alias))
                {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("未找到对应的工位");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
                materialLotVo3.setMaterialLotCode(collect.getSn());
                List<String> materialLotIds = mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);

                if(CollectionUtils.isEmpty(materialLotIds))
                {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    operationCollectItfDTO.setProcessStatus("E");
                    operationCollectItfDTO.setProcessMessage("sn对应的物料批不存在");
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                    return operationCollectItfDTOs;
                }
                MtMaterialLot materialLot = mtMaterialLotRepository.materialLotPropertyGet(tenantId, materialLotIds.get(0));
                MtMaterial mtMaterial = mtMaterialRepository.selectByPrimaryKey(materialLot.getMaterialId());

                OperationCollectItfDTO1 operationCollectItfDTO1 = new OperationCollectItfDTO1();
                operationCollectItfDTO1.setEoId(materialLot.getEoId());
                operationCollectItfDTO1.setOperationId(select.get(0).getOperationId());
                operationCollectItfDTO1.setReworkFlag(collect.getReworkFlag());
                String tagCode = alias.get(0);

                if (StringUtils.isNotEmpty(collect.getCurrent())) {
                    tagCode = tagCode + "-" + collect.getCurrent();
                } else {
                    tagCode = tagCode + "%";
                }
                if (StringUtils.isNotEmpty(collect.getCosPosition())) {
                    tagCode = tagCode + "-" + collect.getCosPosition();
                } else {
                    tagCode = tagCode + "%";
                }
                if (StringUtils.isNotEmpty(collect.getDuration())) {
                    tagCode = tagCode + "-" + collect.getDuration();
                } else {
                    tagCode = tagCode + "%";
                }
                if (StringUtils.isNotEmpty(collect.getTagType())) {
                    tagCode = tagCode + "-" + collect.getTagType();
                } else {
                    tagCode = tagCode + "%";
                }
                operationCollectItfDTO1.setTagCode(tagCode);
                List<OperationCollectItfDTO> result = itfOperationCollectIfaceMapper.queryResultLike(tenantId, operationCollectItfDTO1);
                for (OperationCollectItfDTO temp :
                        result) {
                    OperationCollectItfDTO operationCollectItfDTO = new OperationCollectItfDTO();
                    BeanUtils.copyProperties(collect, operationCollectItfDTO);
                    MtUserInfo mtUserInfo = userClient.userInfoGet(tenantId, temp.getCreatedBy());
                    if (!ObjectUtils.isEmpty(mtUserInfo))
                    {
                        operationCollectItfDTO.setRealName(mtUserInfo.getRealName());
                        operationCollectItfDTO.setLoginName(mtUserInfo.getLoginName());
                    }
                    operationCollectItfDTO.setTagCode(temp.getTagCode());
                    operationCollectItfDTO.setResult(temp.getResult());
                    operationCollectItfDTO.setMaterialCode(mtMaterial.getMaterialCode());
                    operationCollectItfDTOs.add(operationCollectItfDTO);
                }
            }
        }
        return operationCollectItfDTOs;
    }
}
