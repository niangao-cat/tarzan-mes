package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeImportMaterialLotVO;
import com.ruike.wms.app.service.WmsMaterialLotService;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.repository.MtNumrangeRepository;
import io.tarzan.common.domain.sys.CustomSequence;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO5;
import io.tarzan.common.domain.vo.MtNumrangeVO2;
import io.tarzan.common.domain.vo.MtNumrangeVO5;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ImportHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.general.domain.repository.MtEventRepository;
import tarzan.general.domain.repository.MtEventRequestRepository;
import tarzan.general.domain.vo.MtEventCreateVO;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtInvOnhandQuantityRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.inventory.domain.vo.MtContainerVO24;
import tarzan.inventory.domain.vo.MtInvOnhandQuantityVO9;
import tarzan.inventory.domain.vo.MtMaterialLotVO13;
import tarzan.inventory.domain.vo.MtMaterialLotVO2;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.io.IOException;
import java.util.*;

/**
 * @author sanfeng.zhang@hand-china.com 2021/2/3 10:51
 */
@ImportService(templateCode = "HME.MATERIAL_LOT_TWO")
@Slf4j
public class HmeImportMaterialLotTwoServiceImpl extends ImportHandler {
    @Autowired
    private CustomSequence customSequence;
    @Autowired
    private MtEventRequestRepository eventRequestRepository;
    @Autowired
    private MtMaterialLotRepository materialLotRepository;
    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;
    @Autowired
    private MtEventRepository eventRepository;
    @Autowired
    private MtInvOnhandQuantityRepository onhandQuantityRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtSupplierRepository mtSupplierRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtNumrangeRepository mtNumrangeRepository;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsMaterialLotService wmsMaterialLotService;

