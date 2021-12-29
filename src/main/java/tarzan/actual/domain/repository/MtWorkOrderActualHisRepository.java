package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtWorkOrderActualHis;
import tarzan.actual.domain.vo.MtWorkOrderActualHisVO;
import tarzan.actual.domain.vo.MtWorkOrderActualHisVO2;

/**
 * 生产指令实绩历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtWorkOrderActualHisRepository
                extends BaseRepository<MtWorkOrderActualHis>, AopProxy<MtWorkOrderActualHisRepository> {

    /**
     * woActualHisPropertyQuery-获取生产指令实绩变更历史
     * 
     * @param tenantId
     * @param dto
     * @return
     */
    List<MtWorkOrderActualHis> woActualHisPropertyQuery(Long tenantId, MtWorkOrderActualHisVO dto);


    /**
     * eventLimitWoActualHisQuery-获取指定事件的生产指令实绩历史记录
     * 
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtWorkOrderActualHis> eventLimitWoActualHisQuery(Long tenantId, String eventId);


    /**
     * eventLimitWoActualHisBatchQuery-获取一批事件的生产指令实绩历史记录
     * 
     * @param tenantId
     * @param ls
     * @return
     */
    List<MtWorkOrderActualHis> eventLimitWoActualHisBatchQuery(Long tenantId, List<String> ls);


    /**
     * woLimitWoActualHisQuery-获取指定生产指令或生产指令实绩的所有历史记录
     * 
     * @param tenantId
     * @param vo
     * @return
     */
    List<MtWorkOrderActualHisVO2> woLimitWoActualHisQuery(Long tenantId, MtWorkOrderActualHisVO vo);

}
