package tarzan.method.infra.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.domian.Condition;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.hzero.mybatis.util.Sqls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtBom;
import tarzan.method.domain.entity.MtBomSiteAssign;
import tarzan.method.domain.repository.MtBomSiteAssignRepository;
import tarzan.method.domain.vo.MtBomSiteAssignVO2;
import tarzan.method.infra.mapper.MtBomMapper;
import tarzan.method.infra.mapper.MtBomSiteAssignMapper;
import tarzan.modeling.domain.entity.MtModSite;
import tarzan.modeling.domain.repository.MtModSiteRepository;

/**
 * 装配清单站点分配 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
@Component
public class MtBomSiteAssignRepositoryImpl extends BaseRepositoryImpl<MtBomSiteAssign>
                implements MtBomSiteAssignRepository {

    @Autowired
    private MtModSiteRepository mtModSiteRepository;

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtBomSiteAssignMapper mtBomSiteAssignMapper;

    @Autowired
    private MtBomMapper mtBomMapper;

    @Override
    public List<String> bomLimitEnableSiteQuery(Long tenantId, String bomId) {
        if (StringUtils.isEmpty(bomId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomId", "【API:bomLimitEnableSiteQuery】"));
        }

        MtBomSiteAssign mtBomSiteAssign = new MtBomSiteAssign();
        mtBomSiteAssign.setTenantId(tenantId);
        mtBomSiteAssign.setBomId(bomId);
        mtBomSiteAssign.setEnableFlag("Y");
        List<MtBomSiteAssign> result = mtBomSiteAssignMapper.select(mtBomSiteAssign);
        if (CollectionUtils.isEmpty(result)) {
            return Collections.emptyList();
        }
        return result.stream().map(MtBomSiteAssign::getSiteId).collect(Collectors.toList());
    }

    @Override
    public List<MtBomSiteAssign> bomLimitBomSiteAssignBatchQuery(Long tenantId, List<String> bomIdList) {
        if (CollectionUtils.isEmpty(bomIdList)) {
            return null;
        }
        return mtBomSiteAssignMapper.queryBomSiteAssignByRouterId(tenantId, bomIdList);
    }

    @Override
    public List<String> siteLimitBomQuery(Long tenantId, String siteId) {
        if (StringUtils.isEmpty(siteId)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "siteId", "【API:siteLimitBomQuery】"));
        }

        MtBomSiteAssign mtBomSiteAssign = new MtBomSiteAssign();
        mtBomSiteAssign.setTenantId(tenantId);
        mtBomSiteAssign.setSiteId(siteId);
        mtBomSiteAssign.setEnableFlag("Y");
        List<MtBomSiteAssign> mtBomSiteAssigns = mtBomSiteAssignMapper.select(mtBomSiteAssign);
        if (CollectionUtils.isEmpty(mtBomSiteAssigns)) {
            return Collections.emptyList();
        }
        return mtBomSiteAssigns.stream().map(MtBomSiteAssign::getBomId).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void bomSiteAssign(Long tenantId, MtBomSiteAssign dto) {
        if (StringUtils.isEmpty(dto.getSiteId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "siteId", "【API:bomSiteAssign】"));
        }
        if (StringUtils.isEmpty(dto.getBomId())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomId", "【API:bomSiteAssign】"));
        }
        if (StringUtils.isEmpty(dto.getEnableFlag())) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "enableFlag", "【API:bomSiteAssign】"));
        }

        MtBom bom = new MtBom();
        bom.setTenantId(tenantId);
        bom.setBomId(dto.getBomId());
        bom = mtBomMapper.selectOne(bom);
        if (bom == null) {
            throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0056", "BOM", "bomId", "【API:bomSiteAssign】"));
        }

        MtModSite site = mtModSiteRepository.siteBasicPropertyGet(tenantId, dto.getSiteId());
        if (site == null || !site.getEnableFlag().equals("Y")) {
            throw new MtException("MT_BOM_0054", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0054", "BOM", "siteId", "【API:bomSiteAssign】"));
        }
        if (!dto.getEnableFlag().equals("Y") && !dto.getEnableFlag().equals("N")) {
            throw new MtException("MT_BOM_0055", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0055", "BOM", "enableFLag", "【API:bomSiteAssign】"));
        }

        MtBomSiteAssign tmp = new MtBomSiteAssign();
        tmp.setTenantId(tenantId);
        tmp.setBomId(dto.getBomId());
        tmp.setSiteId(dto.getSiteId());
        tmp = mtBomSiteAssignMapper.selectOne(tmp);
        if (tmp != null) {
            if (StringUtils.isEmpty(dto.getAssignId()) || !tmp.getAssignId().equals(dto.getAssignId())) {
                throw new MtException("MT_BOM_0057", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0057", "BOM", "【API:bomSiteAssign】"));
            }
        }

        if (!StringUtils.isEmpty(dto.getAssignId())) {
            tmp = new MtBomSiteAssign();
            tmp.setTenantId(tenantId);
            tmp.setAssignId(dto.getAssignId());
            tmp = mtBomSiteAssignMapper.selectOne(tmp);
            if (tmp == null) {
                throw new MtException("MT_BOM_0056", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                                "MT_BOM_0056", "BOM", "assignId", "【API:bomSiteAssign】"));
            }
            // 更新
            tmp.setTenantId(tenantId);
            tmp.setEnableFlag(dto.getEnableFlag());
            tmp.setBomId(dto.getBomId());
            tmp.setSiteId(dto.getSiteId());
            self().updateByPrimaryKeySelective(tmp);
        } else {
            // 新增
            tmp = new MtBomSiteAssign();
            tmp.setTenantId(tenantId);
            tmp.setBomId(dto.getBomId());
            tmp.setSiteId(dto.getSiteId());
            tmp.setEnableFlag(dto.getEnableFlag());
            self().insertSelective(tmp);
        }
    }


    @Override
    public List<MtBomSiteAssignVO2> bomListLimitSiteQuery(Long tenantId, List<String> bomIdList, String enableFlag) {
        if (CollectionUtils.isEmpty(bomIdList)) {
            throw new MtException("MT_BOM_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_BOM_0001", "BOM", "bomIds", "【API:bomListLimitSiteQuery】"));
        }
        SecurityTokenHelper.close();
        List<MtBomSiteAssign> assignList = self().selectByCondition(Condition.builder(MtBomSiteAssign.class)
                        .andWhere(Sqls.custom().andEqualTo(MtBomSiteAssign.FIELD_TENANT_ID, tenantId)
                                        .andEqualTo(MtBomSiteAssign.FIELD_ENABLE_FLAG, enableFlag, true))
                        .andWhere(Sqls.custom().andIn(MtBomSiteAssign.FIELD_BOM_ID, bomIdList)).build());

        if (CollectionUtils.isEmpty(assignList)) {
            return Lists.newArrayList();
        }

        return assignList.stream().map(
                        t -> new MtBomSiteAssignVO2(t.getAssignId(), t.getBomId(), t.getSiteId(), t.getEnableFlag()))
                        .collect(Collectors.toList());
    }
}
