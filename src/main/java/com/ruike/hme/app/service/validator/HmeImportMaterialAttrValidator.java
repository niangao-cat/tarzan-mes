package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.domain.vo.HmeMaterialAttrVO;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.springframework.beans.factory.annotation.Autowired;
import tarzan.material.domain.entity.MtMaterial;
import tarzan.material.domain.entity.MtUom;
import tarzan.material.domain.repository.MtMaterialRepository;
import tarzan.material.domain.repository.MtUomRepository;
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
@ImportValidators({@ImportValidator(templateCode = "HME.MATERIAL_ATTR")})
public class HmeImportMaterialAttrValidator extends ValidatorHandler {
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

    /**
     * 校验
     *
     * @author penglin.sui@hand-china.com 2020/8/18 20:30
     */
    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        boolean flag = true;
        if (data != null && !"".equals(data)) {
            HmeMaterialAttrVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeMaterialAttrVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 校验数据
            flag = checkHmeMaterialAttrVO(tenantId, importVO);
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
    private Boolean checkHmeMaterialAttrVO(Long tenantId, HmeMaterialAttrVO importVO) {
        // 站点
        MtModSite mtModSite = checkSite(tenantId, importVO.getSiteCode());
        if (Objects.isNull(mtModSite)) {
            // 必输字段${1}为空,请检查!
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "站点"));
            return false;
        }
        // 物料编码
        MtMaterial mtMaterial = checkMaterial(tenantId, importVO.getMaterialCode());
        if (Objects.isNull(mtMaterial)) {
            // 必输字段${1}为空,请检查!
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "物料编码"));
            return false;
        }
        // 默认存储货位、默认发料库位、默认完工库位
        boolean mtModLocatorFlag = checkMtModLocator(tenantId, importVO.getStockLocatorCode(), importVO.getIssuedLocatorCode(), importVO.getCompletionLocatorCode(), importVO);
        if (!mtModLocatorFlag) {
            return false;
        }
        // 包装单位
        String packageSizeUomCode = importVO.getPackageSizeUomCode();
        if (StringUtils.isNotBlank(packageSizeUomCode)) {
            MtUom mtUom = checkMtModUom(tenantId, packageSizeUomCode);
            if (Objects.isNull(mtUom)) {
                // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ASSEMBLE_0004", "ASSEMBLE", "包装单位", packageSizeUomCode));
                return false;
            }
        }
        // 是否有效
        if (StringUtils.isBlank(importVO.getEnableFlag())) {
            // 必输字段${1}为空,请检查!
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "是否有效"));
            return false;
        }
        return true;
    }

    /**
     * 校验单位
     *
     * @param tenantId
     * @param packageSizeUomCode
     * @return
     */
    private MtUom checkMtModUom(Long tenantId, String packageSizeUomCode) {
        MtUom mtUom = new MtUom();
        mtUom.setTenantId(tenantId);
        mtUom.setUomCode(packageSizeUomCode);
        return mtUomRepository.selectOne(mtUom);
    }

    /**
     * 校验数据
     *
     * @param tenantId
     * @param stockLocatorCode
     * @param issuedLocatorCode
     * @param completionLocatorCode
     * @param importVO
     * @return
     */
    private Boolean checkMtModLocator(Long tenantId, String stockLocatorCode, String issuedLocatorCode, String completionLocatorCode, HmeMaterialAttrVO importVO) {
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
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "默认存储货位", stockLocatorCode));
                    return false;
                }
            }
            if (StringUtils.isNotBlank(issuedLocatorCode)) {
                List<MtModLocator> issuedLocatorList = mtModLocatorList.stream().filter(mtModLocator -> StringUtils.equals(issuedLocatorCode, mtModLocator.getLocatorCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(issuedLocatorList)) {
                    importVO.setIssuedLocatorId(issuedLocatorList.get(0).getLocatorId());
                } else {
                    // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "默认发料库位", issuedLocatorCode));
                    return false;
                }
            }
            if (StringUtils.isNotBlank(completionLocatorCode)) {
                List<MtModLocator> completionLocatorList = mtModLocatorList.stream().filter(mtModLocator -> StringUtils.equals(completionLocatorCode, mtModLocator.getLocatorCode())).collect(Collectors.toList());
                if (CollectionUtils.isNotEmpty(completionLocatorList)) {
                    importVO.setCompletionLocatorId(completionLocatorList.get(0).getLocatorId());
                } else {
                    // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ASSEMBLE_0004", "ASSEMBLE", "默认完工库位", completionLocatorCode));
                    return false;
                }
            }
        } else if (CollectionUtils.isNotEmpty(list)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "库位", list.toString()));
            return false;
        }
        return true;
    }

    /**
     * 表mt_material
     *
     * @param tenantId
     * @param materialCode
     * @return
     */
    private MtMaterial checkMaterial(Long tenantId, String materialCode) {
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
