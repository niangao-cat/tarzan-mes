package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtWorkOrderActual;
import tarzan.actual.domain.vo.*;

/**
 * 生产指令实绩资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderActualRepository
                extends BaseRepository<MtWorkOrderActual>, AopProxy<MtWorkOrderActualRepository> {

    /**
     * woActualGet-根据生产指令ID或生产指令实绩ID获取生产指令实绩信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    MtWorkOrderActual woActualGet(Long tenantId, MtWorkOrderActualVO vo);

    /**
     * woActualUpdate-根据生产指令更新生产指令实绩并记录实绩历史
     *
     * @param tenantId
     * @param dto
     */
    MtWorkOrderActualVO5 woActualUpdate(Long tenantId, MtWorkOrderActualVO4 dto, String fullUpdate);

    /**
     * 根据生产指令的开始结束时间获取生产周期
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    MtWorkOrderActualVO2 woProductionPeriodGet(Long tenantId, MtWorkOrderActualVO2 dto);

    /**
     * 查询实际工单信息/sen.luo 2018-03-19
     * 
     * @param tenantId
     * @param workOrderIds
     * @return
     */
    List<MtWorkOrderActual> queryWorkOrderActual(Long tenantId, List<String> workOrderIds);

    /**
     * 查询实际工单信息/penglin.sui 2021-07-05
     *
     * @param tenantId
     * @param workOrderIds
     * @return
     */
    List<MtWorkOrderActual> queryWorkOrderActualForUpdate(Long tenantId, List<String> workOrderIds);

    /**
     * woScrap-生产指令报废
     *
     * @author chuang.yang
     * @date 2019/3/19
     * @param tenantId
     * @param dto
     * @return void
     */
    void woScrap(Long tenantId, MtWorkOrderActualVO1 dto);

    /**
     * woScrapCancel-生产指令报废取消
     *
     * @author chuang.yang
     * @date 2019/3/20
     * @param tenantId
     * @param dto
     * @return void
     */
    void woScrapCancel(Long tenantId, MtWorkOrderActualVO1 dto);

    /**
     * propertyLimitWOActualPropertyQuery-根据属性获取生产指令实绩信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtWorkOrderActualVO7> propertyLimitWOActualPropertyQuery(Long tenantId, MtWorkOrderActualVO6 vo);

    /**
     * woActualBatchUpdate-根据生产指令更新生产指令实绩并记录实绩历史(批量)
     *
     * @param tenantId
     * @param dto
     * @author guichuan.li
     * @date 2020/10/26
     */
    List<MtWorkOrderActualVO5> woActualBatchUpdate(Long tenantId, MtWorkOrderActualVO8 dto, String fullUpdate);
}
