package com.ruike.wms.app.service.impl;

import java.util.List;

import com.ruike.hme.infra.constant.HmeConstants;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruike.wms.app.service.WmsBarCodeIdentifyService;
import com.ruike.wms.api.dto.WmsCodeIdentifyDTO;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.inventory.domain.entity.MtContainerType;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtContainerTypeRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContLoadDtlVO5;
import tarzan.inventory.domain.vo.MtContainerVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO3;

/**
 * @Classname CodeIdentifyServiceImpl
 * @Description 条码识别实现类
 * @Date 2019/10/4 16:38
 * @Author zhihao.sang
 */
@Service
public class WmsBarCodeIdentifyServiceImpl implements WmsBarCodeIdentifyService {

    @Autowired
    private MtContainerRepository mtContainerRepository;

    @Autowired
    private MtContainerTypeRepository mtContainerTypeRepository;

    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;

    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public WmsCodeIdentifyDTO codeIdentifyPro(Long tenantId, String code) {
        WmsCodeIdentifyDTO dto = new WmsCodeIdentifyDTO();
        dto.setCode(code);

        // 检验条码是否为容器
        MtContainerVO13 containerVo13 = new MtContainerVO13();
        containerVo13.setContainerCode(code);
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, containerVo13);
        if (!CollectionUtils.isEmpty(containerIds)) {
            String containerId = containerIds.get(0);
            // 获取容器类型ID
            String containerTypeId =
                    mtContainerRepository.containerPropertyGet(tenantId, containerId).getContainerTypeId();
            // 获取容器类型
            String containerType = mtContainerTypeRepository.containerTypePropertyGet(tenantId, containerTypeId)
                    .getContainerTypeDescription();
            dto.setContainerType(containerType);
            dto.setContainerTypeCode( mtContainerTypeRepository.containerTypePropertyGet(tenantId, containerTypeId).getContainerTypeCode());
            dto.setCodeId(containerId);
            dto.setCodeType(CONTAINER);
        } else {
            // 检验条码是否为物料批
            MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
            materialLotVo3.setMaterialLotCode(code);
            //materialLotVo3.setEnableFlag("Y");
            List<String> materialLotIds =
                    mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
            if (CollectionUtils.isEmpty(materialLotIds)) {
                // 条码返回错误信息
                throw new MtException("WMS_COST_CENTER_0006",mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_COST_CENTER_0006", "WMS", code));
            }
            String materialLotId = materialLotIds.get(0);
            dto.setCodeId(materialLotId);
            dto.setCodeType(MATERIAL_LOT);
        }
        return this.getLoadContainer(tenantId, dto);
    }

    @Override
    public WmsCodeIdentifyDTO codeIdentify(Long tenantId, String code) {
        WmsCodeIdentifyDTO dto = new WmsCodeIdentifyDTO();
        dto.setCode(code);

        // 检验条码是否为容器
        MtContainerVO13 containerVo13 = new MtContainerVO13();
        containerVo13.setContainerCode(code);
        List<String> containerIds = mtContainerRepository.propertyLimitContainerQuery(tenantId, containerVo13);
        if (!CollectionUtils.isEmpty(containerIds)) {
            String containerId = containerIds.get(0);
            // 获取容器类型ID
            String containerTypeId =
                            mtContainerRepository.containerPropertyGet(tenantId, containerId).getContainerTypeId();
            // 获取容器类型
            MtContainerType mtContainerType = mtContainerTypeRepository.containerTypePropertyGet(tenantId, containerTypeId);
            String containerType = mtContainerType.getContainerTypeDescription();
            dto.setContainerType(containerType);
            dto.setCodeId(containerId);
            dto.setCodeType(CONTAINER);
            dto.setContainerTypeCode(mtContainerType.getContainerTypeCode());
        } else {
            // 检验条码是否为物料批
            MtMaterialLotVO3 materialLotVo3 = new MtMaterialLotVO3();
            materialLotVo3.setMaterialLotCode(code);
            materialLotVo3.setEnableFlag(HmeConstants.ConstantValue.YES);
            List<String> materialLotIds =
                            mtMaterialLotRepository.propertyLimitMaterialLotQuery(tenantId, materialLotVo3);
            if (CollectionUtils.isEmpty(materialLotIds)) {
                // 条码返回错误信息
                throw new MtException("WMS_COST_CENTER_0006",mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "WMS_COST_CENTER_0006", "WMS", code));
            }
            String materialLotId = materialLotIds.get(0);
            dto.setCodeId(materialLotId);
            dto.setCodeType(MATERIAL_LOT);
        }
        return this.getLoadContainer(tenantId, dto);
    }

    /** set装载容器ID */
    private WmsCodeIdentifyDTO getLoadContainer(Long tenantId, WmsCodeIdentifyDTO dto) {
        MtContLoadDtlVO5 contLoadDtlVo5 = new MtContLoadDtlVO5();
        contLoadDtlVo5.setLoadObjectId(dto.getCodeId());
        contLoadDtlVo5.setLoadObjectType(dto.getCodeType());
        List<String> lContainerIds =
                        mtContainerLoadDetailRepository.objectLimitLoadingContainerQuery(tenantId, contLoadDtlVo5);
        if (!CollectionUtils.isEmpty(lContainerIds)) {
            dto.setLoadingContainerId(lContainerIds.get(0));
        }
        return dto;
    }
}
