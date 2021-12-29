package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import io.tarzan.common.domain.vo.MtExtendVO10;
import tarzan.method.domain.entity.MtRouterStep;
import tarzan.method.domain.vo.*;

/**
 * 工艺路线步骤资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:22:58
 */
public interface MtRouterStepRepository extends BaseRepository<MtRouterStep>, AopProxy<MtRouterStepRepository> {

    /**
     * routerStepGet-获取工艺路线步骤基础属性
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterStep routerStepGet(Long tenantId, String routerStepId);

    /**
     * routerStepBatchGet-批量获取工艺路线步骤基础属性
     *
     * @param tenantId
     * @param routerStepIds
     * @return
     */
    List<MtRouterStep> routerStepBatchGet(Long tenantId, List<String> routerStepIds);

    /**
     * operationStepQuery-获取工艺对应的步骤清单
     *
     * @param tenantId
     * @param operationId
     * @param routerId
     * @return
     */
    List<String> operationStepQuery(Long tenantId, String operationId, String routerId);

    /**
     * parentStepQuery-获取上层步骤
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterStepVO6 parentStepQuery(Long tenantId, String routerStepId);

    /**
     * childStepGet获取下层步骤
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    MtRouterStepVO10 subStepQuery(Long tenantId, String routerStepId);

    /**
     * stepRouterStepGet-基于步骤识别码获取工艺路线步骤
     *
     * @param tenantId
     * @param routerId
     * @param stepName
     * @return
     */
    String stepNameLimitRouterStepGet(Long tenantId, String routerId, String stepName);

    /**
     * routerStepQueueDecisionTypeGet-工艺路线步骤队列决策类型查询
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    String routerStepQueueDecisionTypeGet(Long tenantId, String routerStepId);

    /**
     * routerStepListQuery-基于工艺路线ID获取工艺路线步骤清单
     *
     * @param tenantId
     * @param routerId
     * @return
     */
    List<MtRouterStepVO5> routerStepListQuery(Long tenantId, String routerId);

    /**
     * routerEntryRouterStepGet-获取工艺路线入口步骤
     *
     * update remarks 2019-07-18 添加通过KeyStepFlag校验逻辑
     *
     * @param tenantId
     * @param routerId
     * @return List
     */
    List<String> routerEntryRouterStepGet(Long tenantId, String routerId);

    /**
     * routerEntryRouterStepBatchGet-工艺路线入口步骤获取
     *
     * @author benjamin
     * @date 2020/11/3 2:19 PM
     * @param tenantId 租户Id
     * @param routerIds 工艺路线Id列表
     * @return List
     */
    List<MtRouterStepVO17> routerEntryRouterStepBatchGet(Long tenantId, List<String> routerIds);

    /**
     * bottomStepOperationQuery-获取底层步骤及工艺
     *
     * @param tenantId
     * @param routerStepId
     * @return
     */
    List<MtRouterVO> bottomStepOperationQuery(Long tenantId, String routerStepId);

    /**
     * 获取工艺路线组件并计算用量/sen.luo 2018-04-01
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterStepVO1> routerComponentQtyCalculate(Long tenantId, MtRouterStepVO2 dto);

    /**
     * propertyLimitRouterStepPropertyQuery-根据属性获取工艺路线步骤信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterStepVO12> propertyLimitRouterStepPropertyQuery(Long tenantId, MtRouterStepVO11 dto);

    /**
     * eoLimitUnStartRouterStepQuery
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtRouterStepVO14> eoLimitUnStartRouterStepQuery(Long tenantId, MtRouterStepVO13 dto);

    /**
     * routerStepAttrPropertyUpdate-工艺路线步骤新增&更新扩展表属性
     *
     * @Author Xie.yiyang
     * @Date 2019/11/12 14:35
     * @param tenantId, vo
     * @return java.lang.Void
     */
    void routerStepAttrPropertyUpdate(Long tenantId, MtExtendVO10 vo);

    /**
     * stepNameLimitRouterStepQuery-基于步骤识别码批量获取工艺路线步骤
     *
     * @param tenantId
     * @param routerId
     * @return
     */
    List<MtRouterStep> stepNameLimitRouterStepQuery(Long tenantId, List<String> routerId);

    /**
     * routerStepListBatchQuery-基于工艺路线ID批量获取工艺路线步骤清单
     *
     * @param tenantId
     * @param routerIds
     * @return
     */
    List<MtRouterStepVO15> routerStepListBatchQuery(Long tenantId, List<String> routerIds);

    /**
     * operationStepQuery-获取工艺对应的步骤清单(批量)
     *
     * @param tenantIds
     * @param operationIds
     * @param routerIds
     * @return
     */
    List<String> operationStepBatchQuery(Long tenantIds, List<String> operationIds, List<String> routerIds);
}
