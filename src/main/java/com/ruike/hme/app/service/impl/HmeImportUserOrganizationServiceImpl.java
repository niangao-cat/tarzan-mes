package com.ruike.hme.app.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruike.hme.api.dto.HmeHzeroIamUserDTO;
import com.ruike.hme.domain.vo.HmeUserOrganizationVO;
import com.ruike.hme.infra.feign.HmeHzeroIamFeignClient;
import com.ruike.wms.domain.entity.WmsDocPrivilege;
import com.ruike.wms.domain.repository.WmsDocPrivilegeRepository;
import com.ruike.wms.infra.mapper.WmsDocPrivilegeMapper;
import io.choerodon.core.oauth.DetailsHelper;
import io.tarzan.common.domain.repository.MtCustomDbRepository;
import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.boot.imported.app.service.IBatchImportService;
import org.hzero.boot.imported.infra.validator.annotation.ImportService;
import org.hzero.boot.platform.lov.adapter.LovAdapter;
import org.hzero.boot.platform.lov.dto.LovValueDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import tarzan.general.domain.entity.MtUserOrganization;
import tarzan.general.domain.repository.MtUserOrganizationRepository;
import tarzan.general.infra.mapper.MtUserOrganizationMapper;
import tarzan.modeling.domain.entity.*;
import tarzan.modeling.domain.repository.*;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.ruike.hme.infra.constant.HmeConstants.ConstantValue.YES;
import static io.tarzan.common.domain.util.MtBaseConstants.ORGANIZATION_TYPE.*;

/**
 * 用户权限导入
 *
 * @author yapeng.yao@hand-china.com 2020/09/12 11:25*/

@ImportService(templateCode = "MT.USER_ORGANIZATION")
public class HmeImportUserOrganizationServiceImpl implements IBatchImportService {

    @Autowired
    private MtUserOrganizationRepository mtUserOrganizationRepository;
    @Autowired
    private MtModSiteRepository mtModSiteRepository;
    @Autowired
    private MtModAreaRepository mtModAreaRepository;
    @Autowired
    private MtModProductionLineRepository mtModProductionLineRepository;
    @Autowired
    private MtModLocatorRepository mtModLocatorRepository;
    @Autowired
    private MtModWorkcellRepository mtModWorkcellRepository;
    @Autowired
    private HmeHzeroIamFeignClient hmeHzeroIamFeignClient;
    @Autowired
    private MtCustomDbRepository mtCustomDbRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;
    @Autowired
    private WmsDocPrivilegeMapper wmsDocPrivilegeMapper;
    @Autowired
    private WmsDocPrivilegeRepository wmsDocPrivilegeRepository;
    @Autowired
    private MtUserOrganizationMapper mtUserOrganizationMapper;
    @Autowired
    private LovAdapter lovAdapter;

    @Override
    public Boolean doImport(List<String> data) {
        //获取租户Id
        Long tenantId = DetailsHelper.getUserDetails().getTenantId();
        if (CollectionUtils.isNotEmpty(data)) {
            for (String vo : data) {
                HmeUserOrganizationVO importVO;
                try {
                    importVO = objectMapper.readValue(vo, HmeUserOrganizationVO.class);
                } catch (IOException e) {
                    // 失败
                    return false;
                }
                // 校验数据
                checkImportInfo(tenantId, importVO);
                // 处理数据
                setValue(tenantId, importVO);
            }
        }
        return true;
    }

