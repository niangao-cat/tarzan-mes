package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomSubstituteHis;

/**
 * 装配清单行替代项历史资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteHisRepository
                extends BaseRepository<MtBomSubstituteHis>, AopProxy<MtBomSubstituteHisRepository> {

    /**
     * bomSubstituteHisQuery-获取装配清单组件行替代项历史
     *
     * @param tenantId
     * @param bomSubstituteId
     * @return
     */
    List<MtBomSubstituteHis> bomSubstituteHisQuery(Long tenantId, String bomSubstituteId);

    /**
     * eventLimitBomSubstituteHisBatchQuery 获取一批事件的装配清单组件行替代项属性历史
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtBomSubstituteHis> eventLimitBomSubstituteHisBatchQuery(Long tenantId, List<String> eventIds);

}
