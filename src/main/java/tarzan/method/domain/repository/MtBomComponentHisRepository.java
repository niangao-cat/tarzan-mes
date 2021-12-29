package tarzan.method.domain.repository;

import java.util.List;

import org.hzero.core.base.AopProxy;
import org.hzero.mybatis.base.BaseRepository;

import tarzan.method.domain.entity.MtBomComponentHis;

/**
 * 装配清单行历史表资源库
 *
 * @author xinlin.zhao@hand-china.com 2019-07-30 17:16:06
 */
public interface MtBomComponentHisRepository
                extends BaseRepository<MtBomComponentHis>, AopProxy<MtBomComponentHisRepository> {

    /**
     * bomComponentHisQuery-获取装配清单组件行历史
     *
     * @param tenantId
     * @param bomComponentId
     * @return
     */
    List<MtBomComponentHis> bomComponentHisQuery(Long tenantId, String bomComponentId);

    /**
     * eventLimitBomComponentHisBatchQuery 获取一批事件的装配清单组件行历史
     * 
     * @param tenantId
     * @param eventIds
     * @return
     */
    List<MtBomComponentHis> eventLimitBomComponentHisBatchQuery(Long tenantId, List<String> eventIds);

}