    @Override
    public Boolean doImport(String vo) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        HmeImportMaterialLotVO importVO;
        try {
            importVO = objectMapper.readValue(vo, HmeImportMaterialLotVO.class);
        } catch (IOException e) {
            // 失败
            return false;
        }
        // set带有ID的参数
        importVO = setImportVO(tenantId, importVO);
        String eventRequestId = eventRequestRepository.eventRequestCreate(tenantId, "MATERIAL_LOT_INIT");
        String eventId = getEventId(tenantId, eventRequestId);
        addAttributes(tenantId, eventId, importVO, importVO.getMaterialCode());
        onHandQty(tenantId, importVO, eventId);
        if (!org.springframework.util.StringUtils.isEmpty(importVO.getContainerCode())) {
            MtContainerVO24 ctnLoad = new MtContainerVO24();
            ctnLoad.setContainerId(importVO.getContainerId());
            ctnLoad.setLoadObjectType("MATERIAL_LOT");
            ctnLoad.setLoadObjectId(importVO.getMaterialLotId());
            ctnLoad.setTrxLoadQty(importVO.getPrimaryUomQty() == null ? null :
                    Double.parseDouble(importVO.getPrimaryUomQty()));
            ctnLoad.setEventRequestId(eventRequestId);
            mtContainerRepository.containerLoad(tenantId, ctnLoad);
            //add junwei.wang 2020-03-25 15:39  更新mt_container SITE_ID和LOCATOR_ID
            MtContainer mtContainer = new MtContainer();
            mtContainer.setContainerId(importVO.getContainerId());
            mtContainer.setSiteId(importVO.getSiteId());
            mtContainer.setLocatorId(importVO.getLocatorId());
            mtContainerRepository.updateByPrimaryKeySelective(mtContainer);
        }
        return true;
    }

    /**
     * 创建事件ID
     *
     * @param tenantId
     * @param eventRequestId
     * @return
     */
    private String getEventId(Long tenantId, String eventRequestId) {
        MtEventCreateVO eventDto = new MtEventCreateVO();
        eventDto.setEventTypeCode("MATERIAL_LOT_INIT");
        eventDto.setEventRequestId(eventRequestId);
        return eventRepository.eventCreate(tenantId, eventDto);
    }

    /**
     * @param tenantId
     * @param materialLotVO
     * @param eventId
     * @return void
     * @Description 更新物料现有量
     * @Date 2019/11/13 20:16
     * @Author weihua.liao
     */
    private void onHandQty(Long tenantId, HmeImportMaterialLotVO materialLotVO, String eventId) {
        MtInvOnhandQuantityVO9 hand = new MtInvOnhandQuantityVO9();
        if (!org.springframework.util.StringUtils.isEmpty(materialLotVO.getPrimaryUomQty())) {
            if (Double.valueOf(materialLotVO.getPrimaryUomQty()) == 0) {
                log.error("<==== batch material lot import PrimaryUomQty() is zero:{}", materialLotVO);
                return;
            }
            hand.setChangeQuantity(Double.valueOf(materialLotVO.getPrimaryUomQty()));
        } else {
            log.error("<==== batch material lot import PrimaryUomQty() is null:{}", materialLotVO);
            return;
        }
        hand.setSiteId(materialLotVO.getSiteId());
        hand.setEventId(eventId);
        hand.setMaterialId(materialLotVO.getMaterialId());
        hand.setLocatorId(materialLotVO.getLocatorId());
        hand.setLotCode(materialLotVO.getLot());

        onhandQuantityRepository.onhandQtyUpdate(tenantId, hand);
    }

    private void addAttributes(Long tenantId, String eventId, HmeImportMaterialLotVO dto, String materialCode) {
        List<MtExtendVO5> attr = new ArrayList<>();
        MtExtendVO5 tmpVo = null;

        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("STATUS");
        tmpVo.setAttrValue(dto.getStatus());
        attr.add(tmpVo);
        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("MATERIAL_VERSION");
        tmpVo.setAttrValue(dto.getMaterialVersion());
        attr.add(tmpVo);

        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("SO_NUM");
        tmpVo.setAttrValue(dto.getSoNum());
        attr.add(tmpVo);

        tmpVo = new MtExtendVO5();
        tmpVo.setAttrName("SO_LINE_NUM");
        tmpVo.setAttrValue(dto.getSoLineNum());
        attr.add(tmpVo);

        String materialLotId = addMaiterialLot(tenantId, eventId, dto, materialCode);
        dto.setMaterialLotId(materialLotId);
        mtExtendSettingsRepository.attrPropertyUpdate(dto.getTenantId(), "mt_material_lot_attr", dto.getMaterialLotId(),
                eventId, attr);
    }


    private String addMaiterialLot(Long tenantId, String eventId, HmeImportMaterialLotVO materialLotVO, String materialCode) {
        MtMaterialLotVO2 mtMaterialLotVO2 = new MtMaterialLotVO2();
        BeanUtils.copyProperties(materialLotVO, mtMaterialLotVO2);
        mtMaterialLotVO2.setEventId(eventId);
        if (StringUtils.isBlank(materialLotVO.getLot())) {
            mtMaterialLotVO2.setLot("");
        }
        mtMaterialLotVO2.setEnableFlag("Y");
        mtMaterialLotVO2.setTenantId(tenantId);
        mtMaterialLotVO2.setPrimaryUomQty(Double.parseDouble(
                materialLotVO.getPrimaryUomQty() == null ? "0" : materialLotVO.getPrimaryUomQty()));
        mtMaterialLotVO2.setCid(Long.valueOf(customSequence.getNextKey("mt_material_lot_cid_s")));
        mtMaterialLotVO2.setCreateReason("INITIALIZE");
        if (org.springframework.util.StringUtils.isEmpty(mtMaterialLotVO2.getMaterialLotCode())) {
            mtMaterialLotVO2.setMaterialLotCode(numberGenerate(tenantId, materialLotVO.getSiteId(), materialLotVO.getSiteCode()));
        }
        mtMaterialLotVO2.setQualityStatus(materialLotVO.getQualityStatus());

        MtMaterialLotVO13 mtMaterialLotVO13 = wmsMaterialLotService.materialLotUpdate(tenantId, mtMaterialLotVO2, "N");
        log.info("<==== batch material lot import materialLotId:{}", mtMaterialLotVO13.getMaterialLotId());
        return mtMaterialLotVO13.getMaterialLotId();
    }

    /**
     * @param tenantId
     * @param siteId
     * @param siteCode
     * @return void
     * @Description 生成编码
     * @Date 2019/11/13 18:51
     * @Author weihua.liao
     */
    private String numberGenerate(Long tenantId, String siteId, String siteCode) {
        MtNumrangeVO2 mtNumrangeVO2 = new MtNumrangeVO2();
        Map<String, String> map = new HashMap<>(2);
        map.put("siteCode", siteCode);
        mtNumrangeVO2.setCallObjectCodeList(map);
        mtNumrangeVO2.setObjectCode("MATERIAL_LOT_CODE");
        mtNumrangeVO2.setSiteId(siteId);
        MtNumrangeVO5 mtNumrangeVO5 = mtNumrangeRepository.numrangeGenerate(tenantId, mtNumrangeVO2);
        if (null == mtNumrangeVO5) {
            log.error("<==== materialLotCreate numrangeGenerate is null");
            // HME_COS_FUNCTION_002 -> 物料批编码生成异常
            throw new MtException("HME_COS_FUNCTION_002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_FUNCTION_002", "HME"));
        }
        return mtNumrangeVO5.getNumber();
    }

    private HmeImportMaterialLotVO setImportVO(Long tenantId, HmeImportMaterialLotVO importVO) {
        importVO.setTenantId(tenantId);
        // 站点
        MtModSite mtModSite = getMtModSite(tenantId, importVO.getSiteCode());
        importVO.setSiteId(mtModSite.getSiteId());
        // 物料编码
        MtMaterial mtMaterial = getMtMaterial(tenantId, importVO.getMaterialCode());
        importVO.setMaterialId(mtMaterial.getMaterialId());
        // 单位
        String primaryUomCode = importVO.getPrimaryUomCode();
        MtUom mtUom = getMtModUom(tenantId, primaryUomCode);
        importVO.setPrimaryUomId(mtUom.getUomId());
        // 仓库、货位
        importVO = setLocator(tenantId, importVO);
        // 物流器具
        if (StringUtils.isNotBlank(importVO.getContainerCode())) {
            MtContainer mtContainer = getMtContainer(tenantId, importVO.getContainerCode());
            importVO.setContainerId(mtContainer.getContainerId());
        }
        // 供应商
        if (StringUtils.isNotBlank(importVO.getSupplierCode())) {
            MtSupplier mtSupplier = getMtSupplier(tenantId, importVO.getSupplierCode());
            importVO.setSupplierId(mtSupplier.getSupplierId());
        }
        return importVO;
    }

    /**
     * set仓库、货位
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private HmeImportMaterialLotVO setLocator(Long tenantId, HmeImportMaterialLotVO importVO) {
        List<String> list = new ArrayList<>();
        String parentLocatorCode = importVO.getParentLocatorCode();
        String locatorCode = importVO.getLocatorCode();
        list.add(parentLocatorCode);
        list.add(locatorCode);
        List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectModLocatorForCodes(tenantId, list);
        if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
            MtModLocator parentMtModLocator = getMtModLocatorByCode(parentLocatorCode, mtModLocatorList);
            MtModLocator mtModLocator = getMtModLocatorByCode(locatorCode, mtModLocatorList);
            importVO.setParentLocatorId(parentMtModLocator.getLocatorId());
            importVO.setLocatorId(mtModLocator.getLocatorId());
        }
        return importVO;
    }

    /**
     * 获取MtModLocatorList对应的其中一条数据
     *
     * @param code
     * @param mtModLocatorList
     * @return
     */
    private MtModLocator getMtModLocatorByCode(String code, List<MtModLocator> mtModLocatorList) {
        MtModLocator mtModLocatorOne = new MtModLocator();
        Optional<MtModLocator> codeOptional = mtModLocatorList.stream().filter(mtModLocator -> StringUtils.equals(code, mtModLocator.getLocatorCode())).findFirst();
        if (codeOptional.isPresent()) {
            mtModLocatorOne = codeOptional.get();
        }
        return mtModLocatorOne;
    }


    /**
     * 获取mt_supplier
     *
     * @param tenantId
     * @param supplierCode
     * @return
     */
    private MtSupplier getMtSupplier(Long tenantId, String supplierCode) {
        MtSupplier mtSupplier = mtSupplierRepository.selectOne(new MtSupplier() {{
            setTenantId(tenantId);
            setSupplierCode(supplierCode);
        }});
        return mtSupplier;
    }

    /**
     * 获取mt_container
     *
     * @param tenantId
     * @param containerCode
     * @return
     */
    private MtContainer getMtContainer(Long tenantId, String containerCode) {
        MtContainer container = new MtContainer();
        container.setTenantId(tenantId);
        container.setContainerCode(containerCode);
        return mtContainerRepository.selectOne(container);
    }

    /**
     * 校验单位
     *
     * @param tenantId
     * @param primaryUomCode
     * @return
     */
    private MtUom getMtModUom(Long tenantId, String primaryUomCode) {
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomCode(primaryUomCode);
        return mtUomRepository.selectOne(mtUom);
    }

    /**
     * 表mt_material
     *
     * @param tenantId
     * @param materialCode
     * @return
     */
    private MtMaterial getMtMaterial(Long tenantId, String materialCode) {
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(materialCode);
        return mtMaterialRepository.selectOne(mtMaterial);
    }

    /**
     * 表mt_mod_site
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private MtModSite getMtModSite(Long tenantId, String siteCode) {
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        return mtModSiteRepository.selectOne(mtModSite);
    }
}
