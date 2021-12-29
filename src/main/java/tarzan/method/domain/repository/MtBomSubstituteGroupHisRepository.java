package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomSubstituteGroupHis;

/**
 * 装配清单行替代组历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomSubstituteGroupHisRepository
                extends BaseRepository<MtBomSubstituteGroupHis>, AopProxy<MtBomSubstituteGroupHisRepository> {

    /**
     * bomSubstituteGroupHisQuery-获取装配清单组件行替代组历史
     *
     * @param tenantId
     * @param bomSubstituteGroupId
     * @return
     */
    List<MtBomSubstituteGroupHis> bomSubstituteGroupHisQuery(Long tenantId, String bomSubstituteGroupId);

    /**
     * eventLimitBomSubstituteGroupHisBatchQuery 获取一批事件的装配清单组件行替代组属性历史
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtBomSubstituteGroupHis> eventLimitBomSubstituteGroupHisBatchQuery(Long tenantId, List<String> eventIds);

}
