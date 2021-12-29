package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterOperation;

/**
 * 工艺路线步骤对应工序资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterOperationRepository
                extends BaseRepository<MtRouterOperation>, AopProxy<MtRouterOperationRepository> {

    /**
     * routerOperationGet-获取工艺类型步骤的相关属性
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterOperation routerOperationGet(Long tenantId, String routerStepId);

    /**
     * routerOperationBatchGet-批量获取工艺类型步骤的相关属性
     *
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterOperation> routerOperationBatchGet(Long tenantId, List<String> routerStepIds);
}
