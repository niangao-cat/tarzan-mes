package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterReturnStep;
import tarzan.method.domain.repository.MtRouterReturnStepRepository;
import tarzan.method.infra.mapper.MtRouterReturnStepMapper;

/**
 * 返回步骤 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:57
 */
@Component
public class MtRouterReturnStepRepositoryImpl extends BaseRepositoryImpl<MtRouterReturnStep>
        implements MtRouterReturnStepRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtRouterReturnStepMapper mtRouterReturnStepMapper;

    @Override
    public MtRouterReturnStep routerReturnStepGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerReturnStepGet】"));
        }

        MtRouterReturnStep mtRouterReturnStep = new MtRouterReturnStep();
        mtRouterReturnStep.setTenantId(tenantId);
        mtRouterReturnStep.setRouterStepId(routerStepId);
        return this.mtRouterReturnStepMapper.selectOne(mtRouterReturnStep);
    }

    @Override
    public List<MtRouterReturnStep> routerReturnStepBatchGet(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerReturnStepBatchGet】"));
        }
        return this.mtRouterReturnStepMapper.selectRouterReturnStepByIds(tenantId, routerStepIds);
    }

    @Override
    public String returnStepValidate(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                    "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:returnStepValidate】"));
        }

        MtRouterReturnStep mtRouterReturnStep = new MtRouterReturnStep();
        mtRouterReturnStep.setTenantId(tenantId);
        mtRouterReturnStep.setRouterStepId(routerStepId);
        mtRouterReturnStep = this.mtRouterReturnStepMapper.selectOne(mtRouterReturnStep);
        if (mtRouterReturnStep == null) {
            return "N";
        }
        return "Y";
    }
}