    /**
     * 处理数据
     *
     * @param tenantId
     * @param importVO
     * @return
     */
    private void setValue(Long tenantId, HmeUserOrganizationVO importVO){
        MtUserOrganization mtUserOrganization = new MtUserOrganization();
        // tenantId
        mtUserOrganization.setTenantId(tenantId);
        // userId
        String userId = checkUserCode(tenantId, importVO.getUserCode());
        mtUserOrganization.setUserId(Long.valueOf(userId));
        //organizationId
        String organizationId = checkOrganization(tenantId, importVO.getOrganizationType(), importVO.getOrganizationCode());
        mtUserOrganization.setOrganizationId(organizationId);
        // organizationType
        mtUserOrganization.setOrganizationType(importVO.getOrganizationType());
        // defaultOrganizationFlag
        mtUserOrganization.setDefaultOrganizationFlag(importVO.getDefaultOrganizationFlag());
        // enableFlag
        mtUserOrganization.setEnableFlag(importVO.getEnableFlag());

        MtUserOrganization mtUserOrganizationExist = new MtUserOrganization();
        mtUserOrganizationExist.setTenantId(tenantId);
        mtUserOrganizationExist.setUserId(Long.valueOf(userId));
        mtUserOrganizationExist.setOrganizationType(importVO.getOrganizationType());
        mtUserOrganizationExist.setOrganizationId(organizationId);
        // 更新或插入
        MtUserOrganization mtUserOrganizationOne = mtUserOrganizationRepository.selectOne(mtUserOrganizationExist);
        if (Objects.nonNull(mtUserOrganizationOne)) {
            mtUserOrganization.setUserOrganizationId(mtUserOrganizationOne.getUserOrganizationId());
            mtUserOrganizationMapper.updateByPrimaryKeySelective(mtUserOrganization);
        }else {
            mtUserOrganizationRepository.insertSelective(mtUserOrganization);
        }

        importVO.setUserId(userId);
        importVO.setOrganizationId(organizationId);
        //如果组织类型为库位
        if("LOCATOR".equals(importVO.getOrganizationType())) {
            // 组织类型为库位 单据类型、操作类型、权限分配是否有效全为空 则不记录wms_doc_privilege
            boolean nonHaveValueFlag = StringUtils.isBlank(importVO.getDocType()) && StringUtils.isBlank(importVO.getOperationType()) && StringUtils.isBlank(importVO.getPrivilegeEnableFlag());
            if (!nonHaveValueFlag) {
                WmsDocPrivilege wmsDocPrivilege = new WmsDocPrivilege();
                wmsDocPrivilege.setDocType(importVO.getDocType());
                wmsDocPrivilege.setOperationType(importVO.getOperationType());
                wmsDocPrivilege.setLocationType(StringUtils.isNotBlank(importVO.getLocationType()) ? importVO.getLocationType() : "");
                wmsDocPrivilege.setUserOrganizationId(mtUserOrganization.getUserOrganizationId());
                wmsDocPrivilege.setEnableFlag(importVO.getPrivilegeEnableFlag());

                // 更新或插入
                WmsDocPrivilege wmsDocPrivilegeExist = wmsDocPrivilegeMapper.selectPrivilege(tenantId, importVO);
                if (wmsDocPrivilegeExist != null) {
                    wmsDocPrivilege.setPrivilegeId(wmsDocPrivilegeExist.getPrivilegeId());
                    wmsDocPrivilegeMapper.updateByPrimaryKeySelective(wmsDocPrivilege);
                } else {
                    wmsDocPrivilegeRepository.insertSelective(wmsDocPrivilege);
                }
            }
        }
    }

