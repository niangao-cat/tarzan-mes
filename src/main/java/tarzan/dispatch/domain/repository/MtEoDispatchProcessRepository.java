package tarzan.dispatch.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.dispatch.domain.entity.MtEoDispatchProcess;
import tarzan.dispatch.domain.vo.*;

/**
 * 调度过程处理表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 16:54:58
 */
public interface MtEoDispatchProcessRepository
                extends BaseRepository<MtEoDispatchProcess>, AopProxy<MtEoDispatchProcessRepository> {

    /**
     * wkcShiftLimitDispatchedProcessEoQuery-获取指定班次正在调度的执行作业和调度信息
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchProcess> wkcShiftLimitDispatchedProcessEoQuery(Long tenantId, MtEoDispatchProcessVO1 dto);

    /**
     * dispatchedEoUpdate-更新执行作业调度过程数据
     *
     * @param tenantId
     * @param dto
     */
    String dispatchedEoUpdate(Long tenantId, MtEoDispatchProcessVO2 dto, String fullUpdate);

    /**
     * toBeDispatchedEoDispatchableQtyGet-获取执行作业的可调度数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    Double toBeDispatchedEoDispatchableQtyGet(Long tenantId, MtEoDispatchProcessVO8 dto);

    /**
     * operationLimitToBeDispatchedEoQuery-根据输入的工艺获取排队且未进行调度的执行作业及调度数量
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchProcessVO4> operationLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO3 dto);

    /**
     * planTimeLimitToBeDispatchedEoQuery-根据计划时间限制获取需调度的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchProcessVO4> planTimeLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO5 dto);

    /**
     * wkcShiftLimitToBeDispatchedEoQuery-获取工作单元指定班次需调度的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchProcessVO4> wkcShiftLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO6 dto);

    /**
     * wkcShiftPeriodLimitToBeDispatchedEoQuery-获取工作单元指定班次预设时间范围内需调度的执行作业
     *
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtEoDispatchProcessVO4> wkcShiftPeriodLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO7 dto);

    /**
     * propertyLimitDispatchedProcessPropertyQuery-根据获取调度过程信息
     *
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoDispatchProcessVO10> propertyLimitDispatchedProcessPropertyQuery(Long tenantId, MtEoDispatchProcessVO9 vo);


    /**
     * propertyLimitDispatchedProcessPropertyBatchQuery-根据批量属性获取调度过程信息
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtEoDispatchProcessVO10> propertyLimitDispatchedProcessPropertyBatchQuery(Long tenantId,
                                                                                   MtEoDispatchProcessVO13 vo);

    /**
     * 获取指定范围内需调度的执行作业及调度数量
     * 
     * @Author peng.yuan
     * @Date 2019/10/14 18:01
     * @param tenantId :
     * @param dto :
     * @return tarzan.dispatch.domain.vo.MtEoDispatchProcessVO12
     */
    List<MtEoDispatchProcessVO12> rangeLimitToBeDispatchedEoQuery(Long tenantId, MtEoDispatchProcessVO11 dto);

    /**
     * dispatchedEoAssignQtyBatchGet-批量获取执行作业已调度数量
     *
     * @author chuang.yang
     * @date 2019/12/19
     * @param tenantId
     * @param dto
     * @return java.util.List<tarzan.dispatch.domain.vo.MtEoDispatchProcessVO14>
     */
    List<MtEoDispatchProcessVO14> dispatchedEoAssignQtyBatchGet(Long tenantId, MtEoDispatchProcessVO15 dto);
}
