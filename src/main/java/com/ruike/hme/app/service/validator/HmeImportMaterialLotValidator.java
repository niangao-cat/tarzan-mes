package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeImportMaterialLotVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtContainer;
import tarzan.inventory.domain.entity.MtContainerLoadDetail;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtContainerLoadDetailRepository;
import tarzan.inventory.domain.repository.MtContainerRepository;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
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
 * 物料存储属性导入模板
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@ImportValidators({@ImportValidator(templateCode = "HME.MATERIAL_LOT")})
public class HmeImportMaterialLotValidator extends ValidatorHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtMaterialRepository mtMaterialRepository;
    @Autowired
    private MtUomRepository mtUomRepository;
    @Autowired
    private MtContainerRepository mtContainerRepository;
    @Autowired
    private MtContainerLoadDetailRepository mtContainerLoadDetailRepository;
    @Autowired
    MtSupplierRepository mtSupplierRepository;
    @Autowired
    private MtMaterialLotRepository mtMaterialLotRepository;

    /**
     * 校验
     *
     * @author yapeng.yao@hand-china.com 2020/9/24 15:30
     */
    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        boolean flag = true;
        if (data != null && !"".equals(data)) {
            HmeImportMaterialLotVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeImportMaterialLotVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 校验数据
            flag = checkHmeImportMaterialLotVO(tenantId, importVO);
        }
        return flag;
    }


    /**
     * 将输入参数的Code-->ID
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private Boolean checkHmeImportMaterialLotVO(Long tenantId, HmeImportMaterialLotVO importVO) {
        // 校验非空
        boolean notBlankFlag = checkInfoNotBlank(tenantId, importVO);
        if (!notBlankFlag) {
            return false;
        }
        // 站点
        MtModSite mtModSite = checkSite(tenantId, importVO.getSiteCode());
        if (Objects.isNull(mtModSite)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "站点", importVO.getSiteCode()));
            return false;
        }
        // 物料编码
        MtMaterial mtMaterial = getMtMaterial(tenantId, importVO.getMaterialCode());
        if (Objects.isNull(mtMaterial)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "物料编码", importVO.getMaterialCode()));
            return false;
        }
        // 单位
        String primaryUomCode = importVO.getPrimaryUomCode();
        boolean primaryUomCodeExistFlag = checkPrimaryUomCode(tenantId, primaryUomCode, mtMaterial.getPrimaryUomId());
        if (!primaryUomCodeExistFlag) {
            return false;
        }
        // 仓库是货位的父locator_id
        boolean mtModLocatorFlag = checkLocator(tenantId, importVO.getParentLocatorCode(), importVO.getLocatorCode());
        if (!mtModLocatorFlag) {
            return false;
        }
        // 物流器具
        if (StringUtils.isNotBlank(importVO.getContainerCode())) {
            MtContainer mtContainer = getMtContainer(tenantId, importVO.getContainerCode());
            if (Objects.isNull(mtContainer)) {
                // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ASSEMBLE_0004", "ASSEMBLE", "物流器具", importVO.getContainerCode()));
                return false;
            } else {
                String locatorId = mtContainer.getLocatorId();
                String containerId = mtContainer.getContainerId();
                MtModLocator mtModLocator = getMtModLocator(tenantId, importVO.getLocatorCode());
                if (Objects.nonNull(mtModLocator) && !StringUtils.equals(locatorId, mtModLocator.getLocatorId())) {
                    // HME_MATERIAL_LOT_002 --> 容器和物料批货位不一致
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_MATERIAL_LOT_002", "HME"));
                    return false;
                }
                String materialLotCode = importVO.getMaterialCode();
                if (!StringUtils.isEmpty(materialLotCode)) {
                    MtMaterialLot mtMaterialLot = getMtMaterialLot(tenantId, materialLotCode);
                    if (Objects.nonNull(mtMaterialLot)) {
                        MtContainerLoadDetail mtContainerLoadDetail = getMtContainerLoadDetail(tenantId, mtMaterialLot.getMaterialLotId());
                        if (Objects.nonNull(mtContainerLoadDetail) && !StringUtils.equals(mtContainerLoadDetail.getContainerId(), containerId)) {
                            // HME_MATERIAL_LOT_003 -> 物料批已经装载在容器中
                            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                    "HME_MATERIAL_LOT_003", "HME"));
                            return false;
                        }
                    }
                }
            }
        }
        // 供应商
        if (StringUtils.isNotBlank(importVO.getSupplierCode())) {
            MtSupplier mtSupplier = getMtSupplier(tenantId, importVO.getSupplierCode());
            if (Objects.isNull(mtSupplier)) {
                // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ASSEMBLE_0004", "ASSEMBLE", "供应商", importVO.getSupplierCode()));
                return false;
            }
        }
        return true;
    }

    /**
     * mt_material_lot
     *
     * @param tenantId
     * @param materialLotCode
     * @return
     */
    private MtMaterialLot getMtMaterialLot(Long tenantId, String materialLotCode) {
        MtMaterialLot mtMaterialLot = new MtMaterialLot();
        mtMaterialLot.setTenantId(tenantId);
        mtMaterialLot.setMaterialLotCode(materialLotCode);
        return mtMaterialLotRepository.selectOne(mtMaterialLot);
    }

    /**
     * mt_container_load_detail
     *
     * @param tenantId
     * @param materialId
     */
    private MtContainerLoadDetail getMtContainerLoadDetail(Long tenantId, String materialId) {
        MtContainerLoadDetail mtContainerLoadDetail = new MtContainerLoadDetail();
        mtContainerLoadDetail.setTenantId(tenantId);
        mtContainerLoadDetail.setLoadObjectType("MATERIAL_LOT");
        mtContainerLoadDetail.setLoadObjectId(materialId);
        return mtContainerLoadDetailRepository.selectOne(mtContainerLoadDetail);
    }

    /**
     * mt_mod_locator
     *
     * @param tenantId
     * @param locatorCode
     * @return
     */
    private MtModLocator getMtModLocator(Long tenantId, String locatorCode) {
        return mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(locatorCode);
        }});
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
     * 仓库是货位的父locator_id
     *
     * @param tenantId
     * @param parentLocatorCode
     * @param locatorCode
     * @return
     */
    private boolean checkLocator(Long tenantId, String parentLocatorCode, String locatorCode) {
        boolean flag = true;
        List<String> list = new ArrayList<>();
        list.add(parentLocatorCode);
        list.add(locatorCode);
        List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectModLocatorForCodes(tenantId, list);
        if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
            MtModLocator parentMtModLocator = getMtModLocatorByCode(tenantId, parentLocatorCode, mtModLocatorList, "仓库");
            MtModLocator mtModLocator = getMtModLocatorByCode(tenantId, locatorCode, mtModLocatorList, "货位");
            if (Objects.nonNull(parentMtModLocator) && Objects.nonNull(mtModLocator)) {
                if (!StringUtils.equals(mtModLocator.getParentLocatorId(), parentMtModLocator.getLocatorId())) {
                    // HME_MATERIAL_LOT_001 --> 货位仓库对应关系错误
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "HME_MATERIAL_LOT_001", "HME"));
                    flag = false;
                }
            } else {
                flag = false;
            }
        } else {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "仓库", parentLocatorCode));
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "货位", locatorCode));
            flag = false;
        }
        return flag;
    }

    private MtModLocator getMtModLocatorByCode(Long tenantId, String code, List<MtModLocator> mtModLocatorList, String message) {
        MtModLocator mtModLocatorOne = new MtModLocator();
        Optional<MtModLocator> codeOptional = mtModLocatorList.stream().filter(mtModLocator -> StringUtils.equals(code, mtModLocator.getLocatorCode())).findFirst();
        if (codeOptional.isPresent()) {
            mtModLocatorOne = codeOptional.get();
        } else {
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", message, code));
        }
        return mtModLocatorOne;
    }

    /**
     * 校验单位
     *
     * @param tenantId
     * @param primaryUomCode
     * @param primaryUomId
     * @return
     */
    private boolean checkPrimaryUomCode(Long tenantId, String primaryUomCode, String primaryUomId) {
        boolean flag = true;
        // 在mt_uom存在
        MtUom mtUom = getMtModUom(tenantId, primaryUomCode);
        if (Objects.isNull(mtUom)) {
            flag = false;
        } else {
            // 与mt_material的PRIMARY_UOM_ID相同
            if (!StringUtils.equals(mtUom.getUomId(), primaryUomId)) {
                flag = false;
            }
        }
        if (!flag) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "包装单位", primaryUomCode));
        }
        return flag;

    }

    /**
     * 校验非空
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private boolean checkInfoNotBlank(Long tenantId, HmeImportMaterialLotVO importVO) {
        boolean flag = true;
        Map<String, String> map = new HashMap<>();
        map.put(importVO.getSiteCode(), "工厂");
        map.put(importVO.getStatus(), "条码状态");
        map.put(importVO.getMaterialCode(), "物料编码");
        map.put(importVO.getPrimaryUomQty(), "数量");
        map.put(importVO.getPrimaryUomCode(), "单位");
        map.put(importVO.getParentLocatorCode(), "仓库");
        map.put(importVO.getLocatorCode(), "货位");
        map.put(importVO.getQualityStatus(), "当前质量状态");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (StringUtils.isBlank(entry.getKey())) {
                // 必输字段${1}为空,请检查!
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_QR_CODE_0003", "WMS", entry.getValue()));
                flag = false;
            }
        }
        return flag;
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
    private MtModSite checkSite(Long tenantId, String siteCode) {
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        return mtModSiteRepository.selectOne(mtModSite);
    }
}
