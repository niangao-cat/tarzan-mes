package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtRouterDoneStep;
import tarzan.method.domain.vo.MtRouterDoneStepVO;

/**
 * 完成步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterDoneStepRepository
                extends BaseRepository<MtRouterDoneStep>, AopProxy<MtRouterDoneStepRepository> {

    /**
     * routerDoneStepGet-获取完成步骤的基础信息
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterDoneStep routerDoneStepGet(Long tenantId, String routerStepId);

    /**
     * doneStepVerify-校验步骤是否为完成步骤
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    String doneStepValidate(Long tenantId, String routerStepId);

    /**
     * routerDoneStepBatchGet-批量获取完成步骤的基础信息
     *
     * @param tenantId
     * @param routerStepIds
     */
    List<MtRouterDoneStep> routerDoneStepBatchGet(Long tenantId, List<String> routerStepIds);

    /**
     * routerLimitDoneStepQuery-获取工艺路线完成步骤
     *
     * @author chuang.yang
     * @date 2019/9/25
     * @param tenantId
     * @param routerId
     * @return java.util.List<java.lang.String>
     */
    List<String> routerLimitDoneStepQuery(Long tenantId, String routerId);

    /**
     * eoLimitDoneStepQuery-根据执行作业获取工艺路线完成步骤
     *
     * @author chuang.yang
     * @date 2019/9/25
     * @param tenantId
     * @param eoId
     * @return java.util.List<java.lang.String>
     */
    List<String> eoLimitDoneStepQuery(Long tenantId, String eoId);

    /**
     * doneStepBatchValidate-批量校验步骤是否为完成步骤
     *
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterDoneStepVO> doneStepBatchValidate(Long tenantId, List<String> routerStepIds);
}
