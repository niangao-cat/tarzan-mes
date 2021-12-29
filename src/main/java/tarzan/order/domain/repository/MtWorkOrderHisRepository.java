package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.order.domain.entity.MtWorkOrderHis;
import tarzan.order.domain.vo.MtWorkOrderHisVO;
import tarzan.order.domain.vo.MtWorkOrderHisVO1;
import tarzan.order.domain.vo.MtWorkOrderHisVO2;

/**
 * 生产指令历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:34:08
 */
public interface MtWorkOrderHisRepository extends BaseRepository<MtWorkOrderHis>, AopProxy<MtWorkOrderHisRepository> {

    /**
     * woHisPropertyQuery-获取生产指令变更历史
     * 
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtWorkOrderHis> woHisPropertyQuery(Long tenantId, MtWorkOrderHisVO condition);

    /**
     * woLimitWoHisQuery-获取指定生产指令的所有历史记录
     * 
     * @param tenantId
     * @param workOrderId
     */
    List<MtWorkOrderHisVO1> woLimitWoHisQuery(Long tenantId, String workOrderId);

    /**
     * eventLimitWoHisQuery-获取指定事件的生产指令历史记录
     * 
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtWorkOrderHis> eventLimitWoHisQuery(Long tenantId, String eventId);

    /**
     * eventLimitWoHisBatchQuery-获取一批事件的生产指令历史记录
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtWorkOrderHis> eventLimitWoHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * woLatestHisGet-获取生产指令最新历史
     *
     * @author chuang.yang
     * @date 2019/9/27
     * @param tenantId
     * @param workOrderId
     * @return tarzan.order.domain.vo.MtWorkOrderHisVO2
     */
    MtWorkOrderHisVO2 woLatestHisGet(Long tenantId, String workOrderId);
}
