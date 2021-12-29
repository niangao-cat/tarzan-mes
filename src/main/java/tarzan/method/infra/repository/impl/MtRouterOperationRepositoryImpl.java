package tarzan.method.infra.repository.impl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hzero.mybatis.base.impl.BaseRepositoryImpl;
import org.hzero.mybatis.helper.SecurityTokenHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.tarzan.common.domain.repository.MtErrorMessageRepository;
import io.tarzan.common.domain.sys.MtException;
import tarzan.method.domain.entity.MtRouterOperation;
import tarzan.method.domain.repository.MtRouterOperationRepository;
import tarzan.method.infra.mapper.MtRouterOperationMapper;

/**
 * 工艺路线步骤对应工序 资源库实现
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
@Component
public class MtRouterOperationRepositoryImpl extends BaseRepositoryImpl<MtRouterOperation>
                implements MtRouterOperationRepository {

    @Autowired
    private MtErrorMessageRepository mtErrorMessageRepository;

    @Autowired
    private MtRouterOperationMapper mtRouterOperationMapper;

    @Override
    public MtRouterOperation routerOperationGet(Long tenantId, String routerStepId) {
        if (StringUtils.isEmpty(routerStepId)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerOperationGet】"));
        }

        MtRouterOperation mtRouterOperation = new MtRouterOperation();
        mtRouterOperation.setTenantId(tenantId);
        mtRouterOperation.setRouterStepId(routerStepId);
        mtRouterOperation = this.mtRouterOperationMapper.selectOne(mtRouterOperation);
        if (mtRouterOperation == null) {
            throw new MtException("MT_ROUTER_0002", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0002", "ROUTER", "【API:routerOperationGet】"));
        }
        return mtRouterOperation;
    }

    @Override
    public List<MtRouterOperation> routerOperationBatchGet(Long tenantId, List<String> routerStepIds) {
        if (CollectionUtils.isEmpty(routerStepIds)) {
            throw new MtException("MT_ROUTER_0001", mtErrorMessageRepository.getErrorMessageWithModule(tenantId,
                            "MT_ROUTER_0001", "ROUTER", "routerStepId", "【API:routerOperationBatchGet】"));
        }

        SecurityTokenHelper.close();
        return this.mtRouterOperationMapper.selectRouterOperationByIds(tenantId, routerStepIds);
    }
}
