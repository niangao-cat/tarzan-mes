package tarzan.actual.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.actual.domain.entity.MtEoStepActualHis;

/**
 * 执行作业-工艺路线步骤执行实绩资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 15:58:53
 */
public interface MtEoStepActualHisRepository
                extends BaseRepository<MtEoStepActualHis>, AopProxy<MtEoStepActualHisRepository> {

    /**
     * 获取特定执行作业步骤实绩历史
     * 
     * @param tenantId
     * @param eoId
     * @return
     */
    List<MtEoStepActualHis> eoStepActualHisQuery(Long tenantId, String eoId);

    /**
     * 获取特定事件执行作业步骤实绩历史
     * 
     * @param tenantId
     * @param eventId
     * @return
     */
    List<MtEoStepActualHis> eoStepActualHisByEventQuery(Long tenantId, String eventId);

}
