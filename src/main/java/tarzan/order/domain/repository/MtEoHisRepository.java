package tarzan.order.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.order.domain.entity.MtEoHis;
import tarzan.order.domain.vo.MtEoHisVO;
import tarzan.order.domain.vo.MtEoHisVO1;
import tarzan.order.domain.vo.MtEoHisVO2;

/**
 * 执行作业历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:33:47
 */
public interface MtEoHisRepository extends BaseRepository<MtEoHis>, AopProxy<MtEoHisRepository> {
    /**
     * eoHisPropertyGet-获取执行作业变更历史
     *
     * @param tenantId
     * @param condition
     * @return
     */
    List<MtEoHis> eoHisPropertyQuery(Long tenantId, MtEoHisVO condition);

    /**
     * eoLimitEoHisQuery-获取指定执行作业的所有历史记录
     *
     * @param tenantId
     * @param eoId
     * @return
     */
    List<MtEoHisVO1> eoLimitEoHisQuery(Long tenantId, String eoId);

    /**
     * eventLimitEoHisBatchQuery-获取一批事件的生产指令历史记录
     *
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtEoHis> eventLimitEoHisBatchQuery(Long tenantId, List<String> eventIds);

    /**
     * eventLimitEoHisQuery-获取指定事件的执行作业历史记录
     *
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtEoHis> eventLimitEoHisQuery(Long tenantId, String eventId);

    /**
     * 获取执行作业最新历史
     * @Author peng.yuan
     * @Date 2019/9/28 11:22
     * @param tenantId :
     * @param eoId : 执行作业id
     * @return tarzan.order.domain.vo.MtEoHisVO2
     */
    MtEoHisVO2 eoLatestHisGet(Long tenantId, String eoId);
}
