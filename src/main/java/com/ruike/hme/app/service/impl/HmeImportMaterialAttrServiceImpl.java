package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeMaterialAttrVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO5;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtPfepInventoryRepository;
import tarzan.material.domain.repository.MtUomRepository;
import tarzan.material.domain.vo.MtPfepInventoryVO6;
import tarzan.modeling.domain.entity.MtModLocator;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModLocatorRepository;
import tarzan.modeling.domain.repository.MtModSiteRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 物料存储属性导入模板
 *
 * @author yapeng.yao@hand-china.com 2020/09/10 11:33
 */
@ImportService(templateCode = "HME.MATERIAL_ATTR")
public class HmeImportMaterialAttrServiceImpl implements IBatchImportService {

    @Autowired
    private MtPfepInventoryRepository mtPfepInventoryRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtMaterialRepository mtMaterialRepository;

    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;

    @Autowired
    private MtUomRepository mtUomRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LovAdapter lovAdapter;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Override
    public Boolean doImport(List<String> data) {
        //获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                HmeMaterialAttrVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, HmeMaterialAttrVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 校验数据
                importVO = checkHmeMaterialAttrVO(tenantId, importVO);
                //处理数据
                MtPfepInventoryVO6 mtPfepInventoryVO6 = new MtPfepInventoryVO6();
                BeanUtils.copyProperties(importVO, mtPfepInventoryVO6);

                String pfepInventoryId = mtPfepInventoryRepository.materialPfepInventoryUpdate(tenantId, mtPfepInventoryVO6, "N");

                List<MtExtendVO5> mtExtendVO5List = setMtExtendVO5List(importVO.getLocatorRecomMode());
                mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_pfep_inventory_attr", pfepInventoryId, "", mtExtendVO5List);
            }
        }
        return true;
    }

    /**
     * 设置扩展表字段
     * @param locatorRecomMode
     * @return
     */
    private List<MtExtendVO5> setMtExtendVO5List(String locatorRecomMode) {
        List<MtExtendVO5> mtExtendVO5List = new ArrayList<MtExtendVO5>();
        MtExtendVO5 mtExtendVO5 = new MtExtendVO5();
        mtExtendVO5.setAttrName("LOCATOR_RECOM_MODE");
        mtExtendVO5.setAttrValue(locatorRecomMode);
        mtExtendVO5List.add(mtExtendVO5);
        return mtExtendVO5List;
    }

    /**
     * 将输入参数的Code-->ID
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private HmeMaterialAttrVO checkHmeMaterialAttrVO(Long tenantId, HmeMaterialAttrVO importVO) {
        // 站点
        String siteId = checkSite(tenantId, importVO.getSiteCode());
        importVO.setSiteId(siteId);
        // 物料编码
        String materialId = checkMaterial(tenantId, importVO.getMaterialCode());
        importVO.setMaterialId(materialId);
        // 默认存储货位、默认发料库位、默认完工库位
        importVO = checkMtModLocator(tenantId, importVO.getStockLocatorCode(), importVO.getIssuedLocatorCode(), importVO.getCompletionLocatorCode(), importVO);
        // 包装单位
        if (StringUtils.isNotBlank(importVO.getPackageSizeUomCode())) {
            String packageSizeUomId = checkMtModUom(tenantId, importVO.getPackageSizeUomCode());
            importVO.setPackageSizeUomId(packageSizeUomId);
        }
        // 是否有效
        if (StringUtils.isBlank(importVO.getEnableFlag())) {
            // 必输字段${1}为空,请检查!
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "是否有效"));
        }
        return importVO;
    }

    /**
     * 校验单位
     * @param tenantId
     * @param packageSizeUomCode
     * @return
     */
    private String checkMtModUom(Long tenantId, String packageSizeUomCode) {
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomCode(packageSizeUomCode);
        mtUom = mtUomRepository.selectOne(mtUom);
        if (Objects.isNull(mtUom)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "包装单位", packageSizeUomCode));
        }
        return mtUom.getUomId();
    }

    /**
     * 校验数据
     * @param tenantId
     * @param stockLocatorCode
     * @param issuedLocatorCode
     * @param completionLocatorCode
     * @param importVO
     * @return
     */
    private HmeMaterialAttrVO checkMtModLocator(Long tenantId, String stockLocatorCode, String issuedLocatorCode, String completionLocatorCode, HmeMaterialAttrVO importVO) {
        List<String> list = new ArrayList<>();
        if (StringUtils.isNotBlank(stockLocatorCode)) {
            list.add(stockLocatorCode);
        }
        if (StringUtils.isNotBlank(issuedLocatorCode)) {
            list.add(issuedLocatorCode);
        }
        if (StringUtils.isNotBlank(completionLocatorCode)) {
            list.add(completionLocatorCode);
        }
        List<MtModLocator> mtModLocatorList = mtModLocatorRepository.selectModLocatorForCodes(tenantId, list);
        if (CollectionUtils.isNotEmpty(mtModLocatorList)) {
            if (StringUtils.isNotBlank(stockLocatorCode)) {
                List<MtModLocator> stockLocatorList = mtModLocatorList.stream().filter(mtModLocator -> StringUtils.equals(stockLocatorCode, mtModLocator.getLocatorCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(stockLocatorList)) {
                    importVO.setStockLocatorId(stockLocatorList.get(0).getLocatorId());
                } else {
                    // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                    throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "默认存储货位", stockLocatorCode));
                }
            }
            if (StringUtils.isNotBlank(issuedLocatorCode)) {
                List<MtModLocator> issuedLocatorList = mtModLocatorList.stream().filter(mtModLocator -> StringUtils.equals(issuedLocatorCode, mtModLocator.getLocatorCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(issuedLocatorList)) {
                    importVO.setIssuedLocatorId(issuedLocatorList.get(0).getLocatorId());
                } else {
                    // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                    throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "默认发料库位", issuedLocatorCode));
                }
            }
            if (StringUtils.isNotBlank(completionLocatorCode)) {
                List<MtModLocator> completionLocatorList = mtModLocatorList.stream().filter(mtModLocator -> StringUtils.equals(completionLocatorCode, mtModLocator.getLocatorCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(completionLocatorList)) {
                    importVO.setCompletionLocatorId(completionLocatorList.get(0).getLocatorId());
                } else {
                    // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                    throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "默认完工库位", completionLocatorCode));
                }
            }
        } else if (CollectionUtils.isNotEmpty(list)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "库位", list.toString()));
        }
        return importVO;
    }

    /**
     * 表mt_material
     *
     * @param tenantId
     * @param materialCode
     * @return
     */
    private String checkMaterial(Long tenantId, String materialCode) {
        if (StringUtils.isBlank(materialCode)) {
            // 必输字段${1}为空,请检查!
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "物料编码"));
        }
        MtMaterial mtMaterial = new MtMaterial();
        mtMaterial.setTenantId(tenantId);
        mtMaterial.setMaterialCode(materialCode);
        mtMaterial = mtMaterialRepository.selectOne(mtMaterial);
        if (Objects.isNull(mtMaterial)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "物料编码", materialCode));
        }
        return mtMaterial.getMaterialId();
    }

    /**
     * 表mt_mod_site
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private String checkSite(Long tenantId, String siteCode) {
        if (StringUtils.isBlank(siteCode)) {
            // 必输字段${1}为空,请检查!
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "站点"));
        }
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        mtModSite = mtModSiteRepository.selectOne(mtModSite);
        if (Objects.isNull(mtModSite)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "站点", siteCode));
        }
        return mtModSite.getSiteId();
    }
}
