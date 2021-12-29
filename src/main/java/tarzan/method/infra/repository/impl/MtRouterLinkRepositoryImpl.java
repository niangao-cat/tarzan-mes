package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterLink;
import tarzan.method.domain.repository.MtRouterLinkRepository;
import tarzan.method.infra.mapper.MtRouterLinkMapper;

/**
 * 嵌套工艺路线步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterLinkRepositoryImpl extends BaseRepositoryImpl<MtRouterLink> implements MtRouterLinkRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepo;

    @Autowired
    private MtRouterLinkMapper mtRouterLinkMapper;

    @Override
    public MtRouterLink routerLinkGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerLinkGet】"));
        }

        MtRouterLink mtRouterLink = new MtRouterLink();
        mtRouterLink.setTenantId(tenantId);
        mtRouterLink.setRouterStepId(routerStepId);
        mtRouterLink = this.mtRouterLinkMapper.selectOne(mtRouterLink);
        if (mtRouterLink == null) {
            throw new MtException("MT_ROUTER_0004", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0004", "ROUTER", "【API:routerLinkGet】"));
        }
        return mtRouterLink;
    }

    @Override
    public List<MtRouterLink> routerLinkBatchGet(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepo.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerLinkBatchGet】"));
        }
        return this.mtRouterLinkMapper.selectRouterLinkByIds(tenantId, routerStepIds);
    }
}
