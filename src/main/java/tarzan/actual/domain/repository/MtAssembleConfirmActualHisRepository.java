package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtAssembleConfirmActualHis;
import tarzan.actual.domain.vo.MtAssembleConfirmActualHisVO;
import tarzan.actual.domain.vo.MtAssembleConfirmActualHisVO1;

/**
 * 装配确认实绩历史，指示执行作业组件材料的装配和确认历史记录情况资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtAssembleConfirmActualHisRepository
                extends BaseRepository<MtAssembleConfirmActualHis>, AopProxy<MtAssembleConfirmActualHisRepository> {

    /**
     * 获取装配确认实绩的变更历史
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtAssembleConfirmActualHisVO1> assembleConfirmActualLimitHisQuery(Long tenantId,
                                                                           MtAssembleConfirmActualHisVO dto);

    /**
     * 批量获取指定事件的装配确认实绩/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtAssembleConfirmActualHis> eventLimitAssembleConfirmActualHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * 获取指定事件的装配确认实绩/sen.luo 2018-03-22
     * 
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtAssembleConfirmActualHis> eventLimitAssembleConfirmActualHisQuery(Long tenantId, String eventId);

}
