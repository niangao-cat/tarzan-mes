package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoActual;
import tarzan.actual.domain.vo.*;

/**
 * 执行作业【执行作业需求和实绩拆分开】资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoActualRepository extends BaseRepository<MtEoActual>, AopProxy<MtEoActualRepository> {
    /**
     * eoActualGet-根据执行作业ID或执行作业实绩ID获取执行作业实绩信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    MtEoActual eoActualGet(Long tenantId, MtEoActualVO vo);

    /**
     * eoProductionPeriodGet-根据执行作业的开始结束时间获取生产周期
     *
     * @param tenantId
     * @return
     */
    MtEoActualVO3 eoProductionPeriodGet(Long tenantId, MtEoActualVO2 dto);

    /**
     * eoActualUpdate-更新执行作业实绩并记录实绩历史
     *
     * @param tenantId
     * @param dto
     */
    MtEoActualVO7 eoActualUpdate(Long tenantId, MtEoActualVO4 dto, String fullUpdate);

    /**
     * eoWorking-执行作业开工
     *
     * @param tenantId
     * @param dto
     */
    void eoWorking(Long tenantId, MtEoActualVO5 dto);

    /**
     * eoWorkingCancel-执行作业开工取消
     *
     * @param tenantId
     * @param dto
     */
    void eoWorkingCancel(Long tenantId, MtEoActualVO5 dto);

    /**
     * eoScrap-执行作业报废
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoScrap(Long tenantId, MtEoActualVO6 dto);

    /**
     * eoScrapCancel-执行作业报废取消
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void eoScrapCancel(Long tenantId, MtEoActualVO6 dto);

    /**
     * propertyLimitEoActualPropertyQuery-根据属性获取执行作业实绩信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoActualVO9> propertyLimitEoActualPropertyQuery(Long tenantId, MtEoActualVO8 vo);

    /**
     * eoActualBatchUpdate-批量更新执行作业实绩并记录实绩历史
     *
     * @Author Xie.yiyang
     * @Date 2019/11/26 9:48
     * @param tenantId
     * @param vo
     * @param fullUpdate
     * @return void
     */
    List<MtEoActualVO12> eoActualBatchUpdate(Long tenantId, MtEoActualVO11 vo, String fullUpdate);

    /**
     * eoActualBatchGetByEoIds-根据EO_ID 批量获取eoActual
     */
    List<MtEoActual> eoActualBatchGetByEoIds(Long tenantId, List<String> eoIds);

    /**
     * eoBatchWorking-执行作业开工(批量)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/26
     */
    void eoBatchWorking(Long tenantId, MtEoActualVO13 dto);
}
