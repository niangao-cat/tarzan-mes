package tarzan.method.infra.repository.impl;

import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtNcCode;
import tarzan.method.domain.entity.MtNcGroup;
import tarzan.method.domain.repository.MtNcCodeRepository;
import tarzan.method.domain.repository.MtNcGroupRepository;
import tarzan.method.domain.vo.MtNcCodeVO;
import tarzan.method.domain.vo.MtNcCodeVO1;
import tarzan.method.infra.mapper.MtNcCodeMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 不良代码数据 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:18:39
 */
@Component
public class MtNcCodeRepositoryImpl extends BaseRepositoryImpl<MtNcCode> implements MtNcCodeRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtNcGroupRepository mtNcGroupRepo;

    @Autowired
    private MtNcCodeMapper mtNcCodeMapper;

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;



    @Override
    public String ncCodeLimitNcGroupGet(Long tenantId, String ncCodeId) {
        // Step 1 数据校验
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeLimitNcGroupGet】"));

        }
        MtNcCode mtNcCode = new MtNcCode();
        mtNcCode.setNcCodeId(ncCodeId);
        mtNcCode.setTenantId(tenantId);
        mtNcCode = mtNcCodeMapper.selectOne(mtNcCode);
        if (mtNcCode == null) {
            return null;
        }
        return mtNcCode.getNcGroupId();
    }

    @Override
    public List<MtNcCode> propertyLimitNcCodeQuery(Long tenantId, MtNcCode mtNcCode) {
        return mtNcCodeMapper.select(mtNcCode);
    }

    @Override
    public MtNcCode ncCodePropertyGet(Long tenantId, String ncCodeId) {
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodePropertyGet】"));
        }
        MtNcCode mtNcCode = new MtNcCode();
        mtNcCode.setNcCodeId(ncCodeId);
        mtNcCode.setTenantId(tenantId);
        return mtNcCodeMapper.selectOne(mtNcCode);
    }

    @Override
    public MtNcCode ncCodeGet(Long tenantId, String ncCodeId) {
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeGet】"));
        }
        // 获取不良代码属性 p1
        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);
        // 只需要校验数据库中字段可以为空的就可以了
        if (mtNcCode == null || StringUtils.isEmpty(mtNcCode.getNcGroupId()) || mtNcCode.getPriority() == null
                        || StringUtils.isEmpty(mtNcCode.getClosureRequired())
                        || StringUtils.isEmpty(mtNcCode.getConfirmRequired())
                        || StringUtils.isEmpty(mtNcCode.getAutoCloseIncident())
                        || StringUtils.isEmpty(mtNcCode.getAutoClosePrimary())
                        || StringUtils.isEmpty(mtNcCode.getCanBePrimaryCode())
                        || StringUtils.isEmpty(mtNcCode.getValidAtAllOperations())
                        || StringUtils.isEmpty(mtNcCode.getAllowNoDisposition())
                        || StringUtils.isEmpty(mtNcCode.getComponentRequired())
                        || StringUtils.isEmpty(mtNcCode.getDispositionGroupId()) || mtNcCode.getMaxNcLimit() == null
                        || StringUtils.isEmpty(mtNcCode.getSecondaryCodeSpInstr())
                        || StringUtils.isEmpty(mtNcCode.getSecondaryReqdForClose())
                        || StringUtils.isEmpty(mtNcCode.getDescription())) {
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (ncGroupId == null) {
                return mtNcCode;
            }
            MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
            if (mtNcGroup == null) {
                return mtNcCode;
            }
            // 将mtNcCode为空的用mtNcGroup替换
            if (StringUtils.isEmpty(mtNcCode.getNcGroupId())) {
                mtNcCode.setNcGroupId(mtNcGroup.getNcGroupId());
            }
            if (mtNcCode.getPriority() == null) {
                mtNcCode.setPriority(mtNcGroup.getPriority());
            }
            if (StringUtils.isEmpty(mtNcCode.getClosureRequired())) {
                mtNcCode.setClosureRequired(mtNcGroup.getClosureRequired());
            }
            if (StringUtils.isEmpty(mtNcCode.getConfirmRequired())) {
                mtNcCode.setConfirmRequired(mtNcGroup.getConfirmRequired());
            }
            if (StringUtils.isEmpty(mtNcCode.getAutoCloseIncident())) {
                mtNcCode.setAutoCloseIncident(mtNcGroup.getAutoCloseIncident());
            }
            if (StringUtils.isEmpty(mtNcCode.getAutoClosePrimary())) {
                mtNcCode.setAutoClosePrimary(mtNcGroup.getAutoClosePrimary());
            }
            if (StringUtils.isEmpty(mtNcCode.getCanBePrimaryCode())) {
                mtNcCode.setCanBePrimaryCode(mtNcGroup.getCanBePrimaryCode());
            }
            if (StringUtils.isEmpty(mtNcCode.getValidAtAllOperations())) {
                mtNcCode.setValidAtAllOperations(mtNcGroup.getValidAtAllOperations());
            }
            if (StringUtils.isEmpty(mtNcCode.getAllowNoDisposition())) {
                mtNcCode.setAllowNoDisposition(mtNcGroup.getAllowNoDisposition());
            }
            if (StringUtils.isEmpty(mtNcCode.getComponentRequired())) {
                mtNcCode.setComponentRequired(mtNcGroup.getComponentRequired());
            }
            if (StringUtils.isEmpty(mtNcCode.getDispositionGroupId())) {
                mtNcCode.setDispositionGroupId(mtNcGroup.getDispositionGroupId());
            }
            if (StringUtils.isEmpty(mtNcCode.getSecondaryCodeSpInstr())) {
                mtNcCode.setSecondaryCodeSpInstr(mtNcGroup.getSecondaryCodeSpInstr());
            }
            if (StringUtils.isEmpty(mtNcCode.getSecondaryReqdForClose())) {
                mtNcCode.setSecondaryReqdForClose(mtNcGroup.getSecondaryReqdForClose());
            }
            if (StringUtils.isEmpty(mtNcCode.getDescription())) {
                mtNcCode.setDescription(mtNcGroup.getDescription());
            }
            if (mtNcCode.getMaxNcLimit() == null) {
                mtNcCode.setMaxNcLimit(mtNcGroup.getMaxNcLimit());
            }
        }
        return mtNcCode;
    }

    @Override
    public List<MtNcCode> ncCodeGroupMemberQuery(Long tenantId, String ncGroupId) {
        if (StringUtils.isEmpty(ncGroupId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncGroupId", "【API:ncCodeGroupMemberQuery】"));

        }
        MtNcCode mtNcCode = new MtNcCode();
        mtNcCode.setNcGroupId(ncGroupId);
        mtNcCode.setTenantId(tenantId);
        return mtNcCodeMapper.select(mtNcCode);
    }

    @Override
    public String ncCodeEnableValidate(Long tenantId, String ncCodeId) {
        // 1. 验证参数有效性
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeEnableValidate】"));
        }

        String result = "";

        MtNcCode mtNcCode = new MtNcCode();
        mtNcCode.setTenantId(tenantId);
        mtNcCode.setNcCodeId(ncCodeId);
        mtNcCode = mtNcCodeMapper.selectOne(mtNcCode);
        if (mtNcCode != null) {
            result = mtNcCode.getEnableFlag();
        }
        return result;
    }

    @Override
    public String ncCodeConfirmRequiredValidate(Long tenantId, String ncCodeId) {
        // 1. 参数有效性验证
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeConfirmRequiredValidate】"));
        }

        String result = "";

        // 1. 获取属性 MtNcCode
        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);
        if (mtNcCode != null && StringUtils.isNotEmpty(mtNcCode.getConfirmRequired())) {
            result = mtNcCode.getConfirmRequired();
        } else {
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isNotEmpty(ncGroupId)) {
                MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
                if (mtNcGroup != null) {
                    result = mtNcGroup.getConfirmRequired();
                }
            }
        }

        return result;
    }

    @Override
    public String ncCodeIncidentAutoCloseValidate(Long tenantId, String ncCodeId) {
        // 1. 参数有效性验证
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeIncidentAutoCloseValidate】"));
        }

        String result = "";

        // 1. 获取属性 MtNcCode
        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);
        if (mtNcCode != null && StringUtils.isNotEmpty(mtNcCode.getAutoCloseIncident())) {
            result = mtNcCode.getAutoCloseIncident();
        } else {
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isNotEmpty(ncGroupId)) {
                MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
                if (mtNcGroup != null) {
                    result = mtNcGroup.getAutoCloseIncident();
                }
            }
        }

        return result;
    }

    @Override
    public String ncCodePrimaryCodeAutoCloseValidate(Long tenantId, String ncCodeId) {
        // 1. 参数有效性验证
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodePrimaryCodeAutoCloseValidate】"));
        }

        String result = "";

        // 1. 获取属性 MtNcCode
        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);
        if (mtNcCode != null && StringUtils.isNotEmpty(mtNcCode.getAutoClosePrimary())) {
            result = mtNcCode.getAutoClosePrimary();
        } else {
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isNotEmpty(ncGroupId)) {
                MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
                if (mtNcGroup != null) {
                    result = mtNcGroup.getAutoClosePrimary();
                }
            }
        }

        return result;
    }

    @Override
    public String ncCodeSecondaryCodeRequiredValidate(Long tenantId, String ncCodeId) {
        // 1. 参数有效性验证
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeSecondaryCodeRequiredValidate】"));
        }

        String result = "";

        // 1. 获取属性 MtNcCode
        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);
        if (mtNcCode != null && StringUtils.isNotEmpty(mtNcCode.getSecondaryReqdForClose())) {
            result = mtNcCode.getSecondaryReqdForClose();
        } else {
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isNotEmpty(ncGroupId)) {
                MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
                if (mtNcGroup != null) {
                    result = mtNcGroup.getSecondaryReqdForClose();
                }
            }
        }

        return result;
    }

    @Override
    public String ncCodeValidAtAllOperationValidate(Long tenantId, String ncCodeId) {
        // 1. 参数有效性验证
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeValidAtAllOperationValidate】"));
        }

        String result = "";

        // 1. 获取属性 MtNcCode
        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);
        if (mtNcCode != null && StringUtils.isNotEmpty(mtNcCode.getValidAtAllOperations())) {
            result = mtNcCode.getValidAtAllOperations();
        } else {
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isNotEmpty(ncGroupId)) {
                MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
                if (mtNcGroup != null) {
                    result = mtNcGroup.getValidAtAllOperations();
                }
            }
        }

        return result;
    }

    @Override
    public List<MtNcCodeVO1> propertyLimitNcCodePropertyQuery(Long tenantId, MtNcCodeVO dto) {
        List<MtNcCodeVO1> voList = mtNcCodeMapper.selectCondition(tenantId, dto);
        if (CollectionUtils.isEmpty(voList)) {
            return Collections.emptyList();
        }

        // 依据主表信息来
        List<String> ncGroupList = voList.stream().map(MtNcCodeVO1::getNcGroupId).filter(StringUtils::isNotEmpty)
                        .distinct().collect(Collectors.toList());
        List<String> siteIdList = voList.stream().map(MtNcCodeVO1::getSiteId).filter(StringUtils::isNotEmpty).distinct()
                        .collect(Collectors.toList());
        Map<String, MtNcGroup> mtNcGroupMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(ncGroupList)) {
            List<MtNcGroup> mtNcGroups = mtNcGroupRepo.ncGroupPropertyBatchGet(tenantId, ncGroupList);
            if (CollectionUtils.isNotEmpty(mtNcGroups)) {
                mtNcGroupMap = mtNcGroups.stream().collect(Collectors.toMap(MtNcGroup::getNcGroupId, t -> t));
            }
        }

        Map<String, MtModSite> mtModSiteMap = new HashMap<>(0);
        if (CollectionUtils.isNotEmpty(siteIdList)) {
            List<MtModSite> mtModSites = mtModSiteRepository.siteBasicPropertyBatchGet(tenantId, siteIdList);
            if (CollectionUtils.isNotEmpty(mtModSites)) {
                mtModSiteMap = mtModSites.stream().collect(Collectors.toMap(MtModSite::getSiteId, t -> t));
            }
        }

        for (MtNcCodeVO1 codeVO1 : voList) {
            codeVO1.setSiteCode(null == mtModSiteMap.get(codeVO1.getSiteId()) ? null
                            : mtModSiteMap.get(codeVO1.getSiteId()).getSiteCode());
            codeVO1.setSiteName(null == mtModSiteMap.get(codeVO1.getSiteId()) ? null
                            : mtModSiteMap.get(codeVO1.getSiteId()).getSiteName());
            codeVO1.setNcGroupCode(null == mtNcGroupMap.get(codeVO1.getNcGroupId()) ? null
                            : mtNcGroupMap.get(codeVO1.getNcGroupId()).getNcGroupCode());
            codeVO1.setDescription(null == mtNcGroupMap.get(codeVO1.getNcGroupId()) ? null
                            : mtNcGroupMap.get(codeVO1.getNcGroupId()).getDescription());

        }
        return voList;
    }

    @Override
    public String ncCodeClosedRequiredValidate(Long tenantId, String ncCodeId) {

        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeClosedRequiredValidate】"));
        }
        // 1获取属性
        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);

        if (mtNcCode == null || StringUtils.isEmpty(mtNcCode.getClosureRequired())) {
            // p2
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isEmpty(ncGroupId)) {
                return "";
            }
            MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
            if (mtNcGroup == null) {
                return "";
            }
            return mtNcGroup.getClosureRequired();
        }

        return mtNcCode.getClosureRequired();
    }

    @Override
    public String ncCodeComponentRequiredValidate(Long tenantId, String ncCodeId) {

        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeComponentRequiredValidate】"));
        }

        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);

        if (mtNcCode == null || StringUtils.isEmpty(mtNcCode.getComponentRequired())) {
            // p2
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isEmpty(ncGroupId)) {
                return "";
            }
            MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
            if (mtNcGroup == null) {
                return "";
            }
            return mtNcGroup.getComponentRequired();
        }

        return mtNcCode.getComponentRequired();
    }

    @Override
    public String ncCodeAllowNoDispositionValidate(Long tenantId, String ncCodeId) {
        if (StringUtils.isEmpty(ncCodeId)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeAllowNoDispositionValidate】"));
        }

        MtNcCode mtNcCode = self().ncCodePropertyGet(tenantId, ncCodeId);

        if (mtNcCode == null || StringUtils.isEmpty(mtNcCode.getAllowNoDisposition())) {
            // p2
            String ncGroupId = self().ncCodeLimitNcGroupGet(tenantId, ncCodeId);
            if (StringUtils.isEmpty(ncGroupId)) {
                return "";
            }
            MtNcGroup mtNcGroup = mtNcGroupRepo.ncGroupPropertyGet(tenantId, ncGroupId);
            if (mtNcGroup == null) {
                return "";
            }
            return mtNcGroup.getAllowNoDisposition();
        }
        return mtNcCode.getAllowNoDisposition();

    }

    @Override
    public List<MtNcCode> ncCodeByMcCodeQuery(Long tenantId, List<String> mtNcCode) {
        if (CollectionUtils.isEmpty(mtNcCode)) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "ncCodeId", "【API:ncCodeByMcCodeQuery】"));
        }
        return mtNcCodeMapper.selectByNcCode(tenantId, mtNcCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void ncCodeAttrPropertyUpdate(Long tenantId, MtExtendVO10 dto) {
        // 1.参数校验
        if (dto == null || StringUtils.isEmpty(dto.getKeyId())) {
            throw new MtException("MT_NC_CODE_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_NC_CODE_0001", "NC_CODE", "keyId", "【API:ncCodeAttrPropertyUpdate】"));
        }

        // 2.校验参数是否存在
        MtNcCode ncCode = new MtNcCode();
        ncCode.setTenantId(tenantId);
        ncCode.setNcCodeId(dto.getKeyId());
        ncCode = mtNcCodeMapper.selectOne(ncCode);
        if (ncCode == null) {
            throw new MtException("MT_NC_CODE_0007",
                            mtErrorMessageRepo.getErrorMessageWithModule(tenantId, "MT_NC_CODE_0007", "NC_CODE",
                                            dto.getKeyId(), "mt_nc_code", "【API:ncCodeAttrPropertyUpdate】"));
        }

        // 3.调用API{attrPropertyUpdate}
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_nc_code_attr", dto.getKeyId(), dto.getEventId(),
                        dto.getAttrs());
    }

    @Override
    public List<MtNcCode> ncCodeBatchGet(Long tenantId, List<String> ncCodeIds) {
        if (CollectionUtils.isEmpty(ncCodeIds)) {
            return new ArrayList<>();
        }
        SecurityTokenHelper.close();
        return mtNcCodeMapper.selectByCondition(Condition.builder(MtNcCode.class)
                        .andWhere(Sqls.custom().andEqualTo(MtNcCode.FIELD_TENANT_ID, tenantId)
                                        .andIn(MtNcCode.FIELD_NC_CODE_ID, ncCodeIds, true))
                        .build());
    }
}
