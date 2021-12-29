package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.vo.MtRouterVO21;
import tarzan.method.domain.vo.MtRouterVO8;
import tarzan.order.domain.entity.MtEoRouter;
import tarzan.order.domain.vo.*;

/**
 * EO工艺路线资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoRouterRepository extends BaseRepository<MtEoRouter>, AopProxy<MtEoRouterRepository> {

    /**
     * eoRouterGet-获取指定执行作业的工艺路线
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    String eoRouterGet(Long tenantId, String eoId);

    /**
     * eoRouterVerify-验证执行作业工艺路线是否满足使用条件
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    void eoRouterVerify(Long tenantId, String eoId);

    /**
     * eoRouterBomMatchValidate-验证执行作业工艺路线与装配清单是否一致
     *
     * @param tenantId
     * @param eoId
     */
    void eoRouterBomMatchValidate(Long tenantId, String eoId);

    /**
     * eoRouterUpdate-更新执行作业工艺路线
     *
     * @param tenantId
     * @param vo
     */
    MtEoRouterVO2 eoRouterUpdate(Long tenantId, MtEoRouterVO vo);

    /**
     * eoEntryStepGet-获取执行作业入口步骤
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    List<String> eoEntryStepGet(Long tenantId, String eoId);

    /**
     * eoEntryStepBatchGet-获取执行作业入口步骤
     *
     * @author benjamin
     * @date 2020/11/3 2:35 PM
     * @param tenantId 租户Id
     * @param eoIds 执行作业Id列表
     * @return List
     */
    List<MtEoRouterVO4> eoEntryStepBatchGet(Long tenantId, List<String> eoIds);

    /**
     * eoRouterUpdateVerify-验证执行作业是否达到更改工艺路线的要求
     *
     * @param tenantId
     * @param dto
     * @return
     */
    void eoRouterUpdateVerify(Long tenantId, MtEoRouterVO1 dto);

    /**
     * eoRouterBatchUpdate-批量更新执行作业工艺路线
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2019/11/21
     */
    List<MtEoRouterVO2> eoRouterBatchUpdate(Long tenantId, MtEoRouterVO3 dto);

    /**
     * 根据EoIds 批量获取eoRouter
     */
    List<MtEoRouter> eoRouterBatchGet(Long tenantId, List<String> eoIds);
}
