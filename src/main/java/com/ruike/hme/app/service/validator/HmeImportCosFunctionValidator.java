package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeImportCosFunctionVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.inventory.domain.entity.MtMaterialLot;
import tarzan.inventory.domain.repository.MtMaterialLotRepository;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.entity.MtSupplier;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;
import tarzan.modeling.domain.repository.MtSupplierRepository;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 性能表导入模板
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@ImportValidators({@ImportValidator(templateCode = "HME.COS_FUNCTION")})
public class HmeImportCosFunctionValidator extends ValidatorHandler {
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
    private MtSupplierRepository mtSupplierRepository;
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
            HmeImportCosFunctionVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeImportCosFunctionVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 校验数据
            flag = checkHmeImportCosFunctionVO(tenantId, importVO);
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
    private Boolean checkHmeImportCosFunctionVO(Long tenantId, HmeImportCosFunctionVO importVO) {
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
        // 条码号
        MtMaterialLot mtMaterialLot = getMtMaterialLot(tenantId, importVO.getMaterialLotCode());
        if (Objects.nonNull(mtMaterialLot) && !StringUtils.equals(mtMaterialLot.getMaterialId(), mtMaterial.getMaterialId())) {
            // HME_COS_FUNCTION_001 -> 条码已存在且物料和导入模板不一致
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "HME_COS_FUNCTION_001", "HME"));
            return false;
        }
        // 货位
        MtModLocator mtModLocator = getMtModLocator(tenantId, importVO.getLocatorCode());
        if (Objects.isNull(mtModLocator)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "货位", importVO.getMaterialLotCode()));
            return false;
        }
        return true;
    }

    /**
     * mt_mod_locator
     *
     * @param tenantId
     * @param locatorCode
     * @return
     */
    private MtModLocator getMtModLocator(Long tenantId, String locatorCode) {
        MtModLocator mtModLocator = mtModLocatorRepository.selectOne(new MtModLocator() {{
            setTenantId(tenantId);
            setLocatorCode(locatorCode);
        }});
        return mtModLocator;
    }

    /**
     * 获取mt_material_lot
     *
     * @param tenantId
     * @param materialLotCode
     * @return
     */
    private MtMaterialLot getMtMaterialLot(Long tenantId, String materialLotCode) {
        MtMaterialLot mtMaterialLot = mtMaterialLotRepository.selectOne(new MtMaterialLot() {{
            setTenantId(tenantId);
            setMaterialLotCode(materialLotCode);
        }});
        return mtMaterialLot;
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
     * 校验非空
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private boolean checkInfoNotBlank(Long tenantId, HmeImportCosFunctionVO importVO) {
        boolean flag = true;
        Map<String, String> map = new HashMap<>();
        map.put(importVO.getSiteCode(), "公司编码");
        map.put(importVO.getMaterialLotCode(), "条码号");
        map.put(importVO.getMaterialCode(), "物料编码");
        map.put(importVO.getPrimaryUomQty(), "数量");
        map.put(importVO.getLocatorCode(), "货位");
        map.put(importVO.getLot(), "批次号");
        map.put(importVO.getLoadRowCol(), "芯片位置");
        map.put(importVO.getWafer(), "wafer");
        map.put(importVO.getCosType(), "COS类型");
        map.put(importVO.getHotSinkCode(), "芯片序列号");
        map.put(importVO.getCurrent(), "电流");
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
        return mtModSiteRepository.selectOne(new MtModSite() {{
            setTenantId(tenantId);
            setSiteCode(siteCode);
        }});
    }
}
