package com.ruike.hme.app.service.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.domain.vo.HmeUserOrganizationVO;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.ValidatorHandler;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidator;
import org.hzero.boot.imported.infra.validator.annotation.ImportValidators;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.opensaml.xml.signature.Y;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static io.tarzan.common.domain.util.MtBaseConstants.ORGANIZATION_TYPE.*;

/**
 * 用户权限导入校验
 *
 * @author yapeng.yao@hand-china.com 2020/09/12 11:25
 */
@Slf4j
@ImportValidators({@ImportValidator(templateCode = "MT.USER_ORGANIZATION")})
public class HmeImportUserOrganizationValidator extends ValidatorHandler {
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;
    @Autowired
    private LovAdapter lovAdapter;

    /**
     * 校验
     *
     * @author yapeng.yao@hand-china.com 2020/09/12 11:25
     */
    @Override
    public boolean validate(String data) {
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (tenantId == null) {
            tenantId = 0L;
        }
        boolean flag = true;
        if (data != null && !"".equals(data)) {
            HmeUserOrganizationVO importVO = null;
            try {
                importVO = objectMapper.readValue(data, HmeUserOrganizationVO.class);
            } catch (IOException e) {
                // 失败
                return false;
            }
            // 校验数据
            flag = checkImportInfo(tenantId, importVO);
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
    private Boolean checkImportInfo(Long tenantId, HmeUserOrganizationVO importVO) {
        log.info("=============================importVO=>"+importVO.toString());
        String organizationType = importVO.getOrganizationType();
        String userCode = importVO.getUserCode();
        // 员工工号
        String userId = checkUserCode(tenantId, userCode);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        String organizationCode = importVO.getOrganizationCode();
        // 组织编码
        String organizationId = checkOrganization(tenantId, organizationType, organizationCode);
        if (StringUtils.isBlank(organizationId)) {
            return false;
        }
        // 默认组织标识
        // 用户${1}组织类型为${2}的组织编码${3}已存在
        boolean errFlag = checkDefaultOrganizationFlag(tenantId, userId, organizationType, userCode, importVO.getDefaultOrganizationFlag());
        if (!errFlag) {
            return false;
        }
        // 是否有效
        if (StringUtils.isBlank(importVO.getEnableFlag())) {
            // 必输字段${1}为空,请检查!
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "是否有效"));
            return false;
        }

        //权限授权校验
        if("LOCATOR".equals(importVO.getOrganizationType())){
            log.info("===========>" + importVO.getDocType() + importVO.getLocationType() + importVO.getOperationType() + importVO.getPrivilegeEnableFlag());
            // 单据类型、操作类型、权限分配是否有效应同时有值或者无值
            boolean haveValueFlag = StringUtils.isNotBlank(importVO.getDocType()) && StringUtils.isNotBlank(importVO.getOperationType()) && StringUtils.isNotBlank(importVO.getPrivilegeEnableFlag());
            boolean nonHaveValueFlag = StringUtils.isBlank(importVO.getDocType()) && StringUtils.isBlank(importVO.getOperationType()) && StringUtils.isBlank(importVO.getPrivilegeEnableFlag());
            if (!(haveValueFlag || nonHaveValueFlag)) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PERMISSION_0001", "WMS"));
                return false;
            }
            // 获取值集
            List<LovValueDTO> typeLovList = lovAdapter.queryLovValue("WMS.USER_DOC_TYPE", tenantId);
            String tag = "";
            for (LovValueDTO type : typeLovList) {
                if (StringUtils.equals(type.getValue(), importVO.getDocType())) {
                    tag = type.getTag();
                }
            }
            //单据类型有值且当其对应标记为Y时,仓库类型必输,否则不可输入!
            if (!StringUtils.isBlank(importVO.getDocType()) && "Y".equalsIgnoreCase(tag)) {
                if (StringUtils.isBlank(importVO.getLocationType())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PERMISSION_0003", "WMS"));
                    return false;
                }
            } else {
                if (!StringUtils.isBlank(importVO.getLocationType())) {
                    getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PERMISSION_0003", "WMS"));
                    return false;
                }
            }
        }else {
            //组织层级不为库存时,单据类型、仓库类型、操作类型、权限分配不能有值!
            if (StringUtils.isNotBlank(importVO.getDocType()) || StringUtils.isNotBlank(importVO.getLocationType()) || StringUtils.isNotBlank(importVO.getOperationType()) || StringUtils.isNotBlank(importVO.getPrivilegeEnableFlag())) {
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PERMISSION_0002", "WMS"));
                return false;
            }
        }
        return true;
    }

    /**
     * 一个用户只能有一个Y
     *
     * @param tenantId
     * @param userId
     * @param organizationType
     * @param userCode
     * @param defaultOrganizationFlag
     */
    private boolean checkDefaultOrganizationFlag(Long tenantId, String userId, String organizationType, String userCode, String defaultOrganizationFlag) {
        boolean flag = true;
        // 一个用户同一组织类型只能有一个Y
        if (YES.equalsIgnoreCase(defaultOrganizationFlag)) {
            MtUserOrganization mtUserOrganization = new MtUserOrganization();
            mtUserOrganization.setTenantId(tenantId);
            mtUserOrganization.setUserId(Long.valueOf(userId));
            mtUserOrganization.setOrganizationType(organizationType);
            mtUserOrganization.setDefaultOrganizationFlag(defaultOrganizationFlag);
            MtUserOrganization mtUserOrganizationOne = mtUserOrganizationRepository.selectOne(mtUserOrganization);
            if (Objects.nonNull(mtUserOrganizationOne)) {
                // MT_USER_ORGANIZATION_001 -->用户${1}在组织类型${2}下已存在默认状态数据
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_USER_ORGANIZATION_001", "HME", userCode, organizationType));
                flag = false;
            }
        }
        return flag;
    }

    /**
     * 获取组织编码
     *
     * @param tenantId
     * @param organizationType
     * @param organizationCode
     * @return
     */
    private String checkOrganization(Long tenantId, String organizationType, String organizationCode) {
        String organizationId = "";
        if (StringUtils.isBlank(organizationCode)) {
            // 必输字段${1}为空,请检查!
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "组织编码"));
        } else {
            if (SITE.equalsIgnoreCase(organizationType)) {
                // mt_mod_site
                organizationId = checkMtModSite(tenantId, organizationCode);
            } else if (AREA.equalsIgnoreCase(organizationType)) {
                // mt_mod_area
                organizationId = checkMtModArea(tenantId, organizationCode);
            } else if (PROD_LINE.equalsIgnoreCase(organizationType)) {
                // mt_mod_production_line
                organizationId = checkMtModProdLine(tenantId, organizationCode);
            } else if (WORKCELL.equalsIgnoreCase(organizationType)) {
                // mt_mod_workcell
                organizationId = checkMtModWorkcell(tenantId, organizationCode);
            } else if (LOCATOR.equalsIgnoreCase(organizationType)) {
                // mt_mod_locator
                organizationId = checkMtModLocator(tenantId, organizationCode);
            }
            if (StringUtils.isBlank(organizationId)) {
                // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ASSEMBLE_0004", "ASSEMBLE", "组织编码", organizationId));
            }
        }
        return organizationId;
    }

    /**
     * mt_mod_locator
     *
     * @param tenantId
     * @param organizationCode
     * @return
     */
    private String checkMtModLocator(Long tenantId, String organizationCode) {
        MtModLocator mtModLocator = new MtModLocator();
        mtModLocator.setTenantId(tenantId);
        mtModLocator.setLocatorCode(organizationCode);
        mtModLocator = mtModLocatorRepository.selectOne(mtModLocator);
        if (Objects.isNull(mtModLocator)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "库位", organizationCode));
        }
        return mtModLocator.getLocatorId();
    }

    /**
     * mt_mod_workcell
     *
     * @param tenantId
     * @param workcellCode
     * @return
     */
    private String checkMtModWorkcell(Long tenantId, String workcellCode) {
        String workCellId = "";
        MtModWorkcell mtModWorkcell = new MtModWorkcell();
        mtModWorkcell.setTenantId(tenantId);
        mtModWorkcell.setWorkcellCode(workcellCode);
        mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
        if (Objects.isNull(mtModWorkcell)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "生产线", workcellCode));
        } else {
            workCellId = mtModWorkcell.getWorkcellId();
        }
        return workCellId;
    }

    /**
     * mt_mod_production_line
     *
     * @param tenantId
     * @param prodLineCode
     * @return
     */
    private String checkMtModProdLine(Long tenantId, String prodLineCode) {
        String prodLineId = "";
        MtModProductionLine mtModProductionLine = new MtModProductionLine();
        mtModProductionLine.setTenantId(tenantId);
        mtModProductionLine.setProdLineCode(prodLineCode);
        mtModProductionLine = mtModProductionLineRepository.selectOne(mtModProductionLine);
        if (Objects.isNull(mtModProductionLine)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "生产线", prodLineCode));
        } else {
            prodLineId = mtModProductionLine.getProdLineId();
        }
        return prodLineId;
    }

    /**
     * mt_mod_area
     *
     * @param tenantId
     * @param areaCode
     * @return
     */
    private String checkMtModArea(Long tenantId, String areaCode) {
        String areaId = "";
        MtModArea mtModArea = new MtModArea();
        mtModArea.setTenantId(tenantId);
        mtModArea.setAreaCode(areaCode);
        mtModArea = mtModAreaRepository.selectOne(mtModArea);
        if (Objects.isNull(mtModArea)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "区域", areaCode));
        } else {
            areaId = mtModArea.getAreaId();
        }
        return areaId;
    }

    /**
     * hzero_platform.iam_user
     *
     * @param tenantId
     * @param userCode
     * @return
     */
    private String checkUserCode(Long tenantId, String userCode) {
        String userId = "";
        if (StringUtils.isBlank(userCode)) {
            // 必输字段${1}为空,请检查!
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "员工工号"));
        } else {
            ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, userCode, "P");
            if (userInfo.getBody().getId() != null) {
                userId = userInfo.getBody().getId();
            } else {
                // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
                getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_ASSEMBLE_0004", "ASSEMBLE", "员工工号", userCode));
            }
        }
        return userId;
    }

    /**
     * mt_mod_site
     *
     * @param tenantId
     * @param siteCode
     * @return
     */
    private String checkMtModSite(Long tenantId, String siteCode) {
        String siteId = "";
        MtModSite mtModSite = new MtModSite();
        mtModSite.setTenantId(tenantId);
        mtModSite.setSiteCode(siteCode);
        mtModSite = mtModSiteRepository.selectOne(mtModSite);
        if (Objects.isNull(mtModSite)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            getContext().addErrorMsg(mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "站点", siteCode));
        } else {
            siteId = mtModSite.getSiteId();
        }
        return siteId;
    }
}