    private void checkImportInfo(Long tenantId, HmeUserOrganizationVO importVO) {
        String organizationType = importVO.getOrganizationType();
        String userCode = importVO.getUserCode();
        // 员工工号
        String userId = checkUserCode(tenantId, userCode);
        importVO.setUserId(userId);
        // 组织编码
        String organizationCode = importVO.getOrganizationCode();
        String organizationId = checkOrganization(tenantId, organizationType, organizationCode);
        importVO.setOrganizationId(organizationId);
        // 默认组织标识
        // 一个用户同一组织类型只能有一个Y
        checkDefaultOrganizationFlag(tenantId, userId, organizationType, userCode, importVO.getDefaultOrganizationFlag());
        // 是否有效
        if (StringUtils.isBlank(importVO.getEnableFlag())) {
            // 必输字段${1}为空,请检查!
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "是否有效"));
        }
        //权限授权校验
        if("LOCATOR".equals(importVO.getOrganizationType())) {
            //单据类型、操作类型、权限分配是否有效应同时有值或者无值
            boolean haveValueFlag = StringUtils.isNotBlank(importVO.getDocType()) && StringUtils.isNotBlank(importVO.getOperationType()) && StringUtils.isNotBlank(importVO.getPrivilegeEnableFlag());
            boolean nonHaveValueFlag = StringUtils.isBlank(importVO.getDocType()) && StringUtils.isBlank(importVO.getOperationType()) && StringUtils.isBlank(importVO.getPrivilegeEnableFlag());
            if (!haveValueFlag && !nonHaveValueFlag) {
                throw new MtException("WMS_PERMISSION_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PERMISSION_0001", "WMS"));
            }
            // 获取值集
            List<LovValueDTO> typeLovList = lovAdapter.queryLovValue("WMS.USER_DOC_TYPE", tenantId);
            String tag = "";
            for (LovValueDTO type : typeLovList) {
                if (StringUtils.equals(type.getValue(), importVO.getDocType())) {
                    tag = type.getTag();
                }
            }
            if (!StringUtils.isBlank(importVO.getDocType()) && "Y".equalsIgnoreCase(tag)) {
                if (StringUtils.isBlank(importVO.getLocationType())) {
                    throw new MtException("WMS_PERMISSION_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PERMISSION_0003", "WMS"));
                }
            } else {
                if (!StringUtils.isBlank(importVO.getLocationType())) {
                    throw new MtException("WMS_PERMISSION_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "WMS_PERMISSION_0003", "WMS"));
                }
            }
        }else {
            if (StringUtils.isNotBlank(importVO.getDocType()) || StringUtils.isNotBlank(importVO.getLocationType()) || StringUtils.isNotBlank(importVO.getOperationType()) || StringUtils.isNotBlank(importVO.getPrivilegeEnableFlag())) {
                throw new MtException("WMS_PERMISSION_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "WMS_PERMISSION_0002", "WMS"));
            }
        }
    }




    /**
     * 用户${1}组织类型为${2}的组织编码${3}已存在
     *
     * @param tenantId
     * @param userId
     * @param organizationType
     * @param defaultOrganizationFlag
     */
    private void checkDefaultOrganizationFlag(Long tenantId, String userId, String organizationType,  String userCode, String defaultOrganizationFlag) {
//
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
                throw new MtException("MT_USER_ORGANIZATION_001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                        "MT_USER_ORGANIZATION_001", "HME", userCode, organizationType));
            }
        }
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
            throw new MtException("WMS_QR_CODE_0003", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "WMS_QR_CODE_0003", "WMS", "组织编码"));
        }
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
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "组织编码", organizationId));
        }
        return organizationId;
    }


    /**
     * mt_mod_workcell
     *
     * @param tenantId
     * @param workcellCode
     * @return
     */
    private String checkMtModWorkcell(Long tenantId, String workcellCode) {
        MtModWorkcell mtModWorkcell = new MtModWorkcell();
        mtModWorkcell.setTenantId(tenantId);
        mtModWorkcell.setWorkcellCode(workcellCode);
        mtModWorkcell = mtModWorkcellRepository.selectOne(mtModWorkcell);
        if (Objects.isNull(mtModWorkcell)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "生产线", workcellCode));
        }
        return mtModWorkcell.getWorkcellId();
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
     * mt_mod_production_line
     *
     * @param tenantId
     * @param prodLineCode
     * @return
     */
    private String checkMtModProdLine(Long tenantId, String prodLineCode) {
        MtModProductionLine mtModProductionLine = new MtModProductionLine();
        mtModProductionLine.setTenantId(tenantId);
        mtModProductionLine.setProdLineCode(prodLineCode);
        mtModProductionLine = mtModProductionLineRepository.selectOne(mtModProductionLine);
        if (Objects.isNull(mtModProductionLine)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "生产线", prodLineCode));
        }
        return mtModProductionLine.getProdLineId();
    }

    /**
     * mt_mod_area
     *
     * @param tenantId
     * @param areaCode
     * @return
     */
    private String checkMtModArea(Long tenantId, String areaCode) {
        MtModArea mtModArea = new MtModArea();
        mtModArea.setTenantId(tenantId);
        mtModArea.setAreaCode(areaCode);
        mtModArea = mtModAreaRepository.selectOne(mtModArea);
        if (Objects.isNull(mtModArea)) {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "区域", areaCode));
        }
        return mtModArea.getAreaId();
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
        ResponseEntity<HmeHzeroIamUserDTO> userInfo = hmeHzeroIamFeignClient.getUserInfo(tenantId, userCode, "P");
        if (userInfo.getBody().getId() != null) {
            userId = userInfo.getBody().getId();
        } else {
            // MT_ASSEMBLE_0004 --> ${1}不存在.${2}
            throw new MtException("MT_ASSEMBLE_0004", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ASSEMBLE_0004", "ASSEMBLE", "员工工号", userCode));
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
