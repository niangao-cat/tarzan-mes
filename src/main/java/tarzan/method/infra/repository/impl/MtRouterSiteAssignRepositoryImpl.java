package tarzan.method.infra.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.common.Criteria;
import org.hzero.mybatis.common.query.Comparison;
import org.hzero.mybatis.common.query.WhereField;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.repository.MtExtendSettingsRepository;
import io.tarzan.common.domain.sys.MtException;
import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtRouterSiteAssign;
import tarzan.method.domain.repository.MtRouterSiteAssignRepository;
import tarzan.method.infra.mapper.MtRouterSiteAssignMapper;

/**
 * 工艺路线站点分配表 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@Component
public class MtRouterSiteAssignRepositoryImpl extends BaseRepositoryImpl<MtRouterSiteAssign>
                implements MtRouterSiteAssignRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageService;

    @Autowired
    private MtRouterSiteAssignMapper mtRouterSiteAssignMapper;

    @Autowired
    private MtExtendSettingsRepository mtExtendSettingsRepository;

    @Override
    public List<MtRouterSiteAssign> propertyLimitRouterSiteAssignQuery(Long tenantId, MtRouterSiteAssign dto) {
        if (StringUtils.isEmpty(dto.getRouterSiteAssignId()) && StringUtils.isEmpty(dto.getRouterId())
                        && StringUtils.isEmpty(dto.getSiteId()) && StringUtils.isEmpty(dto.getEnableFlag())) {
            throw new MtException("MT_ROUTER_0001",
                            mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_ROUTER_0001", "ROUTER",
                                            "routerSiteAssignId/routerId/siteId/enableFlag",
                                            "【API:propertyLimitRouterSiteAssignQuery】"));
        }

        MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
        BeanUtils.copyProperties(dto, mtRouterSiteAssign);

        Criteria criteria = new Criteria(mtRouterSiteAssign);
        List<WhereField> whereFields = new ArrayList<WhereField>();
        if (StringUtils.isNotEmpty(dto.getRouterSiteAssignId())) {
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_ROUTER_SITE_ASSIGN_ID, Comparison.EQUAL));
        }
        if (StringUtils.isNotEmpty(dto.getRouterId())) {
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_ROUTER_ID, Comparison.EQUAL));
        }
        if (StringUtils.isNotEmpty(dto.getSiteId())) {
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_SITE_ID, Comparison.EQUAL));
        }
        if (StringUtils.isNotEmpty(dto.getEnableFlag())) {
            whereFields.add(new WhereField(MtRouterSiteAssign.FIELD_ENABLE_FLAG, Comparison.EQUAL));
        }
        criteria.where(whereFields.toArray(new WhereField[whereFields.size()]));
        List<MtRouterSiteAssign> mtRouterSiteAssigns =
                        this.mtRouterSiteAssignMapper.selectOptional(mtRouterSiteAssign, criteria);
        if (CollectionUtils.isEmpty(mtRouterSiteAssigns)) {
            return Collections.emptyList();
        }

        return mtRouterSiteAssigns;
    }

    @Override
    public List<MtRouterSiteAssign> routerLimitRouterSiteAssignBatchQuery(Long tenantId, List<String> routerIdList) {
        if (CollectionUtils.isEmpty(routerIdList)) {
            return Collections.emptyList();
        }

        return mtRouterSiteAssignMapper.queryRouterSiteAssignByRouterId(tenantId, routerIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void routerSiteAssignAttrPropertyUpdate(Long tenantId, MtExtendVO10 vo) {

        if (StringUtils.isEmpty(vo.getKeyId())) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageService.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "keyId", "【API:routerSiteAssignAttrPropertyUpdate】"));
        }
        MtRouterSiteAssign mtRouterSiteAssign = new MtRouterSiteAssign();
        mtRouterSiteAssign.setTenantId(tenantId);
        mtRouterSiteAssign.setRouterSiteAssignId(vo.getKeyId());
        mtRouterSiteAssign = mtRouterSiteAssignMapper.selectOne(mtRouterSiteAssign);
        if (null == mtRouterSiteAssign) {
            throw new MtException("MT_ROUTER_0071",
                    mtErrorMessageService.getErrorMessageWithModule(tenantId, "MT_ROUTER_0071", "ROUTER",
                            "keyId:" + vo.getKeyId(), "mt_router_site_assign",
                            "【API:routerSiteAssignAttrPropertyUpdate】"));
        }
        mtExtendSettingsRepository.attrPropertyUpdate(tenantId, "mt_router_site_assign_attr", vo.getKeyId(),
                vo.getEventId(), vo.getAttrs());
    }
}
